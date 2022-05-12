package it.polimi.ingsw.view;

import it.polimi.ingsw.model.CloudTile;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.IslandTile;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.enumerations.Characters;
import it.polimi.ingsw.model.enumerations.Color;
import it.polimi.ingsw.model.enumerations.TowerColor;
import it.polimi.ingsw.network.ClientHandler;
import it.polimi.ingsw.network.messages.*;
import it.polimi.ingsw.observers.Observable;
import it.polimi.ingsw.observers.Observer;

import java.util.*;

/**
 * Virtual view to implement the MVC pattern on the server.
 *
 * @author A.G. Gaillet
 * @version 1.0
 */
public class VirtualView implements ViewInterface, Observer {
    private static final long serialVersionUID = 1L;
    ClientHandler clientHandler;

    /**
     * Constructor.
     * @param clientHandler to be linked to this {@link VirtualView}
     */
    public VirtualView(ClientHandler clientHandler){
        this.clientHandler = clientHandler;
    }

    /**
     * Ask the user to provide a valid nickname.
     */
    @Override
    public void askNickName() {
        clientHandler.sendMessage(new SMessage(MessageType.S_NICKNAME));
    }

    /**
     * Ask the user to provide the number of desired players and the desired game mode.
     */
    @Override
    public void askGameSettings() {
        clientHandler.sendMessage(new SMessage(MessageType.S_GAMESETTINGS));
    }

    /**
     * Ask the player to move mother nature.
     */
    @Override
    public void askMotherNatureMove() {
        clientHandler.sendMessage(new SMessage(MessageType.S_MOTHERNATURE));
    }

    /**
     * Ask the player to pick an assistant card from provided available cards.
     *
     * @param messageShowDeck containing available assistants
     */
    @Override
    public void showAssistantChoice(SMessageShowDeck messageShowDeck) {
        clientHandler.sendMessage(messageShowDeck);
    }

    /**
     * Display lobby.
     * @param message containing information on connected and desired players.
     */
    @Override
    public void showLobby(SMessageLobby message) {
        clientHandler.sendMessage(message);
    }

    /**
     * Display a disconnection message.
     */
    @Override
    public void showDisconnectionMessage() {new UnsupportedOperationException().printStackTrace();} //TODO: check?

    /**
     * Display a "someone has won" message.
     * @param message containing information on who won.
     */
    @Override
    public void showWinMessage(SMessageWin message) {
        clientHandler.sendMessage(message);
    }

    /**
     * Shows the game status displaying the board.
     * @param message message containing game status.
     */
    @Override
    public void showBoard(SMessageGameState message) {
        clientHandler.sendMessage(message);
    }

    /**
     * Display a generic text message.
     * @param message containing the {@link String} to be displayed.
     */
    @Override
    public void showGenericMessage(SMessageInvalid message){
        clientHandler.sendMessage(message);
    }

    /**
     * Ask player to pick a character card among provided available options.
     * @param messageCharacter message containing {@link Characters} available
     */
    @Override
    public void showCharacterChoice(SMessageCharacter messageCharacter) {
        clientHandler.sendMessage(messageCharacter);
    }

    /**
     * Ask the player to pick a cloud.
     */
    @Override
    public void askCloud(){
        clientHandler.sendMessage(new SMessage(MessageType.S_CLOUD));
    }

    /**
     * To be used to re-execute the last prompt.
     * In client implementations this method only shows an error message. The adapter is responsible for error handling.
     */
    @Override
    public void askAgain() {
        clientHandler.sendMessage(new SMessage(MessageType.S_TRYAGAIN));
    }

    /**
     * Update current player.
     *
     * @param messageCurrentPlayer
     */
    @Override
    public void showCurrentPlayer(SMessageCurrentPlayer messageCurrentPlayer) {
        clientHandler.sendMessage(messageCurrentPlayer);
    }

    /**
     * Getter method for the nickname of the associated player
     *
     * @return {@link String} nickname of the associated player
     */
    @Override
    public String getNickName() {
        return clientHandler.getPlayerNickname();
    }

    /**
     * This method is deprecated, please use specific character methods instead
     * (e.g. {@link VirtualView#grandmaHerbHeraldScene(SMessageGrandmaherbHerald)}).
     * Ask additional information on chosen character effect when necessary.
     *
     * @param message request message
     */
    @Deprecated
    @Override
    public void askCharacterMove(SMessage message) {
        clientHandler.sendMessage(message);
    }

