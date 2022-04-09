package it.polimi.ingsw.network.messages;

/**
 * Message received by the server containing a player's chosen Nickname
 */
public class RMessageNickname extends RMessage{


    public RMessageNickname(String nickName){

        this.type = MessageType.R_NICKNAME;
        this.nickName = nickName;
    }
}
