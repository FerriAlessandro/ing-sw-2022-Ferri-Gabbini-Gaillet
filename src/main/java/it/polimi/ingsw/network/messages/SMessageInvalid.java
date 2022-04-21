package it.polimi.ingsw.network.messages;

public class SMessageInvalid extends SMessage{

    private final String error;

    public SMessageInvalid(String error){
        super(MessageType.S_INVALID);
        this.error = error;
    }

    public String getError(){
        return this.error;
    }
}
