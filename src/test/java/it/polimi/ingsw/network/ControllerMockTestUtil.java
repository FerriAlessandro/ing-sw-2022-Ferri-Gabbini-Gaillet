package it.polimi.ingsw.network;

import it.polimi.ingsw.controller.InputController;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.MessageType;

import java.util.ArrayList;

public class ControllerMockTestUtil extends InputController {
    public final ArrayList<MessageType> arr;

    public ControllerMockTestUtil(int a, boolean b, ArrayList<MessageType> arr) {
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
