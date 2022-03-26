package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enumerations.AssistantCard;
import it.polimi.ingsw.model.enumerations.Wizard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This class models a Deck of Assistand Cards
 * @author Alessandro F.
 * @version 1.0
 */
public class AssistantDeck {

    private final Wizard wizard;
    private List<AssistantCard> deck = new ArrayList<>();

    public AssistantDeck(Wizard wizard){                            // player can choose its wizard
        this.wizard = wizard;
        // goes through each AssistantCard instance and adds it to the deck
        deck.addAll(Arrays.asList(AssistantCard.values()));

    }

    /**
     * @return The deck of the player with the cards he has left
     */
    public List<AssistantCard> getCards() {
        return deck;
    }

    /**
     * @return The back of the deck
     */
    public Wizard getWizard() {
        return wizard;
    }

    /**
     * @return The number of cards left in the deck
     */
    public int currNumOfCards(){
        return this.deck.size();
    }

    /**
     * Removes the played card from the player's deck
     * @param chosenCard the card played
     */
    public void removeCard(AssistantCard chosenCard){

        deck.remove(chosenCard);
    }

    /**
     * Marks the card chosen by the player as 'already played' so that other players cannot use the same card
     * @param chosenCard the card chosen by the player
     */
    public void chooseCard(AssistantCard chosenCard) {

        chosenCard.setPlayed();
    }
}
