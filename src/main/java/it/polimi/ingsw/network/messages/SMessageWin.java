package it.polimi.ingsw.network.messages;

/**
 * {@link Message} sent by the server to signal that someone has won the game.
 * @author A.G. Gaillet
 * @version 1.0
 */
public class SMessageWin extends Message{
    public String winner;
    SMessageWin(String winnerNickname){
        this.type = MessageType.S_WIN;
        winner = winnerNickname;
    }
}
