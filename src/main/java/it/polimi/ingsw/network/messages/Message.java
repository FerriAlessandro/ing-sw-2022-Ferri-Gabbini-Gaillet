package it.polimi.ingsw.network.messages;

import java.io.Serializable;

/**
 * Generic message abstract class. All messages sent across the network should be subclasses of this class.
 * @author A.G. Gaillet
 * @version 1.0
 */
public abstract class Message implements Serializable {
    MessageType type;

    public MessageType getType(){
        return type;
    }
}
