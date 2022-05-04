package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.character_controllers.CharacterController;
import it.polimi.ingsw.controller.character_controllers.CharacterFactory;
import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.enumerations.AssistantCard;
import it.polimi.ingsw.model.enumerations.Characters;
import it.polimi.ingsw.model.enumerations.Phase;
import it.polimi.ingsw.network.messages.*;
import it.polimi.ingsw.view.VirtualView;

import java.security.InvalidParameterException;
import java.util.*;

/**
 * This class represents the Game Controller, which is the one that transforms a user message into an action in the Game
 * @author Alessandro F.
 * @version 1.0
 */

public class GameController {


    private Game game;
    public Phase gamePhase;
    private CharacterController characterController;
    private final Map<String, VirtualView > playersView = new HashMap<>();
    private final boolean isExpert;
    private boolean isLastRound;
    private final int numOfPlayers;
    private int numOfMoves = 0;
    public boolean hasPlayedCharacter;
    private final ArrayList<String> nickNames = new ArrayList<>();

    /**
     * Game Controller constructor
     * @param numOfPlayers Num of players in the game
     * @param isExpert Flag to know if it's an expert game
     */
    public GameController(int numOfPlayers, boolean isExpert){

        this.isLastRound = false;
        this.numOfPlayers = numOfPlayers;
        this.isExpert = isExpert;
        this.hasPlayedCharacter = false;

    }

    /**
     * @return The current Phase of the Game
     */
    public Phase getGamePhase(){return gamePhase;}

    /**
     * @param nickName Name of the player who's virtual view is requested
     * @return The Player's virtual view
     */
    public VirtualView getVirtualView(String nickName){
        return playersView.get(nickName);
    }

    public Game getGame() {
        return game;
    }

    public ArrayList<String> getNickNames() {
        return nickNames;
    }

    /**
     * This method adds players in the game and starts it if the chosen number of players is reached
     * @param nickName Nickname of the player to add
     * @param playerView Virtual View for the player
     * @throws FullGameException Thrown if there's an attempt to add players in an already full game
     */
    public void addPlayer(String nickName, VirtualView playerView) throws FullGameException {

        if (playersView.size() < numOfPlayers) {

            nickNames.add(nickName);  // in order of arrival

            playersView.put(nickName, playerView);

            if (playersView.size() == numOfPlayers) {
                game = new Game(nickNames);
                gamePhase = Phase.CHOOSE_ASSISTANT_CARD;
                if(isExpert){
                    ArrayList<Characters> characters = new ArrayList<>(List.of(Characters.values()));
                    for(int i=0;i<3;i++){
                        Collections.shuffle(characters);
                        game.getGameBoard().addCharacterCard(characters.get(0));
                        characters.remove(0);
                    }
                }

                for(VirtualView v : playersView.values()){
                    game.addObserver(v);
                }
                game.notifyObservers();
                broadcastMessage(game.getCurrentPlayer().getNickName(), MessageType.S_PLAYER);
                getVirtualView(game.getCurrentPlayer().getNickName()).showAssistantChoice(new SMessageShowDeck(game.getPlayerDeck()));
            }

        }
        else throw new FullGameException();
    }

    /**
     * This method elaborates messages based on their type
     * @param m Received message
     */
    public void elaborateMessage(Message m){

        switch(m.getType()){

            case R_ASSISTANT:
                elaborateAssistant(m);
                break;

            case R_CHARACTER:
                elaborateCharacter(m);
                break;

            case R_MOVE:
                elaborateMove(m);
                break;

            case R_MOTHERNATURE:
                elaborateMotherNature(m);
                break;

            case R_CLOUD:
                elaborateCloud(m);
                break;

            default:
                 new UnsupportedOperationException().printStackTrace();

        }
    }

    /**
     * Method called when the message received has null/unacceptable data.
     */
    public void askAgain() {
        getVirtualView(game.getCurrentPlayer().getNickName()).askAgain();
    }

    /**
     * Method called when one of the player loses the connection.
     * @param nickname is the nickname of the player disconnected
     */
    public void playerDisconnected(String nickname) {
        //TODO resilienza alle disconnessioni?
    }

    /**
     * Utility method to send the same message to each player's virtual view
     * @param message The message that needs to be broadcasted
     */
    public void broadcastMessage(String message, MessageType type) {
        switch (type) {
            case S_INVALID: {
                SMessageInvalid m = new SMessageInvalid(message);
                for (VirtualView v : playersView.values())
                    v.showGenericMessage(m);
                break;
            }
            case S_PLAYER: {
                SMessageCurrentPlayer m = new SMessageCurrentPlayer(message);
                for (VirtualView v : playersView.values())
                    v.showCurrentPlayer(m);
                break;
            }
        }
    }


