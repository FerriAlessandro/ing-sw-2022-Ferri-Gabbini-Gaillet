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

    void askMotherNatureMove(SMessageMotherNature messageMotherNature);

    /**
     * Ask the player to pick an assistant card from provided available cards.
     *
     * @param message message containing available assistants
     */
    void showAssistantChoice(SMessageShowDeck message);

    /**
     * Ask player to pick a character card among provided available options.
     *
     * @param messageCharacter message containing available characters
     */
    void showCharacterChoice(SMessageCharacter messageCharacter);

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

    /**
     * Update current player.
     */
    void showCurrentPlayer(SMessageCurrentPlayer messageCurrentPlayer);

    /**
     * Getter method for the nickname of the associated player
     * @return {@link String} nickname of the associated player
     */
    String getNickName();

    /**
     * Asks additional information on chosen character effect of
     * {@link it.polimi.ingsw.model.enumerations.Characters#GRANDMA_HERB} or
     * {@link it.polimi.ingsw.model.enumerations.Characters#HERALD}.
     * @param message request message
     */
    void grandmaHerbHeraldScene(SMessageGrandmaherbHerald message);

    /**
     * Asks additional information on chosen character effect of
     * {@link it.polimi.ingsw.model.enumerations.Characters#MONK} or
     * {@link it.polimi.ingsw.model.enumerations.Characters#SPOILED_PRINCESS}.
     * @param message request message
     */
    void monkPrincessScene(SMessageMonkPrincess message);

    /**
     * Asks additional information on chosen character effect of
     * {@link it.polimi.ingsw.model.enumerations.Characters#ROGUE} or
     * {@link it.polimi.ingsw.model.enumerations.Characters#MUSHROOM_PICKER}.
     * @param message request message
     */
    void rogueMushroomPickerScene(SMessageRogueMushroomPicker message);

    /**
     * Asks additional information on chosen character effect of
     * {@link it.polimi.ingsw.model.enumerations.Characters#JESTER} or
     * {@link it.polimi.ingsw.model.enumerations.Characters#BARD}.
     * @param message request message
     */
    void jesterBardScene(SMessageJesterBard message);

    /**
     * Ask the user whether to use the loaded game save or not.
     */
    void askUseSavedGame();

    /**
     * Used to set the client flag for expert game handling.
     * @param messageExpert message containing the flag value
     */
    void setExpert(SMessageExpert messageExpert);
}
