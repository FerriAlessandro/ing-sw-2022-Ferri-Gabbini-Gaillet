package it.polimi.ingsw.network.messages;

/**
 * This class represents a ping message to be used to check the liveliness of a connection.
 * @author A.G. Gaillet
 * @version 1.0
 */
public class PingMessage extends Message{
    /**
     * Constructor.
     */
    public PingMessage(){
        this.type = MessageType.PING;
    }
}
