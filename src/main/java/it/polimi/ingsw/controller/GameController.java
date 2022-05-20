package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.character_controllers.CharacterController;
import it.polimi.ingsw.controller.character_controllers.CharacterFactory;
import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.enumerations.AssistantCard;
import it.polimi.ingsw.model.enumerations.Characters;
import it.polimi.ingsw.model.enumerations.Phase;
import it.polimi.ingsw.network.ClientHandler;
import it.polimi.ingsw.network.messages.*;
import it.polimi.ingsw.view.VirtualView;

import java.io.Serial;
import java.io.Serializable;
import java.security.InvalidParameterException;
import java.util.*;

/**
 * This class represents the Game Controller, which is the one that transforms a user message into an action in the Game
 * @author Alessandro F.
 * @version 1.0
 */

public class GameController implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    private Game game;
    public Phase gamePhase;
    private CharacterController characterController;
    /** Links each player nickname to the corresponding {@link VirtualView} */
    public transient Map<String, VirtualView > playersView;
    private final boolean isExpert;
    private boolean isLastRound;
    private final int numOfPlayers;
    private int numOfMoves = 0;
    /** True if someone has played a character card, false otherwise.*/
    public boolean hasPlayedCharacter;
    private final ArrayList<String> nickNames = new ArrayList<>();

    /**
     * Game Controller constructor.
     * @param numOfPlayers Num of players in the game
     * @param isExpert Flag to know if it's an expert game
     */
    public GameController(int numOfPlayers, boolean isExpert){
        this.isLastRound = false;
        this.numOfPlayers = numOfPlayers;
        this.isExpert = isExpert;
        this.hasPlayedCharacter = false;
        this.playersView = new HashMap<>();
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

    /**
     * Utility method to get the {@link Game}
     * @return the linked game
     */
    public Game getGame() {
        return game;
    }

    /**
     * Utility method to get the list of nicknames of the connected players.
     * @return an {@link ArrayList} of nicknames
     */
    public ArrayList<String> getNickNames() {
        return nickNames;
    }

    /**
     * Restore player. Matches current player to previously saved player.
     * @param nickName of the player to add
     * @param playerView Virtual View for the player
     * @throws FullGameException
     */
    public void restorePlayer(String nickName, VirtualView playerView) throws FullGameException, NotExistingPlayerException{
        if(playersView.size() < numOfPlayers){
            if(getNickNames().contains(nickName)){
                playersView.put(nickName, playerView);
            } else {
                throw new NotExistingPlayerException();
            }

            playerView.setExpert(new SMessageExpert(isExpert));

            if (playersView.size() == numOfPlayers) {
                for(VirtualView v : playersView.values()){
                    game.addObserver(v);
                }
                game.notifyObservers();
                if(!ClientHandler.disconnectionResilient) {
                    restartFromPhase();
                }
            } else {
                for(VirtualView v : playersView.values()){
                    v.showLobby(new SMessageLobby(new ArrayList<>(playersView.keySet()), numOfPlayers));
                }
            }

        } else {
            throw new FullGameException();
        }
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

            playerView.setExpert(new SMessageExpert(isExpert));

            if (playersView.size() == numOfPlayers) {
                game = new Game(nickNames);
                gamePhase = Phase.CHOOSE_ASSISTANT_CARD;
                DiskManager.saveGame(this);
                if(isExpert){
                    ArrayList<Characters> characters = new ArrayList<>(List.of(Characters.values()));
                    characters.remove(Characters.NONE);
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
            } else {
                for(VirtualView v : playersView.values()){
                    v.showLobby(new SMessageLobby(new ArrayList<>(playersView.keySet()), numOfPlayers));
                }
            }

        }
        else throw new FullGameException();
    }

    /**
     * This method elaborates messages based on their type
     * @param m Received message
     */
    public void elaborateMessage(Message m){

        switch (m.getType()) {
            case R_ASSISTANT -> elaborateAssistant(m);
            case R_CHARACTER -> elaborateCharacter(m);
            case R_MOVE -> elaborateMove(m);
            case R_MOTHERNATURE -> elaborateMotherNature(m);
            case R_CLOUD -> elaborateCloud(m);
            case R_GRANDMAHERBHERALD, R_JESTERBARD, R_ROGUEMUSHROOMPICKER, R_MONKPRINCESS -> elaborateActivation(m);
            default -> new UnsupportedOperationException().printStackTrace();
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
        playersView.remove(nickname);

        if(playersView.size() == 1) {
            //End game
            for (VirtualView v : playersView.values()) {
                v.showWinMessage(new SMessageWin("You won"));
                v.showDisconnectionMessage();
            }
        }else{
            //Set-up for reconnection
            ClientHandler.disconnectionResilient = true;


            //Continue without the player that was disconnected
            Optional<Player> disconnectedPlayer = game.getPlayers().stream().filter(x -> x.getNickName().equals(nickname)).findFirst();
            disconnectedPlayer.ifPresent(x -> x.setConnected(false));
            disconnectedPlayer.ifPresent(x->{

                broadcastMessage(nickname + " was disconnected, the game will continue without them until they reconnect",MessageType.S_INVALID);

                if(game.getCurrentPlayer().equals(x)){
                    try {
                        game.endPlayerTurn(x);

                        restartFromPhase();

                    } catch (EndRoundException e) {
                        if(gamePhase.equals(Phase.CHOOSE_ASSISTANT_CARD)){
                            game.sortPlayersActionTurn();
                            for(AssistantCard assistant : AssistantCard.values())
                                assistant.resetPlayed();  //Reset the already played assistants
                            askMoveOrCharacter();
                        }else{
                            //Phase is Choose cloud
                            if(isLastRound) {
                                checkWin();
                            }
                            else{
                                setupNewRound();
                            }
                        }
                    }
                }

            });

        }

    }

    /**
     * Prompts next player to perform an action (depending on the phase) after a load of saved game or disconnection of
     * current player.
     */
    private void restartFromPhase() {
        broadcastMessage(game.getCurrentPlayer().getNickName(), MessageType.S_PLAYER);
        switch (gamePhase) {
            case CHOOSE_ASSISTANT_CARD ->
                    getVirtualView(game.getCurrentPlayer().getNickName()).showAssistantChoice(new SMessageShowDeck(game.getPlayerDeck()));
            case CHOOSE_CHARACTER_CARD_1, CHOOSE_CHARACTER_CARD_2, CHOOSE_CHARACTER_CARD_3 ->
                    getVirtualView(game.getCurrentPlayer().getNickName()).showCharacterChoice(createCharacterMessage());
            case MOVE_STUDENTS ->
                    getVirtualView(game.getCurrentPlayer().getNickName()).askMove();
            case MOVE_MOTHERNATURE ->
                    getVirtualView(game.getCurrentPlayer().getNickName()).askMotherNatureMove(new SMessageMotherNature(game.getCurrentPlayer().getPlayedCard().getMotherNatureMovement()));
            case CHOOSE_CLOUD ->
                    getVirtualView(getGame().getCurrentPlayer().getNickName()).askCloud();
            default ->
                    throw new RuntimeException("Error while restoring the old game or disconnecting the current player");
        }
    }

    /**
     * Utility method to send the same message to each player's virtual view
     * @param message The message that needs to be broadcast
     */
    public void broadcastMessage(String message, MessageType type) {
        switch (type) {
            case S_INVALID -> {
                SMessageInvalid m = new SMessageInvalid(message);
                for (VirtualView v : playersView.values())
                    v.showGenericMessage(m);
            }
            case S_PLAYER -> {
                SMessageCurrentPlayer m = new SMessageCurrentPlayer(message);
                for (VirtualView v : playersView.values())
                    v.showCurrentPlayer(m);
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

    /**
     * Creates a message with available {@link Characters}
     * @return the created message
     */
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

        for(Player p : game.getPlayers()){
            if(playersView.containsKey(p.getNickName())) {
                p.setConnected(true);
            }
        }

        game.sortPlayersAssistantTurn();
        broadcastMessage("A new Round is starting!", MessageType.S_INVALID);
        hasPlayedCharacter = false;
        try {
            game.fillClouds();
        }catch(EmptyBagException exc){
            isLastRound = true;
            broadcastMessage("The bag is empty, this is the last round!", MessageType.S_INVALID);
        }
        gamePhase = Phase.CHOOSE_ASSISTANT_CARD;
        game.notifyObservers(); //Notifies the view in case the round is over
        broadcastMessage(game.getCurrentPlayer().getNickName(), MessageType.S_PLAYER);
        DiskManager.saveGame(this);
        getVirtualView(game.getCurrentPlayer().getNickName()).showAssistantChoice(new SMessageShowDeck(game.getPlayerDeck()));
    }

    /**
     * Switches the Phase of the Game after a Character Card is used (or after no Character Card is chosen)
     */
    public void switchPhase(){
        if(gamePhase.equals(Phase.CHOOSE_CHARACTER_CARD_1)) {
            gamePhase = Phase.MOVE_STUDENTS;
            DiskManager.saveGame(this);
        }


        else if(gamePhase.equals(Phase.CHOOSE_CHARACTER_CARD_2)) {
            gamePhase = Phase.MOVE_MOTHERNATURE;
            DiskManager.saveGame(this);
        }

        else {
            gamePhase = Phase.CHOOSE_CLOUD;
            DiskManager.saveGame(this);
        }
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
        System.out.println("PLAYED CARD: " + assistantMessage.playedAssistant);
        try {
            game.playAssistantCard(assistantMessage.playedAssistant);
            broadcastMessage(game.getCurrentPlayer().getNickName(), MessageType.S_PLAYER);
            getVirtualView(game.getCurrentPlayer().getNickName()).showAssistantChoice(new SMessageShowDeck(game.getPlayerDeck()));


        }catch(NoCurrentPlayerException e){  //should only be thrown if a bug happens...
            e.printStackTrace();

            game.getPlayers().get(0).setPlayerTurn(true);  //if it happens assign the turn to a random player

            broadcastMessage(e.getMessage() + game.getPlayers().get(0).getNickName() + "is the first player now", MessageType.S_INVALID);

        }
        catch(CardNotAvailableException | CardNotFoundException e){
            sendErrorMessage(assistantMessage.nickname, e.getMessage());
            getVirtualView(assistantMessage.nickname).showAssistantChoice(new SMessageShowDeck(game.getPlayerDeck())); //Re-Ask the player to pick a card
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
            askMoveOrCharacter();
        }
    }

    /**
     * If game is expert this method commands the current player to move students, otherwise it presents them a character card choice.
     */
    private void askMoveOrCharacter() {
        broadcastMessage(game.getCurrentPlayer().getNickName(), MessageType.S_PLAYER);
        if(isExpert) {
            gamePhase = Phase.CHOOSE_CHARACTER_CARD_1;
            DiskManager.saveGame(this);
            getVirtualView(game.getCurrentPlayer().getNickName()).showCharacterChoice(createCharacterMessage());

        }

        else {
            gamePhase = Phase.MOVE_STUDENTS;
            DiskManager.saveGame(this);
            getVirtualView(game.getCurrentPlayer().getNickName()).askMove();
        }
    }


    /**
     * Maps a message to an action: moves a piece from the Entrance of a player to an Island or the player's Dining Room
     * @param message The message containing the color of the piece that is being moved and the chosen destination (Island or DiningRoom)
     */
    private void elaborateMove(Message message){

        RMessageMove moveMessage = (RMessageMove) message;
        TileWithStudents dest;
        if (moveMessage.destination == 0)  //Destination is the dining room
            dest = getDiningRoom();

        else if (moveMessage.destination > 0 && moveMessage.destination < game.getGameBoard().getIslands().size() + 1)//Destination is an island
            dest = game.getGameBoard().getIslands().get(moveMessage.destination - 1); //User counts islands from 1, not from 0

        else { //Destination not valid
            sendErrorMessage(moveMessage.nickname,"This destination does not exist!");
            getVirtualView(game.getCurrentPlayer().getNickName()).askMove();
            return;
        }

        try{
            game.move(moveMessage.chosenColor, getEntrance(), dest);

        } catch (FullDestinationException e) { //Destination already full, need to pick another one (don't increment numOfMoves)

            sendErrorMessage(moveMessage.nickname, e.getMessage() + "Please pick another one");
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
                DiskManager.saveGame(this);
                getVirtualView(game.getCurrentPlayer().getNickName()).showCharacterChoice(createCharacterMessage());
            }
            else{
                gamePhase = Phase.MOVE_MOTHERNATURE;
                DiskManager.saveGame(this);
                getVirtualView(game.getCurrentPlayer().getNickName()).askMotherNatureMove(new SMessageMotherNature(game.getCurrentPlayer().getPlayedCard().getMotherNatureMovement()));
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
        int desiredIslandIndex = motherNatureMessage.islandIndex-1;
        int currentMotherNatureIndex = game.getGameBoard().getIslands().indexOf(game.getGameBoard().getMotherNature().getCurrentIsland());
        int playerMaxSteps;
        int numOfSteps;

        if(isExpert && characterController.characterName.equals(Characters.MAGIC_MAILMAN))
                playerMaxSteps = game.getCurrentPlayer().getPlayedCard().getMotherNatureMovement() + 2;

        else playerMaxSteps = game.getCurrentPlayer().getPlayedCard().getMotherNatureMovement();

        if(lastIslandIndex < desiredIslandIndex){
            sendErrorMessage(motherNatureMessage.nickname, "Invalid Island! Please select a number between 1 and " + (lastIslandIndex + 1));
            getVirtualView(game.getCurrentPlayer().getNickName()).askMotherNatureMove(new SMessageMotherNature(game.getCurrentPlayer().getPlayedCard().getMotherNatureMovement()));
            return;
        }

        if(currentMotherNatureIndex < desiredIslandIndex)
            numOfSteps = desiredIslandIndex - currentMotherNatureIndex;


        else numOfSteps = lastIslandIndex - currentMotherNatureIndex + desiredIslandIndex + 1;


        if(playerMaxSteps < numOfSteps) {
            sendErrorMessage(motherNatureMessage.nickname, "Insufficient number of steps!");
            getVirtualView(game.getCurrentPlayer().getNickName()).askMotherNatureMove(new SMessageMotherNature(game.getCurrentPlayer().getPlayedCard().getMotherNatureMovement()));
        }
        else {
            try {
                System.out.println("CIao");
                game.moveMotherNature(numOfSteps);

            } catch (TowerWinException e) {

                for (String nickName : nickNames)
                   getVirtualView(nickName).showWinMessage(new SMessageWin(e.getMessage()));
                return;


            }
            catch (NumOfIslandsException e) {

                checkWin();
                return;

            }


            if(isExpert && !hasPlayedCharacter) {
                gamePhase = Phase.CHOOSE_CHARACTER_CARD_3;
                DiskManager.saveGame(this);
                getVirtualView(game.getCurrentPlayer().getNickName()).showCharacterChoice(createCharacterMessage());
            }

            else {
                gamePhase = Phase.CHOOSE_CLOUD;
                DiskManager.saveGame(this);
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

            game.chooseCloud(game.getGameBoard().getClouds().get(cloudMessage.cloudIndex - 1));
            askMoveOrCharacter();

        } catch(CloudNotFullException e){
            sendErrorMessage(cloudMessage.nickname, "This Cloud is Empty, please select another Cloud");
            getVirtualView(game.getCurrentPlayer().getNickName()).askCloud();

        }
        catch(FullDestinationException e){
            sendErrorMessage(cloudMessage.nickname, "Your Entrance is full, you can't choose a Cloud");
            getVirtualView(game.getCurrentPlayer().getNickName()).askCloud();
        }
        catch(EndRoundException e){

            if(isLastRound) {
                checkWin();
            }
            else{
                setupNewRound();
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

        sceneAfterCharacter();

    }

    /**
     * Activates the effect of the chosen Character Card
     * @param message The message containing the parameters needed for the card activation (if any)
     */
    public void elaborateActivation(Message message){
        characterController.activate(message);
        sceneAfterCharacter();
    }

    /**
     * Asks next action after character choice.
     */
    private void sceneAfterCharacter() {
        switch (gamePhase) {
            case MOVE_STUDENTS -> getVirtualView(getGame().getCurrentPlayer().getNickName()).askMove();
            case MOVE_MOTHERNATURE -> getVirtualView(getGame().getCurrentPlayer().getNickName()).askMotherNatureMove(new SMessageMotherNature(game.getCurrentPlayer().getPlayedCard().getMotherNatureMovement()));
            case CHOOSE_CLOUD -> getVirtualView(getGame().getCurrentPlayer().getNickName()).askCloud();
            default -> {}
        }
    }

    /**
     * Return expert-game-flag
     * @return true if the game is an expert game, false otherwise.
     */
    public boolean isExpert(){
        return isExpert;
    }
}
