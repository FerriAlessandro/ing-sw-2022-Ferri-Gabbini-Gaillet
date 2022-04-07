package it.polimi.ingsw.network.messages;

import java.io.Serializable;

public class Message implements Serializable {
    MessageType type;

    public Message(){
        type = MessageType.GENERIC;
    }

    public Message(MessageType type){
        this.type = type;
    }

    public MessageType getType(){
        return type;
    }
}
