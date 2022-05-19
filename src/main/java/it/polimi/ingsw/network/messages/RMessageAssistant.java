package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.model.enumerations.AssistantCard;

/**
 * Message sent by the client containing the chosen Assistant Card.
 * @author Alessandro F.
 * @version 1.0
 */
public class RMessageAssistant extends RMessage{

    /**
     * Chosen assistant card.
     */
     public final AssistantCard playedAssistant;

    /**
     * Constructor.
     * @param assistant chosen {@link AssistantCard}
     * @param nickName of the player
     */
    public RMessageAssistant(AssistantCard assistant, String nickName){

        this.type = MessageType.R_ASSISTANT;
        this.nickname = nickName;
        this.playedAssistant = assistant;

    }

}
