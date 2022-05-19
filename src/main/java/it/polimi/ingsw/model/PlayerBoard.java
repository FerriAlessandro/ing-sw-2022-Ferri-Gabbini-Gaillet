package it.polimi.ingsw.model;

import java.io.Serial;
import java.io.Serializable;

/**This class represents the board personal to each player
 * @author A.G. Gaillet
 * @version 1.0
 * @see Entrance
 * @see DiningRoom
 * @see TowerZone
 */
public class PlayerBoard implements Serializable {
    @Serial
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

    /**
     * @return the {@link DiningRoom} of this {@link PlayerBoard}
     */
    public DiningRoom getDiningRoom() {
        return diningRoom;
    }

    /**
     * @return the {@link Entrance} of this {@link PlayerBoard}
     */
    public Entrance getEntrance() {
        return entrance;
    }

    /**
     * @return the {@link TowerZone} of this {@link PlayerBoard}
     */
    public TowerZone getTowerZone() {
        return towerZone;
    }

    /**
     * @return the number of coins owned by the linked player
     */
    public int getCoin() { return coin; }

    /**
     * Adds a coin to the linked player.
     */
    public void addCoin() { coin ++; }

    /**
     * Removes a coin from the linked player.
     */
    public void removeCoin(int num){coin -= num;}
}
