package it.polimi.ingsw.network.messages;

/**
 * Message used to communicate the intention of restoring a saved game.
 * @author A.G. Gaillet
 * @version 1.0
 */
public class RMessageLoadGame extends RMessage{
    public final boolean use;

    /**
     * Constructor.
     * @param use true in case of restoring old game, false to create a new one
     */
    public RMessageLoadGame(boolean use){
        this.type = MessageType.R_LOADGAME;
        this.use = use;
    }
}
