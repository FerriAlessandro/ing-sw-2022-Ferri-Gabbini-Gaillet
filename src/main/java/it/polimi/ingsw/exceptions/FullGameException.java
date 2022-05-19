package it.polimi.ingsw.exceptions;

/**
 * This class provides a custom exception to be thrown when trying to add a player to an already full game.
 * @author Alessandro F.
 * @version 1.0
 */
public class FullGameException extends Exception{
    /**
     * Constructor.
     */

    public FullGameException(){
        super("This Match is already full!");
    }
}
