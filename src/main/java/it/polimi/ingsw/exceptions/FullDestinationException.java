package it.polimi.ingsw.exceptions;

/**
 * Custom exception to be thrown when the given destination for a move or add is already full.
 * @author A.G. Gaillet
 * @version 1.0
 */
public class FullDestinationException extends Exception{
    /**
     * Constructor.
     */
    public FullDestinationException(){
        super("Given destination tile is full");
    }
}
