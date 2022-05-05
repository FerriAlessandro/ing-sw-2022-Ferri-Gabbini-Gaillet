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
    public static boolean restored = false;

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
                    controller.elaborateMessage(inMessage);
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
        } catch (IOException e) {
            System.out.println("Unable to send the given message");
        }
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

                        if(restored) {

                            if(controller.getGameController().playerExisted(nickMessage.nickname)){
                                if(!controller.getNicknames().contains(nickMessage.nickname)) {
                                    System.out.println("Nickname for " + clientSocket.getInetAddress() + " is " + nickMessage.nickname);
                                    this.playerNickname = nickMessage.nickname;
                                    try {
                                        VirtualView virtualView = new VirtualView(this);
                                        controller.getGameController().restorePlayer(nickMessage.nickname, virtualView);
                                        validNickName = true;
                                    } catch (FullGameException | NotExistingPlayerException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    sendMessage(new SMessageInvalid("Nickname already taken"));
                                }
                            } else {
                                sendMessage(new SMessageInvalid("No player of the saved game had this nickname"));
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
