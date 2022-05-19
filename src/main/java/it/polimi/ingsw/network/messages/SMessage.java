package it.polimi.ingsw.network.messages;

import java.io.Serial;

/**
 * This class represents a {@link Message} sent by the server.
 * @author A.G. Gaillet
 * @version 1.0
 */
public class SMessage extends Message{
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Constructor.
     * @param type of the message to be created.
     */
    public SMessage(MessageType type){
        this.type = type;
    }
}
