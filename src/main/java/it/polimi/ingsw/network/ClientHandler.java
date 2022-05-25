package it.polimi.ingsw.network;

import it.polimi.ingsw.controller.InputController;
import it.polimi.ingsw.exceptions.FullGameException;
import it.polimi.ingsw.exceptions.NotExistingPlayerException;
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

    /** True when the current game has been restored from a save file, false otherwise */
    public static boolean restored = false;

    /** True if a player was disconnected and the game is set up for them to reconnect, false otherwise */
    public static boolean disconnectionResilient = false;

    /** Last message received by the only remaining connected client. Stored to be used when other(s) reconnect */
    public static Message queued = null;

    /** True if only one player is still connected */
    public static boolean oneRemaining = false;

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
                System.out.println("Message received, type: " + inMessage.getType() + "Client Handler:" + Thread.currentThread().getId());

                if (inMessage.getType().equals(MessageType.R_DISCONNECT)) {
                    disconnect();
                } else if (!inMessage.getType().equals(MessageType.PING)) {
                    synchronized (timeout) {
                        timeout.euthanize();
                    }

                    //If only one player is connected the message is stored until the other player(s) reconnects
                    if (oneRemaining && !inMessage.getType().equals(MessageType.R_NICKNAME)){
                        queued = inMessage;
                    } else {
                        controller.elaborateMessage(inMessage);
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
            out.writeObject(message);

            if(isActionMessage(message)){
                //Saves last action message sent so it can be used in case the current player disconnects and only 2 players are present
                System.out.println("\n\nLast useful sent: " + message.getType());
                lastUsefulSent = message;
            }

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
        return !message.getType().equals(MessageType.S_INVALID) && !message.getType().equals(MessageType.S_TRYAGAIN) &&
                !message.getType().equals(MessageType.S_PLAYER) && !message.getType().equals(MessageType.S_GAMESTATE)
                && !message.getType().equals(MessageType.S_NICKNAME) && !message.getType().equals(MessageType.S_EXPERT);
    }

    /**
     * Handles disconnection of the client closing the socket and interrupting the current Thread. Notifies the controller of the disconnection.
     */
    public void disconnect() {
        System.out.println("Disconnection of: " + clientSocket.getInetAddress());
        if (playerNickname != null) {
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

                        if(restored || disconnectionResilient) {


                            if(!controller.getGameController().playersView.containsKey(nickMessage.nickname)) {
                                System.out.println("Nickname for " + clientSocket.getInetAddress() + " is " + nickMessage.nickname);
                                this.playerNickname = nickMessage.nickname;
                                try {
                                    VirtualView virtualView = new VirtualView(this);
                                    controller.getGameController().restorePlayer(nickMessage.nickname, virtualView);
                                    validNickName = true;

                                } catch (FullGameException e) {
                                    //Notifies of full game and closes the connection without notifying the controller
                                    sendMessage(new SMessageInvalid("The current game is already full. Please try again later"));
                                    e.printStackTrace();
                                    try {
                                        out.flush();
                                        clientSocket.close();
                                    } catch (Exception ignored) {}
                                    this.interrupt();

                                } catch (NotExistingPlayerException e){
                                    sendMessage(new SMessageInvalid("No pre-existing player had this nickname"));
                                    e.printStackTrace();
                                }

                            } else {
                                sendMessage(new SMessageInvalid("Nickname already taken"));
                            }

                        }else {

                            if (!controller.getNicknames().contains(nickMessage.nickname)) {
                                System.out.println("Nickname for " + clientSocket.getInetAddress() + " is " + nickMessage.nickname);
                                this.playerNickname = nickMessage.nickname;
                                try {
                                    VirtualView virtualView = new VirtualView(this);
                                    controller.addPlayer(nickMessage.nickname, virtualView);
                                    validNickName = true;
                                } catch (FullGameException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                sendMessage(new SMessageInvalid("Nickname already taken"));
                            }

                        }

                    }
                }
            } while (!inMessage.getType().equals(MessageType.R_NICKNAME) || !validNickName);

        } catch (Exception e) {
            if (!e.getClass().equals(EOFException.class)) {
                //System.out.println("Invalid input or corrupted input stream");
                e.printStackTrace();
            } else {
                System.out.println("EOF exception received");
                disconnect();
            }
        }
    }

}
