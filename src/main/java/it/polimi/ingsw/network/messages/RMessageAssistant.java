package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.model.enumerations.AssistantCard;

/**
 * Message sent by the client containing the chosen Assistant Card
 */
public class RMessageAssistant extends RMessage{

     AssistantCard playedAssistant;

    public RMessageAssistant(AssistantCard assistant, String nickName){

        this.type = MessageType.R_ASSISTANT;
        this.nickName = nickName;
        this.playedAssistant = assistant;

    }

    public AssistantCard getPlayedAssistant() {
        return playedAssistant;
    }
}
