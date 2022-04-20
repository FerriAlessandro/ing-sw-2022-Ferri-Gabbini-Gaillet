package it.polimi.ingsw.network;

import it.polimi.ingsw.network.messages.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class ClientSocketTest {
    ClientSocket clientSocket;
    Socket clientHandlerSocket;
    MockClientHandler clientHandler;
    MockAdapter adapter = new MockAdapter();

    @BeforeEach
    void SetUp(){
        try {
            ServerSocket svSock = new ServerSocket(2351);
            clientSocket = new ClientSocket("localhost", 2351, adapter);
            clientSocket.start();

            clientHandlerSocket =  svSock.accept();
            svSock.close();
            TimeUnit.SECONDS.sleep(1); //Needed because output stream in clientSocket needs to be set before a clientHandler can be created
            clientHandler = new MockClientHandler(clientHandlerSocket);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Something went wrong during setup\n");
            fail();
        }
        clientHandler.start();
        clientSocket.startHeartbeat();
    }

    @Test
    @DisplayName("Send-Receive")
    void run() {
        try{
        clientSocket.sendMessage(new SMessage(MessageType.S_LOBBY));
        clientSocket.sendMessage(new SMessage(MessageType.S_WIN));
        clientSocket.sendMessage(new SMessage(MessageType.R_DISCONNECT));
        } catch (Exception e){
            e.printStackTrace();
            fail();
        }

        while(adapter.messageTypes.size() < 2){
            assert true;
        }
        assertEquals(MessageType.S_LOBBY, adapter.messageTypes.get(0));
        assertEquals(MessageType.S_WIN, adapter.messageTypes.get(1));
    }

    @Test
    @DisplayName("Heartbeat")
    void heartbeat() throws IOException, InterruptedException {
        clientHandlerSocket.close();
        TimeUnit.SECONDS.sleep(1);
    }
}

class MockClientHandler extends Thread {
    Socket clientSocket;
    ObjectInputStream in;
    ObjectOutputStream out;

    public MockClientHandler(Socket socket) {
        this.clientSocket = socket;
        try {
            out = new ObjectOutputStream(clientSocket.getOutputStream());
        } catch (IOException ioException){
            System.out.println("Unable to get the output stream");
            ioException.printStackTrace();
        }
        try {
            in = new ObjectInputStream(clientSocket.getInputStream());
        } catch (IOException ioException){
            System.out.println("Unable to get the input stream");
            ioException.printStackTrace();
        }
    }

    public void run(){
        while (!Thread.currentThread().isInterrupted()){
            try {
                Message inMessage = (Message) in.readObject();

                if(inMessage.getType().equals(MessageType.R_DISCONNECT)){
                    Thread.currentThread().interrupt();
                    clientSocket.close();
                }else if(!inMessage.getType().equals(MessageType.PING)){
                    out.writeObject(inMessage);
                }
            } catch (Exception e){
                if(!Thread.currentThread().isInterrupted()) {
                    System.out.println("Lost connection to client");
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

}

class MockAdapter extends Adapter{
    public MockAdapter(){
        super();
    }
    public final ArrayList<MessageType> messageTypes = new ArrayList<>();
    @Override
    public void elaborateMessage(Message mess) {
        messageTypes.add(mess.getType());
    }
}
