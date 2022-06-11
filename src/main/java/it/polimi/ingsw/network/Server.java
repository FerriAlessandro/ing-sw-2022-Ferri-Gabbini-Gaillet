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

    private static ObjectInputStream inputStream;
    private static boolean firstPlayer;
    private static InputController controller;

    /** True when the current game has been restored from a save file, false otherwise */
    public static boolean restored;

    /** True if a player was disconnected and the game is set up for them to reconnect, false otherwise */
    public static boolean disconnectionResilient;

    /**
     * Main function
     * @param args port value to override default value
     */
    public static void main(String[] args){
        int port = 2351;
        reset();

        if (args.length == 1){
            port = Integer.parseInt(args[0]);
        }else if (args.length > 1){
            System.out.println("Invalid number of arguments\n");
            return;
        }

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("\u001b[33m" + "Server listening - port: " + port + "\u001b[0m");
            //noinspection InfiniteLoopStatement
            while (true) {
                System.out.println("Waiting for next player");
                Socket socket = serverSocket.accept();
                System.out.println("\u001b[36m" + "New client connected - address: " + socket.getInetAddress().toString() + "\u001b[0m");
                if (firstPlayer) {
                    System.out.println("This is the first player");
                    //IF PLAYER IS FIRST PLAYER OF CURRENT GAME:
                    try {

                        ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
                        System.out.println("Generated out stream");
                        inputStream = new ObjectInputStream(socket.getInputStream());
                        Message message;
                        System.out.println("Generated in stream");

                        message = new RMessageLoadGame(false);

                        controller = new InputController(DiskManager.loadGame());

                        if(controller.getGameController() != null){
                            //A saved game was found
                            do {
                                System.out.println("Asking whether the user wants to load a previous game");
                                outputStream.writeObject(new SMessageLoadGame(controller.getNicknames().size(), controller.getGameController().isExpert()));
                                message = receiveMessageIgnorePing();
                            } while (message == null || !message.getType().equals(MessageType.R_LOADGAME));
                        }

                        if(controller.getGameController() == null || !((RMessageLoadGame) message).use){
                            //If no save was found or user decided to play a new game -> create a new game
                            do {
                                //Ask for number of players and type of game
                                System.out.println("Asking for game settings");
                                outputStream.writeObject(new SMessage(MessageType.S_GAMESETTINGS));
                                message = receiveMessageIgnorePing();
                            } while (message == null || !message.getType().equals(MessageType.R_GAMESETTINGS));
                            System.out.println("Received game settings");

                            //Cast message
                            int numRequiredGame = ((RMessageGameSettings) message).numPlayers;
                            boolean expertGame = ((RMessageGameSettings) message).expert;

                            controller = new InputController(numRequiredGame, expertGame);
                            System.out.println("Creating new game");
                            restored = false;
                        }else{
                            //The user decided to use the loaded game save
                            System.out.println("Using loaded save");
                            restored = true;
                        }

                        new ClientHandler(socket, inputStream, outputStream, controller).start();
                        firstPlayer = false;

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
                }
             }
        } catch (IOException e){
            System.out.println("Unable to create the socket\n");
            e.printStackTrace();
        }
    }

    /**
     * Loop on input object stream until a {@link Message} that is not of type {@link MessageType#PING} is received.
     * @return the received {@link Message}
     */
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

    /**
     * Resets the {@link Server} to run a new game.
     */
    public static void reset(){
        System.out.println("\033[31;1;4m" + "SERVER RESET" + "\033[0m");
        firstPlayer = true;
        controller = null;
        disconnectionResilient = false;
        restored = false;
    }
}
