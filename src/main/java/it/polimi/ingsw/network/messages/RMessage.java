package it.polimi.ingsw.network.messages;

/**
 * Messages received by the Server (each specific message received will extend this class)
 */

public abstract class RMessage extends Message{

    String nickName;

    public String getNickName() {
        return nickName;
    }
}
