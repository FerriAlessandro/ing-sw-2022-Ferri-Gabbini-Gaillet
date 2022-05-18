package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.model.enumerations.AssistantCard;

import java.util.ArrayList;

/**
 * This class represents a message sent by the server to ask for an assistant card.
 * @author A.G. Gaillet
 * @version 1.0
 */
public class SMessageShowDeck extends SMessage{
    public final ArrayList<AssistantCard> cards;

    /**
     * Constructor
     * @param cards {@link ArrayList} of available {@link AssistantCard}s
     */
    public SMessageShowDeck (ArrayList<AssistantCard> cards){
        super(MessageType.S_ASSISTANT);
        this.cards = cards;
    }

}
