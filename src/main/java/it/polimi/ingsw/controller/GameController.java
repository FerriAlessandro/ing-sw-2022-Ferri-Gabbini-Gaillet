package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.enumerations.Phase;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.view.VirtualView;

import java.util.Map;

public class GameController {

    private Game game;
    private Phase gamePhase;
    //TODO Add Character cards status
    private Map<String, VirtualView > playersView;
    private boolean isExpert;
    private final int numOfPlayers;

    public GameController(int numOfPlayers, boolean isExpert) {
        this.numOfPlayers = numOfPlayers;
        this.isExpert = isExpert;
    }
    public Phase getGamePhase() { return gamePhase; }

    public VirtualView getVirtualView(String nickname) {
        return playersView.get(nickname);
    }

    public void elaborateMessage(Message message) {}

    public void askAgain(Message message) {}
}
