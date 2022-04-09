package it.polimi.ingsw.network.messages;

/**
 * Message received by the server containing the chosen GameMode
 */

public class RMessageGameType extends RMessage{

    int mode; // 0 for a normal game, 1 for an expert game

    public RMessageGameType(int mode, String nickName){

        this.type = MessageType.R_GAMETYPE;
        this.mode = mode;
        this.nickName = nickName;
    }

    public int getMode() {
        return mode;
    }
}
