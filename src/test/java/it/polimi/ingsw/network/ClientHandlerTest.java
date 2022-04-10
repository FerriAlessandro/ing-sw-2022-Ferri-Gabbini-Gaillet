package it.polimi.ingsw.network;

import it.polimi.ingsw.network.messages.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ClientHandlerTest {
    ClientHandler clientHandler;
    ArrayList<MessageType> contMess = new ArrayList<>();
    ControllerMockTestUtil inCont = new ControllerMockTestUtil(1, false, contMess);
    Socket socket = null;
    ClientSocketMockTestUtil mockClient;

    @Test
    void run() {
        try {
            ServerSocket svSock = new ServerSocket(2351);
            mockClient = new ClientSocketMockTestUtil();
            mockClient.start();
            socket =  svSock.accept();
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
            while(Thread.activeCount() > 2){ }
            mockClient.interrupt();
            assertEquals(MessageType.S_LOBBY, contMess.get(0));
            assertEquals(MessageType.S_WIN, contMess.get(1));
            assertEquals(2, contMess.size());
        } catch (Exception e) {
            fail();
        }
    }

}