package it.polimi.ingsw.network;

import it.polimi.ingsw.controller.InputController;
import it.polimi.ingsw.exceptions.FullGameException;
import it.polimi.ingsw.exceptions.NotExistingPlayerException;
import it.polimi.ingsw.exceptions.UnavailableNicknameException;
import it.polimi.ingsw.network.messages.*;
import it.polimi.ingsw.view.VirtualView;

import java.io.*;
import java.net.Socket;

/**
 * This class handles communications with one client.
 * @author A.G. Gaillet
 * @version 1.0
 * @see Socket
 * @see ClientSocket
 */
public class ClientHandler extends Thread {
    private final Socket clientSocket;
    private final ObjectInputStream in;
    private final ObjectOutputStream out;
    private final InputController controller;
    private String playerNickname = null;
    private final Timer timeout;

    /** Last message received by the only remaining connected client. Stored to be used when other(s) reconnect */
    public static Message queued = null;

    /** Last action message that was sent to any player. Used as cache for when the current player disconnects and reconnects (and only one player remains connected in the meantime)*/
    public static SMessage lastUsefulSent = null;

    /**
     * Constructor to be used for players that are not first.
     *
     * @param socket     to be used for communication
     * @param controller of the current game
     * @throws IOException when unable to get {@link InputStream} or {@link OutputStream}
     */
    public ClientHandler(Socket socket, InputController controller) throws IOException {
        this(socket, new ObjectInputStream(socket.getInputStream()), new ObjectOutputStream(socket.getOutputStream()), controller);
    }

    /**
     * Constructor to be used for players who are first.
     *
     * @param socket     to be used for communication
     * @param in         {@link ObjectInputStream} of the given {@link Socket}
     * @param out        {@link ObjectOutputStream} of the given {@link Socket}
     * @param controller of the current game
     */
    public ClientHandler(Socket socket, ObjectInputStream in, ObjectOutputStream out, InputController controller) {
        this.clientSocket = socket;
        this.in = in;
        this.out = out;
        this.controller = controller;
        timeout = new Timer(this);
    }

    /**
     * This method elaborates messages received by the socket.
     */
    public void run() {
        if (in == null) return;

        askNickname();

        //Receive messages
        timeout.start();
        while (!Thread.currentThread().isInterrupted() && !clientSocket.isClosed()) {
            try {
                Message inMessage = (Message) in.readObject();
                System.out.println("\u001b[38;5;244m" + "Message received, type: " + inMessage.getType() + "Client Handler: " + Thread.currentThread().getId() + " - " + playerNickname + "\u001b[0m");

                if (inMessage.getType().equals(MessageType.R_DISCONNECT)) {
                    disconnect();
                } else if (!inMessage.getType().equals(MessageType.PING)) {
                    synchronized (timeout) {
                        timeout.euthanize();
                    }

                    //If only one player is connected the message is stored until the other player(s) reconnects
                    if (Server.oneRemaining && !inMessage.getType().equals(MessageType.R_NICKNAME)){
                        queued = inMessage;
                    } else {
                        try {
                            controller.elaborateMessage(inMessage);
                        } catch (RuntimeException e){
                            e.printStackTrace();
                            sendMessage(new SMessageInvalid(e.getMessage()));
                            sendMessage(new SMessage(MessageType.S_TRYAGAIN));
                        }
                    }

                    synchronized (timeout) {
                        timeout.reset();
                        timeout.revive();
                    }
                } else {
                    timeout.reset();
                }
            } catch (Exception ignored) {
                Thread.currentThread().interrupt();
                //System.out.println("Invalid input or corrupted input stream");
            }
        }
    }

    /**
     * Method to be called to send a message to the linked client.
     *
     * @param message the {@link Message} to be sent
     */
    public void sendMessage(SMessage message) {
        try {
            if(isActionMessage(message)){
                //Saves last action message sent so it can be used in case the current player disconnects and only 2 players are present
                System.out.println("\n\nLast useful sent: " + message.getType());
                lastUsefulSent = message;
            }

            System.out.println("Trying to send this message: " + message.getType() + " to " + playerNickname);
            out.writeObject(message);

        } catch (IOException e) {
            System.out.println("Unable to send the given message");
        }
    }

