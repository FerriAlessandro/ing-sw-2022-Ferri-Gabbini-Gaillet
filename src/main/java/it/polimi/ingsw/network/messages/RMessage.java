package it.polimi.ingsw.network.messages;

/**
 * Messages received by the Server (each specific message received will extend this class)
 * @author Alessandro F.
 * @version 1.0
 */

public abstract class RMessage extends Message{

    String nickName;

    public String getNickName() {
        return nickName;
    }
}
