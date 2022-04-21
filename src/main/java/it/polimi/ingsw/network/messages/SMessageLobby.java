package it.polimi.ingsw.network.messages;

import java.util.List;

/**
 * Message sent by the server to show the status of the lobby.
 * @author A.G. Gaillet
 * @version 1.0
 */
public class SMessageLobby extends SMessage{
    public List<String> currentPlayers;
    public int numPlayersTotal;

    SMessageLobby(List<String> currentPlayers, int numPlayersTotal){
        super(MessageType.S_LOBBY);
        this.currentPlayers = currentPlayers;
        this.numPlayersTotal = numPlayersTotal;

    }
}
