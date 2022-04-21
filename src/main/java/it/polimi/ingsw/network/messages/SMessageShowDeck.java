package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.model.enumerations.AssistantCard;

import java.util.ArrayList;

public class SMessageShowDeck extends SMessage{
    public ArrayList<AssistantCard> cards;

    public SMessageShowDeck (ArrayList<AssistantCard> cards){
        super(MessageType.S_ASSISTANT);
        this.cards = cards;
    }

}
