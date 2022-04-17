package it.polimi.ingsw.network;

import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.view.ViewInterface;
import it.polimi.ingsw.view.VirtualView;

import java.io.IOException;

/**
 * This class masks the network implementation to the {@link ViewInterface} on the client side.
 * @author A.G. Gaillet
 * @version 1.0
 */
public class Adapter {
    ViewInterface view;
    ClientSocket socket;

    /**
     * Constructor used for mock classes that extend this one. To be used for testing.
     */
    public Adapter(){}

    /**
     * Default constructor. Used in testing.
     * @param view the {@link ViewInterface} linked to this {@link Adapter}
     */
    public Adapter(ViewInterface view){
        this.view = view;
        socket = new ClientSocket(this);
        socket.start();
    }

    /**
     * Constructor that allows for overriding of default ip and port values.
     * @param view the {@link ViewInterface} linked to this {@link Adapter}
     * @param ip address of the server you wish to connect to
     * @param port of the server you wish to connect to
     */
    public Adapter(ViewInterface view, String ip, int port){
        this.view = view;
        this.socket = new ClientSocket(ip, port, this);
        socket.start();
    }

    /**
     * Elaborates messages coming from the {@link ClientSocket} and invokes the appropriate method of the linked {@link ViewInterface}
     * @param message received by the {@link ClientSocket}
     */
    public void elaborateMessage(Message message){

    }

    /**
     * Send the message through the linked {@link ClientSocket}
     * @param message to be sent
     */
    public void sendMessage(Message message){
        try {
            socket.sendMessage(message);
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