    /**
     * Asks additional information on chosen character effect of
     * {@link Characters#GRANDMA_HERB} or
     * {@link Characters#HERALD}.
     *
     * @param message request message
     */
    @Override
    public void grandmaHerbHeraldScene(SMessageGrandmaherbHerald message) {
        clientHandler.sendMessage(message);
    }

    /**
     * Asks additional information on chosen character effect of
     * {@link Characters#MONK} or
     * {@link Characters#SPOILED_PRINCESS}.
     *
     * @param message request message
     */
    @Override
    public void monkPrincessScene(SMessageMonkPrincess message) {
        clientHandler.sendMessage(message);
    }

    /**
     * Asks additional information on chosen character effect of
     * {@link Characters#ROGUE} or
     * {@link Characters#MUSHROOM_PICKER}.
     *
     * @param message request message
     */
    @Override
    public void rogueMushroomPickerScene(SMessageRogueMushroomPicker message) {
        clientHandler.sendMessage(message);
    }

    /**
     * Asks additional information on chosen character effect of
     * {@link Characters#JESTER} or
     * {@link Characters#BARD}.
     *
     * @param message request message
     */
    @Override
    public void jesterBardScene(SMessageJesterBard message) {
        clientHandler.sendMessage(message);
    }

    /**
     * Ask the user whether to use the loaded game save or not.
     */
    @Override
    public void askUseSavedGame() {
        new UnsupportedOperationException().printStackTrace();
    }

    /**
     * Ask the player to move a student.
     */
    @Override
    public void askMove(){
        clientHandler.sendMessage(new SMessage(MessageType.S_MOVE));
    }

    
    /**
     * When this method is called by the observed {@link Game} it creates a {@link SMessageGameState} containing the game state
     * and calls the {@link VirtualView#showBoard(SMessageGameState)} method
     * @param observable the observable object
     */
    @Override
    public void notify(Observable observable) {

        Game g = (Game) observable;
        System.out.println(g.toString());

        Map<String, Map<Color, Integer>> studEntrance = new HashMap<>();
        Map<String, Map<Color, Integer>> studDining = new HashMap<>();
        Map<String, Integer> numTowers = new HashMap<>();
        Map<String, TowerColor> towerColor = new HashMap<>();
        Map<Integer, Map<Color, Integer>> studIslands = new HashMap<>();
        Map<Integer, Integer> numTowersIslands = new HashMap<>();
        Map<Integer, TowerColor> colorTowersIslands = new HashMap<>();
        Map<Integer, Integer> forbiddenTokens = new HashMap<>();
        Map<Integer, Map<Color, Integer>> studClouds = new HashMap<>();
        Map<Color, String> professors = new HashMap<>();
        Map<String, Integer> coins = new HashMap<>();


        List<Player> players = g.getPlayers();

        for (Player player : players){
            studEntrance.put(player.getNickName(), g.getGameBoard().getPlayerBoard(player).getEntrance().getState());
            studDining.put(player.getNickName(), g.getGameBoard().getPlayerBoard(player).getDiningRoom().getState());
            numTowers.put(player.getNickName(), g.getGameBoard().getPlayerBoard(player).getTowerZone().getNumOfTowers());
            towerColor.put(player.getNickName(), player.getTowerColor());
            coins.put(player.getNickName(), g.getGameBoard().getPlayerBoard(player).getCoin());
        }

        List<IslandTile> islands = g.getGameBoard().getIslands();

        for(IslandTile isl : islands){
            studIslands.put(islands.indexOf(isl), isl.getState());
            numTowersIslands.put(islands.indexOf(isl), isl.getNumTowers());
            colorTowersIslands.put(islands.indexOf(isl), isl.getTowerColor());
            forbiddenTokens.put(islands.indexOf(isl), isl.getNumOfNoEntryTiles());
        }

        List<CloudTile> clouds = g.getGameBoard().getClouds();
        for (CloudTile cloud : clouds){
            studClouds.put(clouds.indexOf(cloud), cloud.getState());
        }

        for (Color color: g.getGameBoard().getProfessors().keySet()){
            professors.put(color, g.getGameBoard().getProfessors().get(color).getNickName());
        }

        int motherNaturePosition = islands.indexOf(g.getGameBoard().getMotherNature().getCurrentIsland());

        SMessageGameState message = new SMessageGameState(studEntrance, studDining, numTowers, towerColor, studIslands,
                numTowersIslands, colorTowersIslands, forbiddenTokens, studClouds, professors, motherNaturePosition, coins);

        System.out.println("\n------------------\n");
        TestCli cli = new TestCli();
        cli.showBoard(message);
        System.out.println("\n------------------\n");

        showBoard(message);
    }

}
