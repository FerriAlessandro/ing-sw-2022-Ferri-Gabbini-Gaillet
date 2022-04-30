package it.polimi.ingsw.network;

import it.polimi.ingsw.controller.InputController;
import it.polimi.ingsw.exceptions.FullGameException;
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
public class ClientHandler extends Thread{
    private final Socket clientSocket;
    private final ObjectInputStream in;
    private final ObjectOutputStream out;
    private final InputController controller;
    private String playerNickname = null;

    /**
     * Constructor to be used for players that are not first.
     * @param socket to be used for communication
     * @param controller of the current game
     * @throws IOException when unable to get {@link InputStream} or {@link OutputStream}
     */ ClientHandler(Socket socket, InputController controller) throws IOException {
        this(socket, new ObjectInputStream(socket.getInputStream()), new ObjectOutputStream(socket.getOutputStream()), controller);
    }

    /**
     * Constructor to be used for players who are first.
     * @param socket to be used for communication
     * @param in {@link ObjectInputStream} of the given {@link Socket}
     * @param out {@link ObjectOutputStream} of the given {@link Socket}
     * @param controller of the current game
     */
    public ClientHandler(Socket socket, ObjectInputStream in, ObjectOutputStream out, InputController controller) {
        this.clientSocket = socket;
        this.in = in;
        this.out = out;
        this.controller = controller;
    }

    /**
     * This method elaborates messages received by the socket.
     */
    public void run() {
        if (in == null) return;
        boolean validNickName = false;

        //Wait for nickname message
        try {
            Message inMessage;
            do {
                inMessage = (Message) in.readObject();
                if(inMessage.getType().equals(MessageType.R_DISCONNECT)){
                    disconnect();
                    break;
                }else {
                    synchronized (controller) {
                        if (inMessage.getType().equals(MessageType.R_NICKNAME)) {
                            RMessageNickname nickMessage = (RMessageNickname) inMessage;
                            if (!controller.getNicknames().contains(nickMessage.nickname)) {
                                System.out.println("Nickname for " + clientSocket.getInetAddress() + " is " + nickMessage.nickname);
                                this.playerNickname = nickMessage.nickname;
                                validNickName = true;
                            } else {
                                sendMessage(new SMessageInvalid("Nickname already taken"));
                                sendMessage(new SMessage(MessageType.S_NICKNAME));
                            }
                            VirtualView virtualView = new VirtualView(this);
                            try {
                                controller.addPlayer(nickMessage.nickname, virtualView);
                            } catch (FullGameException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            } while(!inMessage.getType().equals(MessageType.R_NICKNAME) || !validNickName);
        } catch (Exception e){
            if(!e.getClass().equals(EOFException.class)) {
                //System.out.println("Invalid input or corrupted input stream");
                e.printStackTrace();
            }else{
                disconnect();
            }
        }

        //Receive messages
        while (!Thread.currentThread().isInterrupted()){
            try {
                Message inMessage = (Message) in.readObject();
                //System.out.println("Message received, type: " + inMessage.getType());

                if(inMessage.getType().equals(MessageType.R_DISCONNECT)){
                    disconnect();
                }else if(!inMessage.getType().equals(MessageType.PING)){
                    controller.elaborateMessage(inMessage);
                }
            } catch (Exception e){
                //System.out.println("Invalid input or corrupted input stream");
                e.printStackTrace();
            }
        }
    }

    /**
     * Method to be called to send a message to the linked client.
     * @param message the {@link Message} to be sent
     */
    public void sendMessage(SMessage message){
        try {
            out.writeObject(message);
        } catch (IOException e){
            System.out.println("Unable to send the given message");
        }
    }

    /**
     * Handles disconnection of the client closing the socket and interrupting the current Thread. Notifies the controller of the disconnection.
     */
    private void disconnect(){
        System.out.println(clientSocket.getInetAddress() + " is being disconnected");

        try{
            out.flush();
            clientSocket.close();
            if (playerNickname != null) {
                controller.playerDisconnected(playerNickname);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        Thread.currentThread().interrupt();
    }

    /**
     * Getter method for the nickname of the associated player.
     * @return {@link String} nickname of the associated player
     */
    public String getPlayerNickname(){
        return playerNickname;
    }
}
