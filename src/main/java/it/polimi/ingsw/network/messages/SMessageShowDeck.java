package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.model.enumerations.AssistantCard;

import java.util.ArrayList;

<<<<<<< HEAD
public class SMessageShowDeck extends Message{

    ArrayList<AssistantCard> cards;

    public SMessageShowDeck (ArrayList<AssistantCard> cards){

        this.type = MessageType.S_ASSISTANT;
        this.cards = cards;
    }

    public ArrayList<AssistantCard> getCards(){
        return this.cards;
    }
=======
public class SMessageShowDeck extends SMessage{
    public ArrayList<AssistantCard> cards;

    public SMessageShowDeck (ArrayList<AssistantCard> cards){
        super(MessageType.S_ASSISTANT);
        this.cards = cards;
    }
>>>>>>> main
}
