package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enumerations.TowerColor;

/**This class represents the board personal to each player
 * @author A.G. Gaillet
 * @version 1.0
 * @see Entrance
 * @see DiningRoom
 * @see TowerZone
 */
public class PlayerBoard {
    Entrance entrance;
    DiningRoom diningRoom;
    TowerZone towerZone;

    /**
     * Class constructor.
     * @param towerColor {@link TowerColor} of the towers to be stored in the linked {@link TowerZone}
     * @param numOfPlayers number of players
     */
    public PlayerBoard(TowerColor towerColor, int numOfPlayers){
        entrance = new Entrance(numOfPlayers);
        diningRoom = new DiningRoom();
        towerZone = new TowerZone(numOfPlayers, towerColor);
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
}
