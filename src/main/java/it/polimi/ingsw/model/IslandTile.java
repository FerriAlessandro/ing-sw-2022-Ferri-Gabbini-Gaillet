package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enumerations.TowerColor;

/**
 * Represents a single island that hosts students and towers.
 * @author AlessandroG
 * @version 1.0
 */

public class IslandTile extends TileWithStudents{
    private int towers;
    private TowerColor towerColor;
    private int numOfNoEntryTiles;

    /**
     * Constructor simply initializes an "empty" island: no students and no towers.
     */
    public IslandTile() {
        super();
        this.towers = 0;
        this.towerColor = TowerColor.NONE;
        this.numOfNoEntryTiles = 0;
    }

    public TowerColor getTowerColor() {
        return this.towerColor;
    }

    public int getNumTowers() { return this.towers; }

    public int getNumOfNoEntryTiles() {
        return this.numOfNoEntryTiles;
    }

    /**
     * Changes the "owner" of the island, following a contention after Mother Nature movement or special character card use.
     * @param towerColor is the winner's tower color.
     * @throws IllegalArgumentException when the towerColor received is "NONE".
     */
    public void swapTower(TowerColor towerColor) {
        if(towerColor == TowerColor.NONE)
            throw new IllegalArgumentException("Players can't have NONE towerColor");
        if(towerColor == this.towerColor)
            return;
        if(this.towerColor == TowerColor.NONE) {
            towers = 1;
        }
        this.towerColor = towerColor;
    }

    /**
     * Necessary when two (or more) islands merge into one island.
     */
    public void mergeTower() {
        this.towers ++;
    }

    /**
     * Invoked when a player decides to put a NoEntryTile on the island.
     */
    public void addNoEntry() {
        this.numOfNoEntryTiles ++;
    }

    /**
     * Invoked when MotherNature moves on a forbidden island: removes the noEntryTile
     * @throws RuntimeException if the method is called even though there aren't noEntryTile.
     */
    public void removeNoEntry() {
        if(numOfNoEntryTiles == 0)
            throw new RuntimeException("There aren't noEntryTile to remove!");
        numOfNoEntryTiles --;
    }

    /**
     * Check if the island is forbidden: there is one (or more) noEntryTile(s) on the island
     * @return true if the island is forbidden, false otherwise.
     */
    public boolean isForbidden() {
        return (numOfNoEntryTiles > 0);
    }
}
