package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enumerations.AssistantCard;
import it.polimi.ingsw.model.enumerations.Wizard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AssistantDeckTest {

    AssistantDeck deck1;
    AssistantDeck deck2;

    @BeforeEach
    void setUp() {
        deck1 = new AssistantDeck(Wizard.WIZARD_1);
        deck2 = new AssistantDeck(Wizard.WIZARD_2);
    }


    @Test
    @DisplayName("checks if removing a card from one deck affects the other in some way and if the number of current" +
            "cards gets updated correctly")
    void removeCardFromOneDeck() {
        deck1.removeCard(AssistantCard.CHEETAH);
        assertEquals(deck2.currNumOfCards()-1, deck1.currNumOfCards());
    }

    @Test
    @DisplayName("checks if the cards are correctly shared among decks")
    void sharedCards(){
        deck1.chooseCard(AssistantCard.CHEETAH);
        assertTrue(deck2.getCards().get(0).getPlayed());
    }

    @Test
    @DisplayName("Corner case: number of cards in a deck becomes 0")
    void zeroCardsLeft(){
        deck1.getCards().removeAll(deck2.getCards());
        assertEquals(deck1.currNumOfCards(), 0);
   }
}