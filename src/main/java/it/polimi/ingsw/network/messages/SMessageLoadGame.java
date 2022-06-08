package it.polimi.ingsw.network.messages;

/**
 * This message is used to communicate the settings of the saved game.
 * @author A.G. Gaillet
 * @version 1.0
 */
public class SMessageLoadGame extends SMessage{
    /**
     * number of players
     */
    public final int numOfPlayers;
    /**
     * true if the game is an expert game, false otherwise
     */
    public final boolean expert;

    /**
     * Constructor.
     * @param numOfPlayers number of players
     * @param expert true if the game is an expert game, false otherwise
     */
    public SMessageLoadGame(int numOfPlayers, boolean expert){
        super(MessageType.S_LOADGAME);
        this.numOfPlayers = numOfPlayers;
        this.expert = expert;
    }
}
