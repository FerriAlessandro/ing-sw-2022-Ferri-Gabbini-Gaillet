package it.polimi.ingsw.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

/**
 * Represents MotherNature with its current island
 * @author A.G. Gaillet
 * @version 1.0
 * @serial
 * @see IslandTile
 */
public class MotherNature implements Serializable {
    private final ArrayList<IslandTile> islands;
    private IslandTile currentIsland;
    private static final long serialVersionUID = -5551582185882138723L;
    /**
     * Constructor
     * @param param {@link ArrayList} of {@link IslandTile} on which {@link MotherNature} moves
     */
    public MotherNature(ArrayList<IslandTile> param){
        islands = param;
        Random rand = new Random();
        int startingIdx = rand.nextInt(islands.size());
        currentIsland = islands.get(startingIdx);
    }

    /**
     * This method changes the current {@link IslandTile} of {@link MotherNature} by the given number of positions
     * @param positions represents the number of 'jumps' from one {@link IslandTile} to another
     */
    public void move(int positions){
        int idx = islands.indexOf(currentIsland);
        currentIsland = islands.get((idx + positions)%islands.size());
    }

    public IslandTile getCurrentIsland(){
        return currentIsland;
    }
}
