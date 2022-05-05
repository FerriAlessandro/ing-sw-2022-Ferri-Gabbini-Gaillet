package it.polimi.ingsw.model;

import java.io.Serializable;

/**This class represents the board personal to each player
 * @author A.G. Gaillet
 * @version 1.0
 * @see Entrance
 * @see DiningRoom
 * @see TowerZone
 */
public class PlayerBoard implements Serializable {
    private static final long serialVersionUID = 1L;
    private final Entrance entrance;
    private final DiningRoom diningRoom;
    private final TowerZone towerZone;
    private int coin;

    /**
     * Class constructor.
     * @param numOfPlayers number of players
     */
    public PlayerBoard(int numOfPlayers){
        entrance = new Entrance(numOfPlayers);
        diningRoom = new DiningRoom();
        towerZone = new TowerZone(numOfPlayers);
        coin = 0;
    }

    public DiningRoom getDiningRoom() {
        return diningRoom;
    }

    public Entrance getEntrance() {
        return entrance;
    }

    public TowerZone getTowerZone() {
        return towerZone;
    }

    public int getCoin() { return coin; }

    public void addCoin() { coin ++; }

    public void removeCoin(int num){coin -= num;}
}
