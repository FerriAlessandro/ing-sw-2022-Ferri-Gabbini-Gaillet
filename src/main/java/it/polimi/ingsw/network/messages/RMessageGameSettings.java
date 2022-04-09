package it.polimi.ingsw.network.messages;

public class RMessageGameSettings extends Message{
    public int numPlayers;
    public boolean expert;

    public RMessageGameSettings(int numPlayers, boolean expert){
        this.numPlayers = numPlayers;
        this.expert = expert;
        this.type = MessageType.R_GAMESETTINGS;
    }
}
