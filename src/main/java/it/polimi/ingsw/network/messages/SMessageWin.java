package it.polimi.ingsw.network.messages;

/**
 * {@link Message} sent by the server to signal that someone has won the game.
 * @author A.G. Gaillet
 * @version 1.0
 */
public class SMessageWin extends SMessage{

    /** String describing the win and who won */
    public final String winMessage;

    /**
     * Constructor.
     * @param winMessage
     */
    public SMessageWin(String winMessage){
        super(MessageType.S_WIN);
        this.winMessage = winMessage;
    }
}
