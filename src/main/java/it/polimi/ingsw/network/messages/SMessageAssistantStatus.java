package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.model.enumerations.AssistantCard;

public class SMessageAssistantStatus extends SMessage{
    /** Nickname of the player who chose an assistant card */
    public final String nickname;

    /** The chosen card */
    public final AssistantCard chosenAssistant;

    /**
     * Constructor.
     * @param nickname of the player that choose the assistant
     * @param assistantCard the chosen assistant card
     */
    public SMessageAssistantStatus(String nickname, AssistantCard assistantCard){
        super(MessageType.S_ASSISTANTSTATUS);
        this.nickname = nickname;
        this.chosenAssistant = assistantCard;
    }

}
