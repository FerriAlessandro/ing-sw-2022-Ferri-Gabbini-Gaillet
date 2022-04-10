package it.polimi.ingsw.network.messages;

/**
 * This class represents the message used by the client to send the nickname of the corresponding player.
 * @author A.G. Gaillet
 * @version 1.0
 */
public class RMessageNickname extends Message{
    public String nickname;

    public RMessageNickname(String nickname){
        this.nickname = nickname;
        this.type = MessageType.R_NICKNAME;
    }
}
