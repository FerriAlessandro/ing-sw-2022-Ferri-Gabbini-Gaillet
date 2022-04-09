package it.polimi.ingsw.network.messages;

/**
 * Message received by the server containing MotherNature's chosen movement
 */
public class RMessageMotherNature extends RMessage{

    int islandIndex;

    public RMessageMotherNature(int index, String nickName){

        this.type = MessageType.R_MOTHERNATURE;
        this.islandIndex = index; // from 1 to 12
        this.nickName = nickName;

    }

    public int getIslandIndex() {
        return islandIndex;
    }

}
