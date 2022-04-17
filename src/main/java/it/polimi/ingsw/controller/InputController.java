package it.polimi.ingsw.controller;

import it.polimi.ingsw.exceptions.FullGameException;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.view.VirtualView;

import java.util.ArrayList;

public class InputController {
    public InputController(int a, boolean b){}
    public void elaborateMessage(Message mess){}

    public void addPlayer(String nick, VirtualView vv) throws FullGameException {}
    public void playerDisconnected(String nick){}
    public ArrayList<String> getNicknames(){return new ArrayList<>();}
}
