package it.polimi.ingsw.network.messages;

/**
 * Message received by the server containing the selected CloudTile
 * @author Alessandro F.
 * @version 1.0
 */
public class RMessageCloud extends RMessage{

    /**
     * Index of the chosen {@link it.polimi.ingsw.model.CloudTile}
     */
    public final int cloudIndex;

    /**
     * Constructor.
     * @param index of the {@link it.polimi.ingsw.model.CloudTile}
     * @param nickName of the player
     */
    public RMessageCloud(int index, String nickName){

        this.type = MessageType.R_CLOUD;
        this.cloudIndex = index;   // from 1 to 3
        this.nickname = nickName;

    }
}
