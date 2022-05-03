package it.polimi.ingsw.network.messages;


public class SMessageCurrentPlayer extends SMessage{
    public String nickname;
    public SMessageCurrentPlayer(String nickname){
        super(MessageType.S_PLAYER);
        this.nickname = nickname;

    }
}
