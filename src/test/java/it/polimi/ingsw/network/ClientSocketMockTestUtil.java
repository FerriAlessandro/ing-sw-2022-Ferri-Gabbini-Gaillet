package it.polimi.ingsw.network;

import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.MessageType;
import it.polimi.ingsw.network.messages.SMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Echo client socket to be used in testing. Sends back each {@link Message} it receives until the {@link MessageType} is
 * {@link MessageType#R_DISCONNECT}
 */
public class ClientSocketMockTestUtil extends Thread{
    Socket socket;
    ObjectOutputStream out;
    ObjectInputStream in;

    public ClientSocketMockTestUtil() {

    }

    public void run(){
        try {
            socket = new Socket("localhost", 2351);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
        }catch (Exception e){
            e.printStackTrace();
        }

        while (true){
            try {
                Message message = (Message) in.readObject();
                out.writeObject(message);
                if (message.getType().equals(MessageType.R_DISCONNECT)){
                    socket.close();
                    break;
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

}
