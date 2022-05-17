package it.polimi.ingsw.network.messages;

/**
 * This message is sent by the controller to ask a player to move mother nature.
 * @author A.G. Gaillet
 * @version 1.0
 */
public class SMessageMotherNature extends SMessage {
    public final int maxNumTiles;

    /**
     * Constructor.
     * @param maxNumTiles maximum number of tiles that mother nature can move across.
     */
    public SMessageMotherNature(int maxNumTiles){
        super(MessageType.S_MOTHERNATURE);
        this.maxNumTiles = maxNumTiles;
    }
}
