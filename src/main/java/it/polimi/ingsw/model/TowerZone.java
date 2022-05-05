package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.TowerWinException;

import java.io.Serializable;

/**
 * Represents the tower zone of a player board. It hosts the player's towers.
 * towers attribute indicates how many towers are left
 * @author AlessandroG
 * @version 2.0
 */
public class TowerZone implements Serializable {
    private static final long serialVersionUID = 1L;
    private int towers;

    /**
     * Constructor initializes towers attribute:
     * @param numOfPlayer indicates how many player will play the game, hence the number of towers.
     */
    public TowerZone(int numOfPlayer) throws IllegalArgumentException {
        switch (numOfPlayer) {
            case 2:
                this.towers = 8;
                break;
            case 3:
                this.towers = 6;
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
     * @throws TowerWinException when the number of remaining towers becomes zero.
     */
    public void remove() throws TowerWinException {
        if(this.towers > 0)
            this.towers --;
        if (this.towers == 0)
            throw new TowerWinException("This player has won the game!");
    }
}
