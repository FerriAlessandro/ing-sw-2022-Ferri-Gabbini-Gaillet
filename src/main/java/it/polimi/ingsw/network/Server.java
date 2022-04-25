package it.polimi.ingsw.network;

import it.polimi.ingsw.controller.InputController;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.MessageType;
import it.polimi.ingsw.network.messages.RMessageGameSettings;
import it.polimi.ingsw.network.messages.SMessage;

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
                Socket socket = serverSocket.accept();
                System.out.println("New client connected - address: " + socket.getInetAddress().toString());
                if (numCurrentGame == 0) {
                    //IF PLAYER IS FIRST PLAYER OF CURRENT GAME:
                    try {
                        ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
                        ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
                        Message message = null;

                        do {
                            //Ask for number of players and type of game
                            outputStream.writeObject(new SMessage(MessageType.S_GAMESETTINGS));
                            try {
                                message = (Message) inputStream.readObject();
                            } catch (ClassNotFoundException e) {
                                e.printStackTrace();
                            }
                        } while (message == null || !message.getType().equals(MessageType.R_GAMESETTINGS));

                        //Initialize game (via creation of new controller)
                        numRequiredGame = ((RMessageGameSettings) message).numPlayers;
                        boolean expertGame = ((RMessageGameSettings) message).expert;
                        controller = new InputController(numRequiredGame, expertGame);

                        new ClientHandler(socket, inputStream, outputStream, controller).start();
                        numCurrentGame += 1;

                    } catch (IOException ioException) {
                        System.out.println("Unable to get the input stream");
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
                        //TODO: add support for multiple concurrent games or for multiple consecutive games
                        numCurrentGame = 0;
                    }
                }
             }
        } catch (IOException e){
            System.out.println("Unable to create the socket\n");
            e.printStackTrace();
        }
    }

}