    /**
     * Discerns between action and informative messages.
     *
     * @return true if this message asks the player to perform an action, false otherwise
     */
    private boolean isActionMessage(SMessage message){
        return  !message.getType().equals(MessageType.S_INVALID) && !message.getType().equals(MessageType.S_TRYAGAIN) &&
                !message.getType().equals(MessageType.S_PLAYER) && !message.getType().equals(MessageType.S_GAMESTATE)
                && !message.getType().equals(MessageType.S_NICKNAME) && !message.getType().equals(MessageType.S_EXPERT)
                && !message.getType().equals(MessageType.S_ASSISTANTSTATUS) && !message.getType().equals(MessageType.S_WIN) &&
                !message.getType().equals(MessageType.S_LOBBY) && !message.getType().equals(MessageType.S_DISCONNECT);
    }

    /**
     * Handles disconnection of the client closing the socket and interrupting the current Thread. Notifies the controller of the disconnection.
     */
    public void disconnect() {
        System.out.println("Disconnection of: " + clientSocket.getInetAddress());
        if (playerNickname != null) {
            try {
                Server.removeClientHandler(this);
            }catch (Exception ignored){}
            controller.playerDisconnected(playerNickname);
        }
        timeout.interrupt();
        try {
            out.flush();
            clientSocket.close();
        } catch (Exception ignored) {}
        this.interrupt();
    }

    /**
     * Getter method for the nickname of the associated player.
     *
     * @return {@link String} nickname of the associated player
     */
    public String getPlayerNickname() {
        return playerNickname;
    }

    /**
     * Handles the beginning of the connection by asking for nickname and checking for existing saved game.
     */
    public void askNickname(){
        //Wait for nickname message
        boolean validNickName = false;
        try {

            Message inMessage;
            do {
                System.out.println("ClientHandler sending nickname message");
                sendMessage(new SMessage(MessageType.S_NICKNAME));
                System.out.println("ClientHandler waiting for nickname");
                do {
                    inMessage = (Message) in.readObject();
                }while (inMessage.getType().equals(MessageType.PING));
                synchronized (controller) {
                    if (inMessage.getType().equals(MessageType.R_NICKNAME)) {
                        RMessageNickname nickMessage = (RMessageNickname) inMessage;

                        if(Server.restored || Server.disconnectionResilient) {

                            System.out.println("Nickname for " + clientSocket.getInetAddress() + " is " + nickMessage.nickname);
                            this.playerNickname = nickMessage.nickname;
                            try {
                                VirtualView virtualView = new VirtualView(this);
                                controller.restorePlayer(nickMessage.nickname, virtualView);
                                validNickName = true;

                            } catch (FullGameException e) {
                                //Notifies of full game and closes the connection without notifying the controller
                                sendMessage(new SMessageInvalid(e.getMessage()));

                                try {
                                    out.flush();
                                    clientSocket.close();
                                } catch (Exception ignored) {}
                                this.interrupt();

                            } catch (NotExistingPlayerException | UnavailableNicknameException e) {
                                sendMessage(new SMessageInvalid(e.getMessage()));
                            }

                        }else {

                            System.out.println("Nickname for " + clientSocket.getInetAddress() + " is " + nickMessage.nickname);
                            this.playerNickname = nickMessage.nickname;
                            try {
                                VirtualView virtualView = new VirtualView(this);
                                controller.addPlayer(nickMessage.nickname, virtualView);
                                validNickName = true;
                            } catch (FullGameException e) {
                                e.printStackTrace();
                            } catch (UnavailableNicknameException e){
                                sendMessage(new SMessageInvalid(e.getMessage()));
                            }

                        }

                    }
                }
            } while (!inMessage.getType().equals(MessageType.R_NICKNAME) || !validNickName);

        } catch (Exception e) {
            System.out.println("Networking error, unable to receive nickname on CH " + Thread.currentThread().getId());
            disconnect();
        }
    }

}
