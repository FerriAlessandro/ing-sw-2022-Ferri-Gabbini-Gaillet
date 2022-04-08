package it.polimi.ingsw.network.messages;

import java.io.Serializable;

public abstract class Message implements Serializable {
    MessageType type;

    public MessageType getType(){
        return type;
    }
}
