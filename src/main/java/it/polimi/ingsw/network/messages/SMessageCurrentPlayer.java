package it.polimi.ingsw.network.messages;

/**
 * This class represents the message used to broadcast the current player.
 * @author A.G. Gaillet, Alessandro F.
 * @version 1.0
 */
public class SMessageCurrentPlayer extends SMessage{
    public final String nickname;

    /**
     * Constructor.
     * @param nickname of the current player
     */
    public SMessageCurrentPlayer(String nickname){
        super(MessageType.S_PLAYER);
        this.nickname = nickname;

    }
}
