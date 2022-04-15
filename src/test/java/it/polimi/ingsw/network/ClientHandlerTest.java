package it.polimi.ingsw.network;

import it.polimi.ingsw.controller.InputController;
import it.polimi.ingsw.network.messages.*;
import org.junit.jupiter.api.Test;

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

    @Test
    void run() {
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
                fail();
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