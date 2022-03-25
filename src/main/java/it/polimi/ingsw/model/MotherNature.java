package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.Random;

public class MotherNature {
    private final ArrayList<IslandTile> islands;
    private IslandTile currentIsland;

    public MotherNature(ArrayList<IslandTile> param){
        islands = param;
        Random rand = new Random();
        int startingIdx = rand.nextInt(islands.size());
        currentIsland = islands.get(startingIdx);
    }
    public void move(int positions){
        int idx = islands.indexOf(currentIsland);
        currentIsland = islands.get((idx + positions)%islands.size());
    }

    public IslandTile getCurrentIsland(){
        return currentIsland;
    }
}
