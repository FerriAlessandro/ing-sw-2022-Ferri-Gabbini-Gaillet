package it.polimi.ingsw.network;

import it.polimi.ingsw.controller.InputController;
import it.polimi.ingsw.network.messages.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

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
            clientHandler.sendMessage(new RMessageNickname("Pippo"));
            clientHandler.sendMessage(new SMessage(MessageType.S_LOBBY));
            clientHandler.sendMessage(new SMessage(MessageType.S_WIN));
            clientHandler.sendMessage(new PingMessage());
            clientHandler.sendMessage(new SMessage(MessageType.R_DISCONNECT));
            while(Thread.activeCount() > 2){ assert true; }
            assertEquals(MessageType.S_LOBBY, contMess.get(0));
            assertEquals(MessageType.S_WIN, contMess.get(1));
            assertEquals(2, contMess.size());
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    void disconnection(){
        try{
            TimeUnit.SECONDS.sleep(1);
        } catch (Exception e){
            e.printStackTrace();
            fail();
        }
        mockClient.disconnect();
    }

}


/**
 * Echo client socket to be used in testing. Sends back each {@link Message} it receives until the {@link MessageType} is
 * {@link MessageType#R_DISCONNECT}
 * @author A.G. Gaillet
 */
class MockClientSocket extends Thread{
    Socket socket;
    ObjectOutputStream out;
    ObjectInputStream in;
    boolean disconnected = false;

    public MockClientSocket() {

    }

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
                out.writeObject(message);
                if (message.getType().equals(MessageType.R_DISCONNECT)){
                    socket.close();
                    Thread.currentThread().interrupt();
                    break;
                }
            } catch (Exception e){
                if(!Thread.currentThread().isInterrupted() && !disconnected){
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