package it.polimi.ingsw.model.enumerations;

/**
 * This is an Enumeration class for the assistant cards.
 * @author Alessandro F.
 * @version 1.0
 */
public enum AssistantCard {

    CHEETAH(1,1),
    OSTRICH (2, 1),
    CAT(3, 2),
    EAGLE(4, 2),
    FOX(5, 3),
    LIZARD(6, 3),
    OCTOPUS(7,4),
    DOG(8,4),
    ELEPHANT(9,5),
    TURTLE(10,5);

    private final int cardValue;
    private final int motherNatureMovement;
    private boolean hasBeenPlayed;

    /**
     * Constructor of the enum class, it's private by default. The attribute "hasBeenPlayed" is set to "False".
     * @param cardValue The value of the card, it's used to decide the order of the turn (who plays first, second etc.)
     * @param motherNatureMovement Defines the maximum number of mother nature's steps that can be done by the player
     */

    AssistantCard(int cardValue, int motherNatureMovement){

         this.cardValue = cardValue;
         this.motherNatureMovement = motherNatureMovement;
         this.hasBeenPlayed = false;

    }

    /**
     * Returns the number of steps that mother nature can do after the card is played.
     * @return The number of mother nature steps
     */
    public int getMotherNatureMovement() {
        return motherNatureMovement;
    }

    /**
     * Returns the card's value (to decide the turn order).
     * @return The value of the card
     */
    public int getCardValue() {
        return cardValue;
    }

    /**
     * @return "True" if the card has already been played, otherwise returns "False"
     */
    public boolean getPlayed(){
        return hasBeenPlayed;
    }
    /**
     * This method marks a card as 'Already Played' when a card is played by someone (this doesn't let another player
     * play the same card on the same turn).
     */
    public void setPlayed(){
        hasBeenPlayed = true;
    }

    /**
     * Remove the 'Already Played' mark from a card.
     */
    public void resetPlayed(){
        hasBeenPlayed = false;
    }


}
