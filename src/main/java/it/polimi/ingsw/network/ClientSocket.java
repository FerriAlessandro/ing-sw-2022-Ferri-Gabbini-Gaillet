package it.polimi.ingsw.network;

import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.MessageType;
import it.polimi.ingsw.network.messages.PingMessage;
import it.polimi.ingsw.network.messages.SMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.Executors.newScheduledThreadPool;

/**
 * This class implements the methods needed to send and receive messages on the client-side.
 * @author A.G. Gaillet
 * @version 1.0
 * @see Socket
 * @see ClientHandler
 */
public class ClientSocket extends Thread {
    String ip;
    int port;
    Socket socket;
    ObjectInputStream in;
    ObjectOutputStream out;
    ScheduledExecutorService heartbeat = newScheduledThreadPool(1);
    boolean kill;
    Adapter adapter;

    /**
     * Constructor for the {@link ClientSocket}; creates a {@link Socket} and gets the {@link ObjectInputStream} and {@link ObjectOutputStream}.
     * @param ip the ip address of the desired server
     * @param port the port number of the desired server
     * @throws IOException when unable to connect
     */
    public ClientSocket(String ip, int port, Adapter adapter) throws IOException {
        this.adapter = adapter;
        this.ip = ip;
        this.port = port;
        socket = new Socket(ip, port);
        try{
            in = new ObjectInputStream(socket.getInputStream());
            out = new ObjectOutputStream(socket.getOutputStream());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * This method runs the thread that receives the incoming messages.
     */
    @Override
    public void run() {
        kill = false;
        startHeartbeat();
        while (!kill){
            try {
                Message inMessage = (Message) in.readObject();

                try{
                    adapter.elaborateMessage(inMessage);}
                catch (UnsupportedOperationException e){
                    sendMessage(new SMessage(MessageType.S_ERROR));
                }

            } catch (Exception e){
                System.out.println("Invalid input or corrupted input stream\n");
                e.printStackTrace();
                closeConnection();
                break;
            }
        }

    }

    /**
     * This method sends a message using the linked socket.
     * @param message to be sent
     * @throws IOException when unable to send the message
     */
    public void sendMessage(Message message) throws IOException{
        if (message.getType().equals(MessageType.R_DISCONNECT)) {
            kill = true;
            stopHeartbeat();
            closeConnection();
        }
        out.writeObject(message);
    }

    /**
     * This method starts an executor which sends {@link PingMessage} at a fixed interval.
     */
    private void startHeartbeat(){
        heartbeat.scheduleWithFixedDelay( () -> {
            try {
                sendMessage(new PingMessage());
            } catch (IOException e) {
                System.out.println("Lost connection to server\n");
                adapter.elaborateMessage(new SMessage(MessageType.DISCONNECTED));
                closeConnection();
                e.printStackTrace();
            }
        }, 0, 1, TimeUnit.SECONDS );
    }

    /**
     * This method stops the ping executor.
     */
    private void stopHeartbeat(){
        heartbeat.shutdownNow();
    }

    /**
     * This method closes the socket and terminates the connection.
     */
    private void closeConnection(){
        try {
            socket.close();
        } catch (IOException ex) {
            System.out.println("Unable to close socket\n");
            ex.printStackTrace();
        }
    }

}
