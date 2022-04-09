package it.polimi.ingsw.network.messages;

public class SMessageInvalid extends Message{

    String error;

    public SMessageInvalid(String error){

        this.type = MessageType.S_INVALID;
        this.error = error;

    }

    public String getError(){
        return this.error;
    }
}
