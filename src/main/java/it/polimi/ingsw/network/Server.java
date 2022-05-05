package it.polimi.ingsw.network;

import it.polimi.ingsw.controller.DiskManager;
import it.polimi.ingsw.controller.InputController;
import it.polimi.ingsw.network.messages.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Class that represent the Server. It contains the server-side entrypoint and handles initial communication with
 * the clients before creating specific {@link ClientHandler}s.
 * @author A.G. Gaillet
 * @version 1.0
 * @see ServerSocket
 * @see ClientHandler
 */
public class Server {

    static ObjectInputStream inputStream;

    /**
     * Main function
     * @param args port value to override default value
     */
    public static void main(String[] args){
        int port = 2351;
        int numCurrentGame = 0;
        int numRequiredGame = 0;
        InputController controller = null;


        if (args.length == 1){
            port = Integer.parseInt(args[0]);
        }else if (args.length > 1){
            System.out.println("Invalid number of arguments\n");
            return;
        }

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server listening - port: " + port);
            while (true) {
                System.out.println("Waiting for next player");
                Socket socket = serverSocket.accept();
                System.out.println("New client connected - address: " + socket.getInetAddress().toString());
                if (numCurrentGame == 0) {
                    System.out.println("This is the first player");
                    //IF PLAYER IS FIRST PLAYER OF CURRENT GAME:
                    try {
                        ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
                        System.out.println("Generated out stream");
                        inputStream = new ObjectInputStream(socket.getInputStream());
                        Message message;
                        System.out.println("Generated in stream");
                        do {
                            //Ask for number of players and type of game
                            System.out.println("Asking for game settings");
                            outputStream.writeObject(new SMessage(MessageType.S_GAMESETTINGS));
                            message = receiveMessageIgnorePing();
                        } while (message == null || !message.getType().equals(MessageType.R_GAMESETTINGS));
                        System.out.println("Received game settings");

                        //Cast message
                        numRequiredGame = ((RMessageGameSettings) message).numPlayers;
                        boolean expertGame = ((RMessageGameSettings) message).expert;

                        controller = new InputController(DiskManager.loadGame(numRequiredGame, expertGame));
                        if(controller.getGameController() != null){
                            //A saved game was found
                            do {
                                //Ask for number of players and type of game
                                System.out.println("Asking whether the user wants to load a previous game");
                                outputStream.writeObject(new SMessage(MessageType.S_LOADGAME));
                                message = receiveMessageIgnorePing();
                            } while (message == null || !message.getType().equals(MessageType.R_LOADGAME));
                        }

                        if(controller.getGameController() == null || !((RMessageLoadGame) message).use){
                            //Create new game
                            controller = new InputController(numRequiredGame, expertGame);
                            System.out.println("Creating new game");
                            ClientHandler.restored = false;
                        }else{
                            System.out.println("Using loaded save");
                            ClientHandler.restored = true;
                        }

                        new ClientHandler(socket, inputStream, outputStream, controller).start();
                        numCurrentGame += 1;

                    } catch (IOException ioException) {
                        System.out.println("Unable to get a stream");
                        ioException.printStackTrace();
                    }

                } else {
                    //IF AT LEAST ONE OTHER PLAYER IS ALREADY CONNECTED
                    try {
                        //Use existing game settings (using existing controller)
                        new ClientHandler(socket, controller).start();
                    }catch (IOException e){
                        e.printStackTrace();
                        System.out.println("Unable to create clientHandler");
                    }
                    numCurrentGame += 1;
                    if (numRequiredGame == numCurrentGame) {
                        //Reset for new game
                        //PLEASE NOTE THAT MULTIPLE GAMES ARE CURRENTLY UNSUPPORTED!!
                        // TODO: add support for multiple concurrent games or for multiple consecutive games
                        numCurrentGame = 0;
                    }
                }
             }
        } catch (IOException e){
            System.out.println("Unable to create the socket\n");
            e.printStackTrace();
        }
    }

    // TODO: add javadoc
    private static Message receiveMessageIgnorePing(){
        Message message = null;
        try {
            do {
                message = (Message) inputStream.readObject();
            }while (message.getType().equals(MessageType.PING));

            System.out.println("Received " + message.getType() + " message");
        } catch (Exception e) {
            System.out.println(System.nanoTime());
            e.printStackTrace();
        }
        return message;
    }
}
