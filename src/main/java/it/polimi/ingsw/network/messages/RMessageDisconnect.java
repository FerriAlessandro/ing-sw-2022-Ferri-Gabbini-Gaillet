package it.polimi.ingsw.network.messages;

/**
 * @author A.G. Gaillet
 * @version 1.0
 * {@link Message} to be sent by the client when it wishes to disconnect from the server
 */
public class RMessageDisconnect extends Message{
    public RMessageDisconnect(){
        this.type = MessageType.R_DISCONNECT;
    }
}
