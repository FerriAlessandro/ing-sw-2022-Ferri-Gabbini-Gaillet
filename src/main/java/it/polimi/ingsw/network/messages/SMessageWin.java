package it.polimi.ingsw.network.messages;

/**
 * {@link Message} sent by the server to signal that someone has won the game.
 * @author A.G. Gaillet
 * @version 1.0
 */
public class SMessageWin extends SMessage{

    /** String describing who won */
    public final String nickname;
    /** True for a tied game, false otherwise */
    public final boolean tie;

    /**
     * Constructor.
     * @param nickname of the winner or "It's a tie" for a tied game
     */
    public SMessageWin(String nickname, boolean tie){
        super(MessageType.S_WIN);
        this.nickname = nickname;
        this.tie = tie;
    }
}