    /**
     * Sends a message to the specified user
     * @param nickName User that receives the message
     * @param message Body of the message to send
     */

    public void sendErrorMessage(String nickName, String message){
        getVirtualView(nickName).showGenericMessage(new SMessageInvalid(message));
    }

    public SMessageCharacter createCharacterMessage(){
        return new SMessageCharacter(game.getGameBoard().getCharacters());
    }


    /**
     * Utility method to get the Entrance of the current player
     * @return The Entrance of the current player
     * @throws NoCurrentPlayerException Thrown if it's nobody's turn
     */
    public Entrance getEntrance() throws NoCurrentPlayerException {

        return game.getGameBoard().getPlayerBoard(game.getCurrentPlayer()).getEntrance();

    }

    /**
     * Utility method to get the Dining Room of the current player
     * @return The Dining Room of the current player
     * @throws NoCurrentPlayerException Thrown if it's nobody's turn
     */
    public DiningRoom getDiningRoom() throws NoCurrentPlayerException {

        return game.getGameBoard().getPlayerBoard(game.getCurrentPlayer()).getDiningRoom();

    }

    /**
     * Utility method to check who is the winner
     */
    public void checkWin(){
        String winner = game.checkWinner();

        if(winner.equals("Tie")){
            for (String nickName : nickNames)
                playersView.get(nickName).showWinMessage(new SMessageWin("It's a tie"));
        }
        else {
            for (String nickName : nickNames)
                playersView.get(nickName).showWinMessage(new SMessageWin(winner + "has won!"));
        }

    }

    /**
     * Utility method to prepare the controller for a new round
     */
    private void setupNewRound(){
        game.sortPlayersAssistantTurn();
        broadcastMessage("A new Round is starting!", MessageType.S_INVALID);
        hasPlayedCharacter = false;
        try {
            game.fillClouds();
        }catch(EmptyBagException exc){
            isLastRound = true;
            broadcastMessage("The bag is empty, this is the last round!", MessageType.S_INVALID);
        }
    }

    /**
     * Switches the Phase of the Game after a Character Card is used (or after no Character Card is chosen)
     */

    public void switchPhase(){
        if(gamePhase.equals(Phase.CHOOSE_CHARACTER_CARD_1))
            gamePhase = Phase.MOVE_STUDENTS;

        else if(gamePhase.equals(Phase.CHOOSE_CHARACTER_CARD_2))
            gamePhase = Phase.MOVE_MOTHERNATURE;

        else gamePhase = Phase.CHOOSE_CLOUD;
    }

    /**
     * @param characterName Name of the character needed
     * @return The CharacterCard with the specified name
     */
    public CharacterCard getCharacterByName(Characters characterName){
        return game.getCharacterByName(characterName);
    }



    /**
     * Maps a message to an action: plays the Assistant card that the player chose.
     * @param message The message containing the chosen Assistant card
     */
    private void elaborateAssistant(Message message){  //The message has already been filtered by the InputController (sender is the current player and
                                                       //the Game Phase is correct)

        RMessageAssistant assistantMessage = (RMessageAssistant) message;
        System.out.println("PLAYED CARD: " + assistantMessage.getPlayedAssistant());
        try {
            game.playAssistantCard(assistantMessage.getPlayedAssistant());
            broadcastMessage(game.getCurrentPlayer().getNickName(), MessageType.S_PLAYER);
            getVirtualView(game.getCurrentPlayer().getNickName()).showAssistantChoice(new SMessageShowDeck(game.getPlayerDeck()));


        }catch(NoCurrentPlayerException e){  //should only be thrown if a bug happens...
            e.printStackTrace();

            game.getPlayers().get(0).setPlayerTurn(true);  //if it happens assign the turn to a random player

            broadcastMessage(e.getMessage() + game.getPlayers().get(0).getNickName() + "is the first player now", MessageType.S_INVALID);

        }
        catch(CardNotAvailableException | CardNotFoundException e){
            sendErrorMessage(assistantMessage.getNickName(), e.getMessage());
            getVirtualView(assistantMessage.getNickName()).showAssistantChoice(new SMessageShowDeck(game.getPlayerDeck())); //Re-Ask the player to pick a card
        }
        catch(EmptyDeckException e){
            if(!isLastRound){
                broadcastMessage(e.getMessage(), MessageType.S_INVALID);
                isLastRound = true;
            }
            broadcastMessage(game.getCurrentPlayer().getNickName(), MessageType.S_PLAYER);
            getVirtualView(game.getCurrentPlayer().getNickName()).showAssistantChoice(new SMessageShowDeck(game.getPlayerDeck()));

        }
        catch(EndRoundException e){
            game.sortPlayersActionTurn();
            for(AssistantCard assistant : AssistantCard.values())
                assistant.resetPlayed();  //Reset the already played assistants
            broadcastMessage(game.getCurrentPlayer().getNickName(), MessageType.S_PLAYER);
            if(isExpert) {
                gamePhase = Phase.CHOOSE_CHARACTER_CARD_1;
                getVirtualView(game.getCurrentPlayer().getNickName()).showCharacterChoice(createCharacterMessage());

            }

            else {
                gamePhase = Phase.MOVE_STUDENTS;
                getVirtualView(game.getCurrentPlayer().getNickName()).askMove();
            }
        }
    }


