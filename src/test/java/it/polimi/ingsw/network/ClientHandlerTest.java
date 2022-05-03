package it.polimi.ingsw.network;

import it.polimi.ingsw.controller.InputController;
import it.polimi.ingsw.network.messages.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ClientHandlerTest {
    ClientHandler clientHandler;
    ArrayList<MessageType> contMess = new ArrayList<>();
    MockController inCont = new MockController(1, false, contMess);
    Socket socket = null;
    MockClientSocket mockClient;


    @BeforeEach
    void SetUp(){
        try {
            ServerSocket svSock = new ServerSocket(2351);
            mockClient = new MockClientSocket();
            mockClient.start();
            socket =  svSock.accept();
            svSock.close();
            clientHandler = new ClientHandler(socket, inCont);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Something went wrong during setup\n");
            fail();
        }
        clientHandler.start();
    }

    @Test
    void run() {
        try {
            int threadCount = Thread.activeCount();
            System.out.println(threadCount);
            mockClient.sendMessage(new RMessageNickname("Pippo"));
            clientHandler.sendMessage(new SMessage(MessageType.S_LOBBY));
            clientHandler.sendMessage(new SMessage(MessageType.S_WIN));
            mockClient.sendMessage(new PingMessage());
            clientHandler.sendMessage(new SMessage(MessageType.R_DISCONNECT));
            while (clientHandler.isAlive()){ assert true; }
            assertEquals(MessageType.S_NICKNAME, contMess.get(0));
            assertEquals(MessageType.S_LOBBY, contMess.get(1));
            assertEquals(MessageType.S_WIN, contMess.get(2));
            assertEquals(3, contMess.size());
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    void disconnection(){
        mockClient.sendMessage(new RMessageNickname("Pippo"));
        while (clientHandler.isAlive()){ assert true; }
        assertEquals(MessageType.S_NICKNAME, contMess.get(0));
        assertEquals(1, contMess.size());
    }

}


/**
 * Echo client socket to be used in testing. Sends back each {@link Message} it receives until the {@link MessageType} is
 * {@link MessageType#R_DISCONNECT}
 * @author A.G. Gaillet
 */
class MockClientSocket extends Thread{
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private boolean disconnected = false;
    private final Object lock = new Object();

    public MockClientSocket() {}

    public void run(){
        try {
            socket = new Socket("localhost", 2351);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
        }catch (Exception e){
            fail();
        }

        while (!Thread.currentThread().isInterrupted()){
            try {
                Message message = (Message) in.readObject();
                sendMessage(message);
                if (message.getType().equals(MessageType.R_DISCONNECT)){
                    socket.close();
                    Thread.currentThread().interrupt();
                    break;
                }
            } catch (Exception e){
                if(!Thread.currentThread().isInterrupted() && !disconnected && !e.getClass().equals(EOFException.class)){
                    e.printStackTrace();
                    fail();
                }
            }
        }
    }

    public void disconnect() {
        try {
            socket.close();
            disconnected = true;
            System.out.println("Closed client socket");
            Thread.currentThread().interrupt();
        } catch (IOException ex) {
            System.out.println("Unable to close socket");
            ex.printStackTrace();
        }
    }

    public void sendMessage(Message message){
        synchronized (lock) {
            try {
                out.writeObject(message);
                if(message.getType().equals(MessageType.R_DISCONNECT)){
                    disconnect();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

/**
 * Mock controller to be used in testing.
 * @author A.G. Gaillet
 */
class MockController extends InputController {
    public final ArrayList<MessageType> arr;

    public MockController(int a, boolean b, ArrayList<MessageType> arr) {
        super(a, b);
        this.arr = arr;
    }

    @Override
    public void elaborateMessage(Message mess) {
        synchronized (arr) {
            arr.add(mess.getType());
        }
    }

}

