package it.polimi.ingsw.network.messages;

public class RMessageLoadGame extends RMessage{
    public boolean use;
    public RMessageLoadGame(boolean use){
        this.type = MessageType.R_LOADGAME;
        this.use = use;
    }
}