    /**
     * Maps a message to an action: moves a piece from the Entrance of a player to an Island or the player's Dining Room
     * @param message The message containing the color of the piece that is being moved and the chosen destination (Island or DiningRoom)
     */
    private void elaborateMove(Message message){

        RMessageMove moveMessage = (RMessageMove) message;
        TileWithStudents dest;
        if (moveMessage.getDestination() == 0)  //Destination is the dining room
            dest = getDiningRoom();

        else if (moveMessage.getDestination() > 0 && moveMessage.getDestination() < game.getGameBoard().getIslands().size() + 1)//Destination is an island
            dest = game.getGameBoard().getIslands().get(moveMessage.getDestination() - 1); //User counts islands from 1, not from 0

        else { //Destination not valid
            sendErrorMessage(moveMessage.getNickName(),"This destination does not exist!");
            getVirtualView(game.getCurrentPlayer().getNickName()).askMove();
            return;
        }

        try{
            game.move(moveMessage.getChosenColor(), getEntrance(), dest);

        } catch (FullDestinationException e) { //Destination already full, need to pick another one (don't increment numOfMoves)

            sendErrorMessage(moveMessage.getNickName(), e.getMessage() + "Please pick another one");
            getVirtualView(game.getCurrentPlayer().getNickName()).askMove();
            return;

        }
        catch(InvalidParameterException e){
            getVirtualView(game.getCurrentPlayer().getNickName()).showGenericMessage(new SMessageInvalid("No student of such color"));
            askAgain();
            return;
        }
        numOfMoves += 1;

        if(numOfMoves == numOfPlayers + 1){ //Already moved 3 (or 4) pieces

            numOfMoves = 0;
            if(isExpert && !hasPlayedCharacter){
                gamePhase = Phase.CHOOSE_CHARACTER_CARD_2;
                getVirtualView(game.getCurrentPlayer().getNickName()).showCharacterChoice(createCharacterMessage());
            }
            else{
                gamePhase = Phase.MOVE_MOTHERNATURE;
                getVirtualView(game.getCurrentPlayer().getNickName()).askMotherNatureMove();
            }
        }

        else getVirtualView(game.getCurrentPlayer().getNickName()).askMove();

    }


    /**
     * Maps a message to an action: Moves mother nature on the selected island
     * @param message The message containing the index of the island selected by the User
     */
    public void elaborateMotherNature(Message message){

        RMessageMotherNature motherNatureMessage = (RMessageMotherNature) message;
        int lastIslandIndex = game.getGameBoard().getIslands().size()-1;
        int desiredIslandIndex = motherNatureMessage.getIslandIndex()-1;
        int currentMotherNatureIndex = game.getGameBoard().getIslands().indexOf(game.getGameBoard().getMotherNature().getCurrentIsland());
        int playerMaxSteps;
        int numOfSteps;

        if(isExpert && characterController.characterName.equals(Characters.MAGIC_MAILMAN))
                playerMaxSteps = game.getCurrentPlayer().getPlayedCard().getMotherNatureMovement() + 2;

        else playerMaxSteps = game.getCurrentPlayer().getPlayedCard().getMotherNatureMovement();

        if(lastIslandIndex < desiredIslandIndex){
            sendErrorMessage(motherNatureMessage.getNickName(), "Invalid Island! Please select a number between 1 and " + (lastIslandIndex + 1));
            getVirtualView(game.getCurrentPlayer().getNickName()).askMotherNatureMove();
            return;
        }

        if(currentMotherNatureIndex < desiredIslandIndex)
            numOfSteps = desiredIslandIndex - currentMotherNatureIndex;


        else numOfSteps = lastIslandIndex - currentMotherNatureIndex + desiredIslandIndex + 1;


        if(playerMaxSteps < numOfSteps) {
            sendErrorMessage(motherNatureMessage.getNickName(), "Insufficient number of steps!");
            getVirtualView(game.getCurrentPlayer().getNickName()).askMotherNatureMove();
        }
        else {
            try {
                System.out.println("CIao");
                game.moveMotherNature(numOfSteps);

            } catch (TowerWinException e) {

                for (String nickName : nickNames)
                   getVirtualView(nickName).showWinMessage(new SMessageWin(e.getMessage()));
                return;
                //TODO CHIUDERE TUTTO OOOOOOOOOOOOOOO HANNO VINTO

            }
            catch (NumOfIslandsException e) {

                checkWin();
                //TODO CHIUDERE TUTTO OOOOOOOOOOOOOOO HANNO VINTO
                return;

            }


            if(isExpert && !hasPlayedCharacter) {
                gamePhase = Phase.CHOOSE_CHARACTER_CARD_3;
                getVirtualView(game.getCurrentPlayer().getNickName()).showCharacterChoice(createCharacterMessage());
            }

            else {
                gamePhase = Phase.CHOOSE_CLOUD;
                getVirtualView(game.getCurrentPlayer().getNickName()).askCloud();
            }


        }

    }

