package it.polimi.ingsw.view;

import it.polimi.ingsw.network.messages.*;

import java.util.ArrayList;

/**
 * Generic interface to be implemented by every View.
 *
 * @author A.G. Gaillet
 * @version 1.0
 */
public interface ViewInterface {

    void askNickName(SMessage message);

    void askNumOfPlayers(SMessage message);

    void askMotherNatureMove(SMessage message);

    void showAssistantChoice();

    void showLobby (SMessageLobby message);

    void showDisconnectionMessage();

    void showBoard(SMessageGameState gameState);

    void showWinMessage(SMessageWin message);

}
