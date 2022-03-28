package it.polimi.ingsw.exceptions;

/**
 * @author Alessandro F.
 * @version 1.0
 * This is a custom Exception to handle the case in which the player tries to play an assistant card
 * that has already been played by another player in the same turn
 */

public class CardNotAvailableException extends Exception{
    public CardNotAvailableException(){
        super("This card has already been played by another Player! Please select another Card");
    }


}
