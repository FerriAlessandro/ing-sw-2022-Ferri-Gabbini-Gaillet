package it.polimi.ingsw.network.messages;

/**
 * This class represents a {@link Message} sent by the server.
 * @author A.G. Gaillet
 * @version 1.0
 */
public class SMessage extends Message{
    public SMessage(MessageType type){
        this.type = type;
    }
}
