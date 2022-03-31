package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enumerations.TowerColor;

/**
 * Represents the tower zone of a player board. It hosts the player's towers.
 * More specifically there are towers attribute that indicates how many towers are left
 * and color indicates the color of the tower of this player.
 * @author AlessandroG
 * @version 1.0
 */
public class TowerZone {
    private int towers;
    private TowerColor color;

    /**
     * Constructor receives 2 param:
     * @param numOfPlayer indicates how many player will play the game, hence the number of towers.
     * @param towerColor determines the color of the tower of this towerZone. Actually it has to be
     *                   compatible with the numOfPlayer. Read rules for more info.
     */
    public TowerZone(int numOfPlayer, TowerColor towerColor) throws IllegalArgumentException {
        switch (numOfPlayer) {
            case 2:
                this.towers = 8;
                switch (towerColor) {
                    case BLACK:
                    case WHITE:
                        this.color = towerColor;
                        break;
                    default:
                        throw new IllegalArgumentException("With 2 players only BLACK and WHITE allowed!");
                }
                break;
            case 3:
                this.towers = 9;
                switch (towerColor) {
                    case BLACK:
                    case WHITE:
                    case GRAY:
                        this.color = towerColor;
                        break;
                    default:
                        throw new IllegalArgumentException("Color must be GRAY, BLACK or WHITE!");
                }
                break;
            default:
                throw new IllegalArgumentException("Illegal number of players!");
        }
    }

    public int getNumOfTowers() {
        return this.towers;
    }

    /**
     * This method simply add a tower.
     * It is called when an island possessed by the towerZone's owner is lost: the tower on the island must return here.
     */
    public void add() {
        this.towers ++;
    }

    /**
     * This method simply remove a tower.
     * It is called when an island is conquered by the towerZone's owner: a tower from here must go there.
     * @throws RuntimeException when there are no towers left, still the method is called.
     */
    public void remove() throws RuntimeException {
        if(getNumOfTowers() == 0)
            throw new RuntimeException("There are no towers left. This player has probably won the game!");
        this.towers --;
    }
}
