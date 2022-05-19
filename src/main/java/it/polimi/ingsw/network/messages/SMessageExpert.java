package it.polimi.ingsw.network.messages;

/**
 * This message is used to communicate whether the game implements expert rules or not.
 * @author A.G. Gaillet
 * @version 1.0
 */
public class SMessageExpert extends SMessage{
    /** True if the game is an expert-game, false otherwise */
    public final boolean expert;

    /**
     * Constructor.
     * @param expert true if the game is an expert-game, false otherwise
     */
    public SMessageExpert(boolean expert){
        super(MessageType.S_EXPERT);
        this.expert = expert;
    }
}
