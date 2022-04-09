package it.polimi.ingsw.network;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.MessageType;
import it.polimi.ingsw.network.messages.RMessageNickname;
import it.polimi.ingsw.view.VirtualView;

import java.io.*;
import java.net.Socket;

/**
 * This class handles communications with one client.
 * @author A.G. Gaillet
 * @version 1.0
 */
public class ClientHandler extends Thread{
    Socket clientSocket;
    ObjectInputStream in;
    ObjectOutputStream out;
    final Object writeLock = new Object();
    final InputController controller;
    //TODO: Add final attribute controller

    public ClientHandler(Socket socket, InputController controller){
        this.clientSocket = socket;
        this.controller = controller;
        try {
            in = new ObjectInputStream(clientSocket.getInputStream());
        } catch (IOException ioException){
            System.out.println("Unable to get the input stream");
            ioException.printStackTrace();
        }
        try {
            out = new ObjectOutputStream(clientSocket.getOutputStream());
        } catch (IOException ioException){
            System.out.println("Unable to get the output stream");
            ioException.printStackTrace();
            return;
        }
    } //TODO: add controller param

    public ClientHandler(Socket socket, ObjectInputStream in, ObjectOutputStream out, InputController controller) {
        this.clientSocket = socket;
        this.in = in;
        this.out = out;
        this.controller = controller;
    }

    public void run() {
        if (in == null) return;

        //Wait for nickname message
        try {
            Message inMessage = (Message) in.readObject();
            do {
                if(inMessage.getType().equals(MessageType.R_DISCONNECT)){
                    Thread.currentThread().interrupt();
                    System.out.println("Thread interrupted, closing connection\n");
                    break;
                }else {
                    if (inMessage.getType().equals(MessageType.R_NICKNAME)) {
                        RMessageNickname nickMessage= (RMessageNickname) inMessage;
                        System.out.println("Nickname for " + clientSocket.getInetAddress() + "is " + nickMessage.nickname);

                        VirtualView virtualView = new VirtualView(this);
                        //TODO: addClient to controller (nickname, virtualView)
                    }
                }
            } while(!inMessage.getType().equals(MessageType.R_NICKNAME));
        } catch (Exception e){
            System.out.println("Invalid input");
            e.printStackTrace();
        }

        //Receive messages
        while (!Thread.currentThread().isInterrupted()){
            try {
                Message inMessage = (Message) in.readObject();

                if(inMessage.getType().equals(MessageType.R_DISCONNECT)){
                    Thread.currentThread().interrupt();
                    System.out.println("Thread interrupted, closing connection\n");
                }else{
                    //TODO: pass message onto appropriate controller
                }
            } catch (Exception e){
                System.out.println("Invalid input");
                e.printStackTrace();
            }
        }
    }


    public void sendMessage(Message message){
        synchronized (writeLock){
            try {
                out.writeObject(message);
            } catch (IOException e){
                System.out.println("Unable to send the given message");
            }
        }
    }

}
