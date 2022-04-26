package it.polimi.ingsw.network.messages;

/**
 * {@link Message} sent by the server to signal that someone has won the game.
 * @author A.G. Gaillet
 * @version 1.0
 */
public class SMessageWin extends SMessage{

    public String winMessage;
    public SMessageWin(String winMessage){
        super(MessageType.S_WIN);
        this.winMessage = winMessage;
    }
}
