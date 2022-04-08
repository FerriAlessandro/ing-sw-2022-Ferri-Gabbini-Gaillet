package it.polimi.ingsw.network.messages;

import java.util.List;

public class SMessageLobby extends Message{
    List<String> currentPlayers;
    int numPlayersTotal;

    SMessageLobby(List<String> currentPlayers, int numPlayersTotal){
        this.type = MessageType.S_LOBBY;
        this.currentPlayers = currentPlayers;
        this.numPlayersTotal = numPlayersTotal;

    }
}
