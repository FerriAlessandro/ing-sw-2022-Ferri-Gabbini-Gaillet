package it.polimi.ingsw.network.messages;

/**
 * Message received by the server containing the selected CloudTile
 */
public class RMessageCloud extends RMessage{

    int cloudIndex;

    public RMessageCloud(int index, String nickName){

        this.type = MessageType.R_CLOUD;
        this.cloudIndex = index;   // from 1 to 3
        this.nickName = nickName;

    }

    public int getCloudIndex() {
        return cloudIndex;
    }
}
