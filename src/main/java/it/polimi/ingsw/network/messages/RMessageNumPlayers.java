package it.polimi.ingsw.network.messages;

public class RMessageNumPlayers extends Message{
    public int numPlayers;

    public RMessageNumPlayers(int numPlayers){
        this.numPlayers = numPlayers;
        this.type = MessageType.R_NUMPLAYERS;
    }
}