    /**
     * Maps a message to an action: Puts the students from the selected Cloud Tile to the User's entrance
     * @param message Message containing the index of the chosen Cloud Tile
     */

    public void elaborateCloud (Message message){

        RMessageCloud cloudMessage = (RMessageCloud) message;
        try {
            characterController = new CharacterController(this, Characters.NONE); //Reset character controller
            hasPlayedCharacter = false; //Next player can play a character
            for(CharacterCard c : game.getGameBoard().getCharacters()) //Reset every character card
                c.setActive(false);

            game.chooseCloud(game.getGameBoard().getClouds().get(cloudMessage.getCloudIndex() - 1));
            broadcastMessage(game.getCurrentPlayer().getNickName(), MessageType.S_PLAYER);


            if(isExpert){
                gamePhase = Phase.CHOOSE_CHARACTER_CARD_1;
                getVirtualView(game.getCurrentPlayer().getNickName()).showCharacterChoice(createCharacterMessage());
            }
            else {
                gamePhase = Phase.MOVE_STUDENTS;
                getVirtualView(game.getCurrentPlayer().getNickName()).askMove(); //Asks move to next player (chooseCloud method
                                                                                 //manages the turn)
            }

        } catch(CloudNotFullException e){
            sendErrorMessage(cloudMessage.getNickName(), "This Cloud is Empty, please select another Cloud");
            getVirtualView(game.getCurrentPlayer().getNickName()).askCloud();

        }
        catch(FullDestinationException e){
            sendErrorMessage(cloudMessage.getNickName(), "Your Entrance is full, you can't choose a Cloud");
            getVirtualView(game.getCurrentPlayer().getNickName()).askCloud();
        }
        catch(EndRoundException e){

            if(isLastRound) {
                checkWin();
            }
            else{
                setupNewRound();
                broadcastMessage(game.getCurrentPlayer().getNickName(), MessageType.S_PLAYER);
                gamePhase = Phase.CHOOSE_ASSISTANT_CARD;
                game.notifyObservers(); //Notifies the view in case the round is over
                getVirtualView(game.getCurrentPlayer().getNickName()).showAssistantChoice(new SMessageShowDeck(game.getPlayerDeck()));
            }
        }

    }

    /**
     * Elaborates the Character Card chosen by the player (if one is chosen), instantiate the Controller responsible for managing Character Cards
     * and asks the parameters required by the card to the Player
     * @param message The message containing the chosen Character Card
     */
    private void elaborateCharacter(Message message) {

        RMessageCharacter characterMessage = (RMessageCharacter) message;

        characterController = CharacterFactory.create(this, characterMessage.character);
        characterController.use(characterMessage.nickname);

        switch(gamePhase){ //If the card sent was 'NONE' the phase has been changed and this switch activates, if there was a valid Card the phase hasn't been changed and this switch does Default
            case MOVE_STUDENTS: {
                getVirtualView(getGame().getCurrentPlayer().getNickName()).askMove();
                break;
            }
            case MOVE_MOTHERNATURE:{
                getVirtualView(getGame().getCurrentPlayer().getNickName()).askMotherNatureMove();
                break;
            }
            case CHOOSE_CLOUD:{
                getVirtualView(getGame().getCurrentPlayer().getNickName()).askCloud();
                break;
            }

            default:
        }



    }

    /**
     * Activates the effect of the chosen Character Card
     * @param message The message containing the parameters needed for the card activation (if any)
     */
    public void elaborateActivation(Message message){
        characterController.activate(message);
        switch(gamePhase){
            case MOVE_STUDENTS: {
                getVirtualView(getGame().getCurrentPlayer().getNickName()).askMove();
                break;
            }
            case MOVE_MOTHERNATURE:{
                getVirtualView(getGame().getCurrentPlayer().getNickName()).askMotherNatureMove();
                break;
            }
            case CHOOSE_CLOUD:{
                getVirtualView(getGame().getCurrentPlayer().getNickName()).askCloud();
                break;
            }
            case WINNER:{
                //TODO CHIUDERE TUTTO, C'E' UN VINCITORE ED E' GIA' STATO NOTIFICATO
                break;
            }
            default:
        }
    }

}
