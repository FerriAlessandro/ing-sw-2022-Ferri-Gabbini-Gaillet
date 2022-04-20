package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.model.enumerations.AssistantCard;

import java.util.ArrayList;

public class SMessageShowDeck extends Message{

    ArrayList<AssistantCard> cards;

    public SMessageShowDeck (ArrayList<AssistantCard> cards){

        this.type = MessageType.S_ASSISTANT;
        this.cards = cards;
    }

    public ArrayList<AssistantCard> getCards(){
        return this.cards;
    }
}
