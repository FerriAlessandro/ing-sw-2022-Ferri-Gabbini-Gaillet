package it.polimi.ingsw.view;

import it.polimi.ingsw.model.CloudTile;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.IslandTile;
import it.polimi.ingsw.model.Player;
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
    ClientHandler clientHandler;

    public VirtualView(ClientHandler clientHandler){
        this.clientHandler = clientHandler;
    }

    @Override
    public void askNickName() { clientHandler.sendMessage(new SMessage(MessageType.S_NICKNAME)); }

    @Override
    public void askGameSettings() {
        clientHandler.sendMessage(new SMessage(MessageType.S_GAMESETTINGS));
    }

    @Override
    public void askMotherNatureMove() {
        clientHandler.sendMessage(new SMessage(MessageType.S_MOTHERNATURE));
    }

    @Override
    public void showAssistantChoice() {}

    @Override
    public void showLobby(SMessageLobby message) {
        clientHandler.sendMessage(message);
    }

    @Override
    public void showDisconnectionMessage() {}

    @Override
    public void showWinMessage(SMessageWin message) {
        clientHandler.sendMessage(message);
    }

    @Override
    public void showBoard(SMessageGameState message) {
        clientHandler.sendMessage(message);
    }

    @Override
    public void showGenericMessage(SMessageInvalid message){
        clientHandler.sendMessage(message);
    }

    @Override
    public void showCharacterChoice() {

    }

    @Override
    public void askCloud(){

    }

    @Override
    public void askMove(){

    }

    
    /**
     * When this method is called by the observed {@link Game} it creates a {@link SMessageGameState} containing the game state
     * and calls the {@link VirtualView#showBoard(SMessageGameState)} method
     * @param observable the observable object
     */
    @Override
    public void notify(Observable observable) {

        Game g = (Game) observable;

        Map<String, Map<Color, Integer>> studEntrance = new HashMap<>();
        Map<String, Map<Color, Integer>> studDining = new HashMap<>();
        Map<Integer, Map<Color, Integer>> studIslands = new HashMap<>();
        Map<Integer, Integer> numTowersIslands = new HashMap<>();
        Map<Integer, TowerColor> colorTowersIslands = new HashMap<>();
        Map<Integer, Integer> forbiddenTokens = new HashMap<>();
        Map<Integer, Map<Color, Integer>> studClouds = new HashMap<>();
        Map<Color, String> professors = new HashMap<>();


        List<Player> players = g.getPlayers();

        for (Player player : players){
            studEntrance.put(player.getNickName(), g.getGameBoard().getPlayerBoard(player).getEntrance().getState());
            studDining.put(player.getNickName(), g.getGameBoard().getPlayerBoard(player).getDiningRoom().getState());
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

        SMessageGameState message = new SMessageGameState(studEntrance, studDining, studIslands, numTowersIslands, colorTowersIslands, forbiddenTokens, studClouds, professors, motherNaturePosition);

        showBoard(message);
    }

}
