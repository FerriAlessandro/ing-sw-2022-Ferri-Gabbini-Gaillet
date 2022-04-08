package it.polimi.ingsw.view;

import it.polimi.ingsw.model.CloudTile;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.IslandTile;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.enumerations.Color;
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
    //TODO: reference to socket

    public VirtualView(/* TODO: receive socket reference */){
        //TODO: assign param to attribute
    }
    @Override
    public void askNickName(SMessage message) {

    }

    @Override
    public void askNumOfPlayers(SMessage message) {

    }

    @Override
    public void askMotherNatureMove(SMessage message) {

    }

    @Override
    public void showAssistantChoice() {

    }

    @Override
    public void showAssistantChoice(SMessage message) {

    }

    @Override
    public void showLobby(SMessageLobby message) {

    }

    @Override
    public void showDisconnectionMessage() {

    }

    @Override
    public void showWinMessage(SMessageWin message) {

    }

    @Override
    public void showBoard(SMessageGameState message) {
        //TODO: send message
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
        Map<Integer, Integer> towerIslands = new HashMap<>();
        Map<Integer, Integer> forbiddenTokens = new HashMap<>();
        Map<Integer, Map<Color, Integer>> studClouds = new HashMap<>();


        List<Player> players = g.getPlayers();

        for (Player player : players){
            studEntrance.put(player.getNickName(), g.getGameBoard().getPlayerBoard(player).getEntrance().getState());
            studDining.put(player.getNickName(), g.getGameBoard().getPlayerBoard(player).getDiningRoom().getState());
        }

        List<IslandTile> islands = g.getGameBoard().getIslands();

        for(IslandTile isl : islands){
            studIslands.put(islands.indexOf(isl), isl.getState());
            towerIslands.put(islands.indexOf(isl), isl.getNumTowers());
            forbiddenTokens.put(islands.indexOf(isl), isl.getNumOfNoEntryTiles());
        }

        List<CloudTile> clouds = g.getGameBoard().getClouds();
        for (CloudTile cloud : clouds){
            studClouds.put(clouds.indexOf(cloud), cloud.getState());
        }

        int motherNaturePosition = islands.indexOf(g.getGameBoard().getMotherNature().getCurrentIsland());

        SMessageGameState message = new SMessageGameState(studEntrance, studDining, studIslands, towerIslands, forbiddenTokens, studClouds, motherNaturePosition);

        showBoard(message);
    }

}
