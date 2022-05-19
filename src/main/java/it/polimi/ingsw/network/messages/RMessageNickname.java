package it.polimi.ingsw.network.messages;

/**
 * This class represents the message used by the client to send the nickname of the corresponding player.
 * Message received by the server containing a player's chosen Nickname
 * @author A.G. Gaillet, Alessandro F
 * @version 1.0
 */
public class RMessageNickname extends RMessage{

    /**
     * Constructor
     * @param nickname chosen by the player
     */
    public RMessageNickname(String nickname){
        this.nickname = nickname;
        this.type = MessageType.R_NICKNAME;
    }

}
