package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.model.enumerations.Color;
import it.polimi.ingsw.network.messages.SMessageGameState;
import it.polimi.ingsw.view.cli.drawables.Drawable;
import it.polimi.ingsw.view.cli.drawables.DrawableCloud;
import it.polimi.ingsw.view.cli.drawables.DrawableIsland;
import it.polimi.ingsw.view.cli.drawables.DrawablePlayerBoard;

import java.util.ArrayList;

public class AdvancedCli extends Cli {

    /**
     * Shows the game status displaying the board.
     *
     * @param gameState message containing game status.
     */
    @Override
    public void showBoard(SMessageGameState gameState) {

        ArrayList<Drawable> playerBoards = new ArrayList<>();
        for (String player: gameState.studEntrance.keySet()){
            if(expert) {
                System.out.print(player.toUpperCase() + " has " + gameState.coins.get(player) + " coins\n");
                coins.put(player,gameState.coins.get(player));
                System.out.print("\n");
            }


            ArrayList<Color> profs = new ArrayList<>();
            for(Color color : gameState.professors.keySet()){
                if (gameState.professors.get(color).equals(player)){
                    profs.add(color);
                }
            }
            DrawablePlayerBoard playerBoard = new DrawablePlayerBoard(gameState.studDining.get(player),
                    gameState.studEntrance.get(player), gameState.towerNumber.get(player), gameState.towerColor.get(player),
                    profs, player);
            playerBoards.add(playerBoard);
        }

        ArrayList<Drawable> clouds = new ArrayList<>();
        for (int cloud : gameState.studClouds.keySet()){
            clouds.add(new DrawableCloud(gameState.studClouds.get(cloud), cloud + 1));
        }

        ArrayList<Drawable> islands = new ArrayList<>();
        for (int island : gameState.studIslands.keySet()) {
            islands.add(new DrawableIsland(gameState.studIslands.get(island), gameState.numTowersIslands.get(island),
                    gameState.colorTowerIslands.get(island), gameState.forbiddenTokens.get(island), island + 1, gameState.motherNaturePosition == island));
        }

        System.out.print(islands.get(0).drawFullScene(islands, clouds, playerBoards));
    }

}
