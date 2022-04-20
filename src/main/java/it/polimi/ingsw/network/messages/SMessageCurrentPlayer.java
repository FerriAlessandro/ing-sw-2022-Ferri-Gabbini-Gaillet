package it.polimi.ingsw.network.messages;

public class SMessageCurrentPlayer extends Message{

    private String nickName;

    public SMessageCurrentPlayer(String nickName){
        this.type = MessageType.S_PLAYER;
        this.nickName = nickName;
    }

    public String getNickName(){
        return this.nickName;
    }
}
