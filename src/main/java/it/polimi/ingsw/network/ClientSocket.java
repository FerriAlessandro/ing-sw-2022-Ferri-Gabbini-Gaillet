package it.polimi.ingsw.network;

import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.MessageType;
import it.polimi.ingsw.network.messages.PingMessage;
import it.polimi.ingsw.network.messages.SMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
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
    private Socket socket;
    private ObjectOutputStream out;
    private final ScheduledExecutorService heartbeat = newScheduledThreadPool(1);
    private final Adapter adapter;
    final Object sendLock = new Object();

    /**
     * Constructor for the {@link ClientSocket}.
     * @param ip the ip address of the desired server
     * @param port the port number of the desired server
     */
    public ClientSocket(String ip, int port, Adapter adapter){
        this.adapter = adapter;
        this.ip = ip;
        this.port = port;
    }

    /**
     * This method runs the thread that receives the incoming messages.
     */
    @Override
    public void run() {
        try{
            socket = new Socket(ip, port);
            ObjectInputStream in;

            synchronized (sendLock) {
                out = new ObjectOutputStream(socket.getOutputStream());
                in = new ObjectInputStream(socket.getInputStream());
            }

            startHeartbeat();

            while (!Thread.currentThread().isInterrupted()){
                try {
                    Message inMessage = (Message) in.readObject();
                    System.out.println("Received " + inMessage.getType());
                    adapter.elaborateMessage(inMessage);

                } catch (Exception e){
                    if(!Thread.currentThread().isInterrupted()){
                        System.out.println(System.nanoTime());
                        System.out.println("Invalid input or corrupted input stream\n");
                        e.printStackTrace();
                        closeConnection();
                    }else{
                        closeConnection();
                    }
                    break;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    /**
     * This method sends a message using the linked socket.
     * @param message to be sent
     * @throws IOException when unable to send the message
     */
    public void sendMessage(Message message) throws IOException{
        synchronized (sendLock){
            out.writeObject(message);
            if(message.getType().equals(MessageType.R_DISCONNECT)){
                closeConnection();
            }
        }
    }

    /**
     * This method starts an executor which sends {@link PingMessage} at a fixed interval.
     */
    public void startHeartbeat(){
        System.out.println("Starting heartbeat");
        heartbeat.scheduleWithFixedDelay( () -> {
            try {
                sendMessage(new PingMessage());
            } catch (Exception e) {
                System.out.println("Lost connection to server");
                closeConnection();
            }
        }, 0, 1, TimeUnit.SECONDS );
    }

    /**
     * This method stops the ping executor.
     */
    public void stopHeartbeat(){
        if (!heartbeat.isShutdown()) {
            heartbeat.shutdownNow();
            System.out.println("Stopping heartbeat");
        }
    }

    /**
     * This method closes the socket and terminates the connection.
     */
    private void closeConnection(){
        System.out.println("Closing connection");
        try{
            out.flush();
        }catch (Exception ignored){}
        try {
            stopHeartbeat();
            socket.close();
            System.out.println("Closed client socket");
            this.interrupt();
        } catch (IOException ex) {
            System.out.println("Unable to close socket");
            ex.printStackTrace();
        }
        adapter.elaborateMessage(new SMessage(MessageType.DISCONNECTED));
    }

}
