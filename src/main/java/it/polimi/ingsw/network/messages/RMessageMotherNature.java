package it.polimi.ingsw.network.messages;

/**
 * Message received by the server containing MotherNature's chosen movement.
 * @author Alessandro F.
 * @version 1.0
 */
public class RMessageMotherNature extends RMessage{

    /** The index of the chosen {@link it.polimi.ingsw.model.IslandTile} of destination*/
    public final int islandIndex;

    /**
     * Constructor.
     * @param index of the island of destination
     * @param nickName of the player
     */
    public RMessageMotherNature(int index, String nickName){

        this.type = MessageType.R_MOTHERNATURE;
        this.islandIndex = index; // from 1 to 12
        this.nickname = nickName;

    }

}
