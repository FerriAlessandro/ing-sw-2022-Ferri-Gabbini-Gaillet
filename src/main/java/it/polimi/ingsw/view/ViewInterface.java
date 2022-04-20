package it.polimi.ingsw.view;

import it.polimi.ingsw.network.messages.*;

/**
 * Generic interface to be implemented by every View.
 *
 * @author A.G. Gaillet
 * @version 1.0
 */
public interface ViewInterface {

    /**
     * Ask the user to provide a valid nickname.
     */
    void askNickName();

    /**
     * Ask the user to provide the number of desired players and the desired game mode.
     */
    void askGameSettings();

    /**
     * Ask the player to move mother nature.
     */
    void askMotherNatureMove();

    /**
     * Ask the player to pick an assistant card from provided available cards.
     */
    void showAssistantChoice();

    /**
     * Display lobby.
     * @param message containing information on connected and desired players.
     */

    void showLobby (SMessageLobby message);

    /**
     * Display a disconnection message.
     */
    void showDisconnectionMessage();

    /**
     * Shows the game status displaying the board.
     * @param gameState message containing game status.
     */
    void showBoard(SMessageGameState gameState);

    /**
     * Display a "someone has won" message.
     * @param message containing information on who won.
     */
    void showWinMessage(SMessageWin message);

    /**
     * Display a generic text message.
     * @param message containing the {@link String} to be displayed.
     */
    void showGenericMessage(SMessageInvalid message);

    /**
     * Ask player to pick a character card among provided available options.
     */
    void showCharacterChoice();

    /**
     * Ask the player to move a student.
     */
    void askMove();

    /**
     * Ask the player to pick a cloud.
     */
    void askCloud();

    /**
     * To be used to re-execute the last prompt.
     * In client implementations this method only shows an error message. The adapter is responsible for error handling.
     */
    void askAgain();
}
