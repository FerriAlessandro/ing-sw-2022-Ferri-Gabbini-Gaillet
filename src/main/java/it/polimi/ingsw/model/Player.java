package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.CardNotAvailableException;
import it.polimi.ingsw.exceptions.CardNotFoundException;
import it.polimi.ingsw.exceptions.EmptyDeckException;
import it.polimi.ingsw.model.enumerations.AssistantCard;
import it.polimi.ingsw.model.enumerations.Status;
import it.polimi.ingsw.model.enumerations.TowerColor;

/**
 * This class represents the Player entity in the game.
 * @author Alessandro F.
 * @version 1.0
 */

/**
 * Constructor of the Player Class, the status is set to "True".
 */
public class Player {
    private final String nickName;
    private final AssistantDeck assistantDeck;
    private Status playerStatus;
    private boolean isFirst;            //is the first player to play an assistant card
    private boolean isPlayerTurn;
    private final TowerColor towerColor;
    private AssistantCard playedCard;

    public Player(AssistantDeck deck, String name, boolean isFirst, boolean isPlayerTurn, TowerColor color){

        this.assistantDeck = deck;
        this.nickName = name;
        this.isFirst = isFirst;
        this.isPlayerTurn = isPlayerTurn;
        this.towerColor = color;
        this.playerStatus = Status.ONLINE;

    }

    /**
     * @return The Nickname of the Player
     */
    public String getNickName() {
        return nickName;
    }

    /**
     * @return The deck of the Player
     */
    public AssistantDeck getDeck() {
        return assistantDeck;
    }

    /**
     * @return The Status of the Player (Online, Offline, Inactive)
     */
    public Status getStatus() {
        return playerStatus;
    }

    /**
     * This method sets the Player's Status.
     * @param playerStatus current Status of the Player
     */
    public void setStatus(Status playerStatus){
        this.playerStatus = playerStatus;
    }

    /**
     * @return "True" if the Player is the first to play, "False" otherwise
     */
    public boolean isFirst(){
        return isFirst;
    }

    /**
     * Set the player as the 'First Player' in a round.
     * @param first Boolean to mark (or remove) a player as 'First Player'
     */
    public void setFirst(boolean first){     //takes a bool as a parameter so we don't have to make another method to reset it
        this.isFirst = first;
    }

    /**
     * @return "True" if it's the Player's turn, "False" otherwise
     */
    public boolean isPlayerTurn(){
        return isPlayerTurn;
    }

    /**
     * Marks the player as the 'Current Playing' player.
     * @param turn Boolean to set to "True" or "False" the 'isPlayerTurn' attribute
     */
    public void setPlayerTurn(boolean turn){
        this.isPlayerTurn = turn;
    }

    /**
     * @return The color of the Player's Towers
     */
    public TowerColor getTowerColor() {
        return towerColor;
    }

    /**
     * @return The Assistant Card played by the Player
     */
    public AssistantCard getPlayedCard() {
        return playedCard;
    }

    /**
     * @return "False" if the player has at least 1 card that hasn't already been played in this turn by other players, "True" otherwise.
     */
    private boolean noCardsAvailable(){
        boolean noCards = true;
        for(AssistantCard a : assistantDeck.getCards())
            if(!a.getPlayed())
                noCards = false;
        return noCards;
    }

    /**
     * This method sets the Assistant card played by the Player, marks the card as 'already played' and removes it from the deck.
     * @throws CardNotAvailableException Thrown if the selected card was already played by someone else in the same turn
     * @throws CardNotFoundException Thrown if the player doesn't have the selected card in its deck
     * @param card The card played by the Player
     */

    public void playAssistantCard (AssistantCard card) throws CardNotAvailableException, CardNotFoundException, EmptyDeckException {

        if(!(this.assistantDeck.getCards().contains(card)))
            throw new CardNotFoundException();

        if(card.getPlayed() && !noCardsAvailable())
            throw new CardNotAvailableException();

        this.playedCard = card;
        assistantDeck.chooseCard(card);
        assistantDeck.removeCard(card);

        if(assistantDeck.currNumOfCards() == 0)
            throw new EmptyDeckException(getNickName() + " played his last card, this is the last Round!");
    }




}
