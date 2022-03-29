package it.polimi.ingsw.exceptions;

/**
 * This is a custom Exception to handle the case in which the player tries to play an assistant card
 * that is no longer in its deck.
 * @author Alessandro F.
 * @version 1.0
 */

public class CardNotFoundException extends Exception{

    public CardNotFoundException(){
        super("You already played this Card! Please select another Card");
    }

}
