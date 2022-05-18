package it.polimi.ingsw.network.messages;

/**
 * Message containing information on the game to be created (number of players and game type)
 * @author A.G. Gaillet
 * @version 1.0
 */
public class RMessageGameSettings extends Message{
    public final int numPlayers;
    public final boolean expert;

    /**
     * Constructor.
     * @param numPlayers desired number of players
     * @param expert true to select an expert game, false otherwise
     */
    public RMessageGameSettings(int numPlayers, boolean expert){
        this.numPlayers = numPlayers;
        this.expert = expert;
        this.type = MessageType.R_GAMESETTINGS;
    }
}
