package it.polimi.ingsw.controller;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import it.polimi.ingsw.exceptions.FullGameException;
import it.polimi.ingsw.exceptions.NotExistingPlayerException;
import it.polimi.ingsw.exceptions.UnavailableNicknameException;
import it.polimi.ingsw.model.enumerations.Characters;
import it.polimi.ingsw.model.enumerations.Color;
import it.polimi.ingsw.model.enumerations.Phase;
import it.polimi.ingsw.view.VirtualView;
import it.polimi.ingsw.network.messages.*;

/**
 * InputController's task is ensuring that every message from users is correct, both syntactically and
 * in the correct game phase. It has to come also from the correct player.
 * @author AlessandroG
 * @version 1.1
 */
public class InputController implements Serializable{

    @Serial
    private static final long serialVersionUID = 1L;
    private final GameController gameController;
    private Phase gamePhase;

    /**
     * Constructor instants gameController, passing the parameters received by the server.
     * @param numOfPlayers number of players
     * @param isExpert indicates if the game is normal or expert version
     */
    public InputController(int numOfPlayers, boolean isExpert){
        gameController = new GameController(numOfPlayers, isExpert);
    }

    /**
     * Constructor to be used when restoring a saved game.
     * @param gameController of the game to be restored
     */
    public InputController(GameController gameController){
        this.gameController = gameController;
    }

    /**
     * @return reference to linked {@link GameController}
     */
    public GameController getGameController() {
        return gameController;
    }

    /**
     * Method called by the clientHandler for sending messages
     * @param mess is the received message that has to be checked
     */
    public void elaborateMessage(Message mess){
        verifyMessage(mess);
    }

    /**
     * Method called by the clientHandler the first time a player tries to connect
     * @param nickname the player's nickname
     * @param virtualView the player's virtualView
     * @throws FullGameException when there are already the max number of players connected
     * @throws UnavailableNicknameException when the provided nickname has already been taken
     */
    public void addPlayer(String nickname, VirtualView virtualView) throws FullGameException, UnavailableNicknameException {
        if(nickname == null)
            throw new RuntimeException("Player's nickname received is null!");
        if(virtualView == null)
            throw new RuntimeException("VirtualView received is null!");
        gameController.addPlayer(nickname, virtualView);
    }

    /**
     * Method called by the clientHandler the first time a player tries to connect
     * @param nickname the player's nickname
     * @param virtualView the player's virtualView
     * @throws FullGameException when there are already the max number of players connected
     * @throws UnavailableNicknameException when the provided nickname has already been taken
     * @throws NotExistingPlayerException when no registered player matches the provided nickname
     */
    public void restorePlayer(String nickname, VirtualView virtualView) throws FullGameException, UnavailableNicknameException, NotExistingPlayerException {
        if(nickname == null)
            throw new RuntimeException("Player's nickname received is null!");
        if(virtualView == null)
            throw new RuntimeException("VirtualView received is null!");
        gameController.restorePlayer(nickname, virtualView);
    }

    /**
     * Method called by the clientHandler when a player disconnects/loses connection
     * @param nickname the player's nickname
     */
    synchronized public void playerDisconnected(String nickname) {
        if(nickname == null)
            throw new RuntimeException("Nickname received is nulL!");
        getGameController().playerDisconnected(nickname);
    }

    /**
     * Utility method to get the list of nicknames of the connected players.
     * @return an {@link ArrayList} of nicknames
     */
    public ArrayList<String> getNicknames(){
        return gameController.getNickNames();
    }

    /**
     * The method calls the proper messageTypeCheck.
     * Every messageTypeCheck checks if there are any null arguments, if the gamePhase is compatible with the
     * message received, if islandIdx is an acceptable number and so on.
     * If the message is valid, the message is forwarded to the GameController.
     *
     * @param message is the message that InputController has to check.
     */
    private void verifyMessage(Message message) {

        gamePhase = getGameController().getGamePhase();

        switch (message.getType()) {
            case R_MOVE -> moveCheck(message);
            case R_CLOUD -> cloudCheck(message);
            case R_ASSISTANT -> assistantCheck(message);
            case R_MOTHERNATURE -> motherNatureCheck(message);
            case R_CHARACTER -> characterCheck(message);
            case R_MONKPRINCESS -> monkPrincessCheck(message);
            case R_JESTERBARD -> jesterBardCheck(message);
            case R_GRANDMAHERBHERALD -> grandmaherbHeraldCheck(message);
            case R_ROGUEMUSHROOMPICKER -> rogueMushroomPickerCheck(message);
            case R_NICKNAME -> nicknameCheck(message);
            default -> new RuntimeException("This messageType doesn't exist!").printStackTrace();
        }
    }


    /**
     * Utility method just to avoid coding repetitions
     * @param message will be elaborated or asked again
     * @param isValid indicates if the message has passed every check.
     */
    private void validateMessage(boolean isValid, Message message) {
        if(isValid)
            getGameController().elaborateMessage(message);
        else
            getGameController().askAgain();
    }

    /**
     * Check if the {@link RMessageMove} has acceptable field.
     */
    private void moveCheck(Message message) {
        int destination = ((RMessageMove)message).destination;
        boolean isValid = true;

        if(gamePhase != Phase.MOVE_STUDENTS)
            new RuntimeException("Message incompatible with actual game phase!").printStackTrace();

        if(((RMessageMove)message).chosenColor == null)
            isValid = false;

        if(destination < 0 || destination > 12)
            isValid = false;

        validateMessage(isValid, message);
    }

