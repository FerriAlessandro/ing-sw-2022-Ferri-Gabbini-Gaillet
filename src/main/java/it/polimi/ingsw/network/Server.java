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
 * Class that represent the Server. It contains the server-side entrypoint.
 * @author A.G. Gaillet
 * @version 1.0
 */
public class Server {
    int port = 2351;
    int numCurrentGame = 0;
    int numRequiredGame = 0;
    InputController controller;

    /**
     * Main function
     * @param args port value to override default value
     */
    public void main(String[] args){
        if (args.length == 1){
            port = Integer.parseInt(args[0]);
        }else if (args.length > 1){
            System.out.println("Invalid number of arguments\n");
            return;
        }

        try{
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Server listening - port: " + port);

            while (true){
                Socket socket = serverSocket.accept();
                System.out.println("New client connected - address: " + socket.getInetAddress().toString());
                if (numCurrentGame == 0){
                    try {
                        ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
                        ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
                        Message message = null;
                        do {
                            outputStream.writeObject(new SMessage(MessageType.S_GAMESETTINGS));
                            try {
                                message = (Message) inputStream.readObject();
                            } catch (ClassNotFoundException e) {
                                e.printStackTrace();
                                break;
                            }
                        }while (!message.getType().equals(MessageType.R_GAMESETTINGS));
                        if (message != null) {
                            numRequiredGame = ((RMessageGameSettings) message).numPlayers;
                            boolean expertGame = ((RMessageGameSettings) message).expert;
                            controller = new InputController(numRequiredGame, expertGame);
                            new ClientHandler(socket, inputStream, outputStream, controller).start();
                        }
                    } catch (IOException ioException){
                        System.out.println("Unable to get the input stream");
                        ioException.printStackTrace();
                    }

                }else {
                    new ClientHandler(socket, controller).start();
                    numCurrentGame = numCurrentGame + 1;
                    if (numRequiredGame == numCurrentGame){
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
