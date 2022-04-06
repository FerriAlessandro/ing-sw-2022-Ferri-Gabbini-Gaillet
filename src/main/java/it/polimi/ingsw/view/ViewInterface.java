package it.polimi.ingsw.view;

import it.polimi.ingsw.model.enumerations.Color;

import java.util.ArrayList;
import java.util.Map;
import java.util.Observer;

/**
 * Generic interface to be implemented by every View.
 *
 * @author A.G. Gaillet
 * @version 1.0
 */
public interface ViewInterface {

    void askNickName();

    void askNumOfPlayers();

    void askMotherNatureMove(int pos);

    void showAssistantChoice();

    void showLobby (ArrayList<String> nicknames, int requiredNum);

    void showTextMessage(String genericMessage);

    void showDisconnectionMessage();

    void showBoard(Map<String, ArrayList<Color>> studEntrance, Map<String, Map<Color, Integer>> studDining,
                   Map<Integer, ArrayList<Color>> studIslands, Map<Integer, Integer> towerIslands, Map<Integer, Integer> forbiddenTokens);

    void showCoins(int coins);

    void showWinMessage(String winner);

}
