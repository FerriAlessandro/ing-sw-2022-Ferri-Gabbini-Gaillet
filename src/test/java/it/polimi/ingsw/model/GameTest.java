package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.enumerations.AssistantCard;
import it.polimi.ingsw.model.enumerations.Characters;
import it.polimi.ingsw.model.enumerations.Color;
import org.junit.jupiter.api.*;


import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    Game game;
    Player p1,p2,p3;
    ArrayList<String> names = new ArrayList<>();

    @BeforeEach
    void setup() {
        names.add("Alessandro");
        names.add("Angelo");
        names.add("Marco");
        game = new Game(names);
        p1=game.getPlayers().get(0);
        p2=game.getPlayers().get(1);
        p3=game.getPlayers().get(2);
        for(AssistantCard assistantCard : AssistantCard.values())
            assistantCard.resetPlayed();

    }

    @Test
    @DisplayName("Tests if the sorting based on the played Assistant Cards works properly")
    public void TestSort() throws CardNotAvailableException, CardNotFoundException, EmptyDeckException {

        game.getPlayers().get(0).playAssistantCard(AssistantCard.EAGLE); // this card is played to avoid leaving player 2 with just 1 card (otherwise it throws EmptyDeckException)
        game.getPlayers().get(0).playAssistantCard(AssistantCard.CAT);
        game.getPlayers().get(1).getDeck().getCards().removeAll(p1.getDeck().getCards());
        game.getPlayers().get(1).playAssistantCard(AssistantCard.CAT);
        game.getPlayers().get(2).playAssistantCard(AssistantCard.CHEETAH);
        assertTrue(p1.isFirst());
        assertTrue(p1.isPlayerTurn());
        game.sortPlayersActionTurn();
        assertTrue(p3.isFirst());
        assertTrue(p3.isPlayerTurn());
        assertEquals("Marco", game.getPlayers().get(0).getNickName());
        assertEquals("Alessandro", game.getPlayers().get(1).getNickName());
        assertEquals("Angelo", game.getPlayers().get(2).getNickName());

        //Notice that Alessandro played CAT before Angelo, after the sorting Alessandro should still play before Angelo

    }

    @Test
    @DisplayName("Tests if the getCurrentPlayer method works properly")
    public void TestGetCurrentPlayer() throws NoCurrentPlayerException {
        Player p = game.getCurrentPlayer();

        assertEquals(p, p1);
        assertEquals(p.getNickName(), p1.getNickName());

    }

    @Test
    @DisplayName("Tests if the playAssistantCard method works properly")
    public void TestPlayAssistantCard() throws NoCurrentPlayerException, EndRoundException, EmptyDeckException,
                                                CardNotAvailableException, CardNotFoundException {
        assertTrue(p1.isPlayerTurn());
        game.playAssistantCard(AssistantCard.CHEETAH);
        assertFalse(p1.isPlayerTurn());
        assertTrue(p2.isPlayerTurn());
        assertTrue(AssistantCard.CHEETAH.getPlayed());
        assertEquals(p2.getDeck().getCards().size() - 1 , p1.getDeck().getCards().size());
        assertEquals(p1.getPlayedCard(), AssistantCard.CHEETAH);

    }

    @Test
    @DisplayName("Tests if the playAssistantCard method works properly on corner case 1: the first player plays its last card")
    public void TestCornerCase1PlayAssistantCard() {

        for(AssistantCard a : AssistantCard.values()){
            p1.getDeck().getCards().remove(a);
        }
        p1.getDeck().getCards().add(AssistantCard.OSTRICH);
        assertTrue(p1.isPlayerTurn());
        assertThrows(EmptyDeckException.class, ()-> game.playAssistantCard(AssistantCard.OSTRICH));
        assertFalse(p1.isPlayerTurn());
        assertTrue(p2.isPlayerTurn());
        assertEquals(p1.getPlayedCard(), AssistantCard.OSTRICH);

    }

    @Test
    @DisplayName("Tests if the playAssistantCard method works properly on corner case 2: the last player plays a card")
    public void TestCornerCase2PlayAssistantCard() {

        p3.setPlayerTurn(true);
        p1.setPlayerTurn(false);
        assertThrows(EndRoundException.class, ()-> game.playAssistantCard(AssistantCard.CHEETAH));
        assertEquals(p3.getPlayedCard(), AssistantCard.CHEETAH);

    }

    @Test
    @DisplayName("Tests if the playAssistantCard method works properly on corner case 3: the last player plays its last card")
    public void TestCornerCase3PlayAssistantCard() {
        for(AssistantCard a : AssistantCard.values()){
            p3.getDeck().getCards().remove(a);
        }
        p3.getDeck().getCards().add(AssistantCard.OSTRICH);
        p1.setPlayerTurn(false);
        p3.setPlayerTurn(true);
        assertThrows(EndRoundException.class, ()->game.playAssistantCard(AssistantCard.OSTRICH));
        assertEquals(p3.getPlayedCard(), AssistantCard.OSTRICH);

    }

    @Test
    @DisplayName("Tests the ChooseCloud method")
    public void ChooseCloudTest() throws NoCurrentPlayerException, EndRoundException, CloudNotFullException, FullDestinationException, EmptyBagException {

        for(Player p : game.getPlayers()){
            for(Color c : Color.values()){
                game.getGameBoard().getPlayerBoard(p).getEntrance().getState().put(c, 0);
            }
        }
        assertTrue(p1.isPlayerTurn());
        game.getGameBoard().fillClouds();

        Entrance entrance =  game.getGameBoard().getPlayerBoard(game.getCurrentPlayer()).getEntrance();
        for (Color color: Color.values()){
            while(entrance.getNumStudents(color) > 0){
                entrance.removeStudent(color);
            }
        }

        game.chooseCloud(game.getGameBoard().getClouds().get(0));
        assertFalse(p1.isPlayerTurn());
        assertTrue(p2.isPlayerTurn());
        assertTrue(game.getGameBoard().getPlayerBoard(p1).getEntrance().getNumStudents() != 0);

    }

    @Test
    @DisplayName("Tests the ChooseCloud method when the player that is choosing the Cloud is the last player")
    public void ChooseCloudTestCorner() throws EmptyBagException {

        for(Player p : game.getPlayers()){
            for(Color c : Color.values()){
                game.getGameBoard().getPlayerBoard(p).getEntrance().getState().put(c, 0);
            }
        }

        p1.setPlayerTurn(false);
        p3.setPlayerTurn(true);
        game.getGameBoard().fillClouds();

        Entrance entrance =  game.getGameBoard().getPlayerBoard(game.getCurrentPlayer()).getEntrance();
        for (Color color: Color.values()){
            while(entrance.getNumStudents(color) > 0){
                entrance.removeStudent(color);
            }
        }
        assertThrows(EndRoundException.class, ()->game.chooseCloud(game.getGameBoard().getClouds().get(0)));
        assertTrue(game.getGameBoard().getPlayerBoard(p3).getEntrance().getNumStudents() != 0);


    }

    @Test
    @DisplayName("Tests if the method SortPlayersAssistantTurn works properly")
    public void TestSortPlayersAssistantTurn(){

        p1.setFirst(false);
        p3.setFirst(true);
        game.sortPlayersAssistantTurn();
        assertEquals(game.getPlayers().get(0), p3);
        assertEquals(game.getPlayers().get(1), p1);
        assertEquals(game.getPlayers().get(2), p2);

        p3.setFirst(false);
        p2.setFirst(true);
        game.sortPlayersAssistantTurn();
        assertEquals(game.getPlayers().get(0), p2);
        assertEquals(game.getPlayers().get(1), p3);
        assertEquals(game.getPlayers().get(2), p1);

    }

    @Test
    @DisplayName("Tests if the CheckWinner method works properly when there isn't a Tie in the number of towers")
    public void TestCheckWinNoTie() throws TowerWinException {

        game.getGameBoard().getPlayerBoard(p1).getTowerZone().remove();
        game.getGameBoard().getPlayerBoard(p2).getTowerZone().remove();
        game.getGameBoard().getPlayerBoard(p3).getTowerZone().remove();
        game.getGameBoard().getPlayerBoard(p3).getTowerZone().remove();
        assertEquals(game.checkWinner(), p3.getNickName());
        assertNotEquals(game.checkWinner(), p1.getNickName());
        assertNotEquals(game.checkWinner(), p2.getNickName());
    }


    @Test
    @DisplayName("Tests if the CheckWinner method works properly when there is a Tie in the number of towers but not in the number of Professors")
    public void TestCheckWinTieInTowers() throws TowerWinException {

        game.getGameBoard().getPlayerBoard(p1).getTowerZone().remove();
        game.getGameBoard().getPlayerBoard(p2).getTowerZone().remove();
        game.getGameBoard().getProfessors().put(Color.RED, p1);
        game.getGameBoard().getProfessors().put(Color.BLUE, p2);
        game.getGameBoard().getProfessors().put(Color.GREEN, p1);
        game.getGameBoard().getProfessors().put(Color.YELLOW, p3);
        game.getGameBoard().getProfessors().put(Color.PINK, p3);
        assertEquals(game.checkWinner(), p1.getNickName());
        assertNotEquals(game.checkWinner(), p2.getNickName());
        assertNotEquals(game.checkWinner(), p3.getNickName());
    }

    @Test
    @DisplayName("Tests if the CheckWinner method works properly when there is a Tie in the number of towers of all 3 players and in the number of Professors")
    public void TestCheckWin3PlayersTie() throws TowerWinException {

        game.getGameBoard().getPlayerBoard(p1).getTowerZone().remove();
        game.getGameBoard().getPlayerBoard(p2).getTowerZone().remove();
        game.getGameBoard().getPlayerBoard(p3).getTowerZone().remove();
        game.getGameBoard().getProfessors().put(Color.RED, p1);
        game.getGameBoard().getProfessors().put(Color.BLUE, p2);
        game.getGameBoard().getProfessors().put(Color.GREEN, p1);
        game.getGameBoard().getProfessors().put(Color.YELLOW, p3);
        game.getGameBoard().getProfessors().put(Color.PINK, p3);
        assertEquals("Tie", game.checkWinner());
        assertNotEquals(game.checkWinner(), p2.getNickName());
        assertNotEquals(game.checkWinner(), p3.getNickName());
        assertNotEquals(game.checkWinner(), p1.getNickName());
    }

    @Test
    @DisplayName("Tests if the CheckWinner method works properly when there is a Tie in the number of towers of all 3 players but not in the number of Professors")
    public void TestCheckWin3PlayersTieTowers() throws TowerWinException {

        game.getGameBoard().getPlayerBoard(p1).getTowerZone().remove();
        game.getGameBoard().getPlayerBoard(p2).getTowerZone().remove();
        game.getGameBoard().getPlayerBoard(p3).getTowerZone().remove();
        game.getGameBoard().getProfessors().put(Color.RED, p1);
        game.getGameBoard().getProfessors().put(Color.BLUE, p2);
        game.getGameBoard().getProfessors().put(Color.YELLOW, p3);
        game.getGameBoard().getProfessors().put(Color.PINK, p3);
        assertEquals(p3.getNickName(), game.checkWinner());
        assertNotEquals(game.checkWinner(), p2.getNickName());
        assertNotEquals(game.checkWinner(), p1.getNickName());
    }

    @Test
    @DisplayName("Tests if the method 'GetCharacterByName' works properly")
    public void TestGetCharacterByName() {

        game.getGameBoard().addCharacterCard(Characters.JESTER);
        game.getGameBoard().addCharacterCard(Characters.GRANDMA_HERB);
        game.getGameBoard().addCharacterCard(Characters.MONK);

        assertEquals(game.getCharacterByName(Characters.JESTER), game.getGameBoard().getCharacters().get(0));
        assertSame(game.getCharacterByName(Characters.JESTER), game.getGameBoard().getCharacters().get(0));
        assertThrows(RuntimeException.class, ()-> game.getCharacterByName(Characters.ROGUE));

    }






    }
