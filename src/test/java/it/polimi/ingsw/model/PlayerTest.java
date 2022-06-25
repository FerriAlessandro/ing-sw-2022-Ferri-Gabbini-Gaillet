package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.CardNotAvailableException;
import it.polimi.ingsw.exceptions.CardNotFoundException;
import it.polimi.ingsw.exceptions.EmptyDeckException;
import it.polimi.ingsw.model.enumerations.AssistantCard;
import it.polimi.ingsw.model.enumerations.TowerColor;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {
    Player p1, p2;

    @BeforeEach
    void setUp() {
        p1 = new Player(1, new AssistantDeck(), "alex", true, true, TowerColor.WHITE);
        p2 = new Player(2, new AssistantDeck(), "alex2", false, false, TowerColor.BLACK);
        for(AssistantCard assistantCard : AssistantCard.values())
            assistantCard.resetPlayed();

        }

    @Test
    @DisplayName("This test checks if the player's deck is correctly updated after playing a card")
    void testPlayCard() throws CardNotAvailableException, CardNotFoundException, EmptyDeckException {
        p1.playAssistantCard(AssistantCard.CHEETAH);
        assertEquals(p1.getPlayedCard(), AssistantCard.CHEETAH);
        assertFalse(p1.getDeck().getCards().contains(AssistantCard.CHEETAH));

    }

    @Test
    @DisplayName("This test checks if the CardNotAvailableException is thrown properly")
    void testCardNotAvailableException() throws CardNotAvailableException, CardNotFoundException, EmptyDeckException {
        p1.playAssistantCard(AssistantCard.CHEETAH);
        assertThrows(CardNotAvailableException.class,()->p2.playAssistantCard(AssistantCard.CHEETAH));

    }

    @Test
    @DisplayName("This test checks if the CardNotFoundException is thrown properly")
    void testCardNotFoundException() throws CardNotAvailableException, CardNotFoundException, EmptyDeckException {
        p1.playAssistantCard(AssistantCard.CHEETAH);
        assertThrows(CardNotFoundException.class, ()->p1.playAssistantCard(AssistantCard.CHEETAH));

    }

    @Test
    @DisplayName("This test checks the corner case in which the player's deck contains only cards that have already been " +
            "played by other players, the CardNotAvailableException should not be thrown")
    void testCornerCaseCardsNotAvailableException() throws CardNotAvailableException, CardNotFoundException, EmptyDeckException {
        p1.playAssistantCard(AssistantCard.CHEETAH);
        p1.playAssistantCard(AssistantCard.CAT);
        p2.getDeck().getCards().removeAll(p1.getDeck().getCards()); // p2 has only the CHEETAH and CAT which have already been played
        assertTrue(p2.getDeck().getCards().get(0).getPlayed()); //CHEETAH HAS ALREADY BEEN PLAYED
        p2.playAssistantCard(AssistantCard.CHEETAH); // DOES NOT THROW AN EXCEPTION, TEST PASSED

    }

    @Test
    @DisplayName("Tests if EmptyDeckException is thrown properly")
    void EmptyDeckExceptionTest() throws EmptyDeckException, CardNotFoundException, CardNotAvailableException{
        p1.playAssistantCard(AssistantCard.CHEETAH);
        p2.getDeck().getCards().removeAll(p1.getDeck().getCards());
        assertThrows(EmptyDeckException.class, ()-> p2.playAssistantCard(AssistantCard.CHEETAH));

    }

    @Test
    @DisplayName("Tests if setConnected and isConnected method work properly")
    void connectionTest() {
        p1.setConnected(true);
        assertTrue(p1.isConnected());
    }

}
