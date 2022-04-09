package it.polimi.ingsw.network.messages;

public class RMessageNickname extends Message{
    public String nickname;

    public RMessageNickname(String nickname){
        this.nickname = nickname;
        this.type = MessageType.R_NICKNAME;
    }
}