    /**
     * Check if the {@link RMessageCloud} has acceptable field.
     */
    private void cloudCheck(Message message) {
        boolean isValid = true;
        int cloud = ((RMessageCloud)message).cloudIndex;

        if(gamePhase != Phase.CHOOSE_CLOUD)
            new RuntimeException("Message incompatible with actual game phase!").printStackTrace();

        if(cloud > 3 || cloud < 1)
            isValid = false;

        validateMessage(isValid, message);
    }

    /**
     * Check if the {@link RMessageAssistant} has acceptable field.
     */
    private void assistantCheck(Message message) {
        boolean isValid = true;

        if(gamePhase != Phase.CHOOSE_ASSISTANT_CARD)
            new RuntimeException("Message incompatible with actual game phase!").printStackTrace();

        if(((RMessageAssistant)message).playedAssistant == null)
            isValid = false;

        validateMessage(isValid, message);
    }

    /**
     * Check if the {@link RMessageMotherNature} has acceptable field.
     */
    private void motherNatureCheck(Message message) {
        int islandIdx = ((RMessageMotherNature)message).islandIndex;
        boolean isValid = true;

        if(gamePhase != Phase.MOVE_MOTHERNATURE)
            new RuntimeException("Message incompatible with actual game phase!").printStackTrace();

        if(islandIdx < 1 || islandIdx > 12)
            isValid = false;

        validateMessage(isValid, message);
    }

    /**
     * Check if the {@link RMessageCharacter} has acceptable field.
     */
    private void characterCheck(Message message) {
        boolean isValid = ((RMessageCharacter)message).getCharacter() != null;

        if((gamePhase != Phase.CHOOSE_CHARACTER_CARD_1) && (gamePhase != Phase.CHOOSE_CHARACTER_CARD_2) && (gamePhase != Phase.CHOOSE_CHARACTER_CARD_3))
            new RuntimeException("Message incompatible with actual game phase!").printStackTrace();

        validateMessage(isValid, message);
    }

    /**
     * Check if the {@link RMessageMonkPrincess} has acceptable field.
     */
    private void monkPrincessCheck(Message message) {
        boolean isValid = true;
        RMessageMonkPrincess mess = (RMessageMonkPrincess) message;
        Color chosenColor = mess.chosenColor;
        Characters characterName = mess.characterName;
        int islandIndex = mess.islandIndex;

        if((gamePhase != Phase.CHOOSE_CHARACTER_CARD_1) && (gamePhase != Phase.CHOOSE_CHARACTER_CARD_2) && (gamePhase != Phase.CHOOSE_CHARACTER_CARD_3))
            new RuntimeException("Message incompatible with actual game phase!").printStackTrace();

        if(characterName != Characters.MONK && characterName != Characters.SPOILED_PRINCESS)
            isValid = false;

        if(chosenColor == null)
            isValid = false;

        if(characterName == Characters.MONK && (islandIndex < 1 || islandIndex > 12))
            isValid = false;

        validateMessage(isValid, message);
    }

    /**
     * Check if the {@link RMessageJesterBard} has acceptable field.
     */
    private void jesterBardCheck(Message message) {
        boolean isValid = true;
        RMessageJesterBard mess = (RMessageJesterBard) message;

        if((gamePhase != Phase.CHOOSE_CHARACTER_CARD_1) && (gamePhase != Phase.CHOOSE_CHARACTER_CARD_2) && (gamePhase != Phase.CHOOSE_CHARACTER_CARD_3))
            new RuntimeException("Message incompatible with actual game phase!").printStackTrace();

        if(mess.characterName == null)
            isValid = false;

        if(mess.characterName != Characters.JESTER && mess.characterName != Characters.BARD)
            isValid = false;

        validateMessage(isValid, message);
    }

    /**
     * Check if the {@link RMessageGrandmaherbHerald} has acceptable field.
     */
    private void grandmaherbHeraldCheck(Message message) {
        boolean isValid = true;
        RMessageGrandmaherbHerald mess = (RMessageGrandmaherbHerald) message;
        int islandIndex = mess.islandIndex;
        Characters characterName = mess.characterName;

        if((gamePhase != Phase.CHOOSE_CHARACTER_CARD_1) && (gamePhase != Phase.CHOOSE_CHARACTER_CARD_2) && (gamePhase != Phase.CHOOSE_CHARACTER_CARD_3))
            new RuntimeException("Message incompatible with actual game phase!").printStackTrace();

        if(characterName == null)
            isValid = false;

        if(characterName != Characters.GRANDMA_HERB && characterName != Characters.HERALD)
            isValid = false;

        if(islandIndex < 1 || islandIndex > 12)
            isValid = false;

        validateMessage(isValid, message);
    }

    /**
     * Check if the {@link RMessageRogueMushroomPicker} has acceptable field.
     */
    private void rogueMushroomPickerCheck(Message message) {
        boolean isValid = true;
        RMessageRogueMushroomPicker mess = (RMessageRogueMushroomPicker) message;

        if((gamePhase != Phase.CHOOSE_CHARACTER_CARD_1) && (gamePhase != Phase.CHOOSE_CHARACTER_CARD_2) && (gamePhase != Phase.CHOOSE_CHARACTER_CARD_3))
            new RuntimeException("Message incompatible with actual game phase!").printStackTrace();

        if(mess.chosenColor == null || mess.characterName == null)
            isValid = false;

        if(mess.characterName != Characters.ROGUE && mess.characterName != Characters.MUSHROOM_PICKER)
            isValid = false;

        validateMessage(isValid, message);
    }

    /**
     * Check if the {@link RMessageNickname} has acceptable field.
     */
    private void nicknameCheck(Message message) {
        boolean isValid = true;
        RMessageNickname mess = (RMessageNickname) message;

        if(mess.nickname == null)
            isValid = false;
        else if(mess.nickname.length() == 0)
            isValid = false;

        validateMessage(isValid, message);
    }

}
