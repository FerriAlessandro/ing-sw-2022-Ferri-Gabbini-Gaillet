package it.polimi.ingsw.exceptions;

/**
 * Exception to throw when the bag is empty
 * @author Alessandro F.
 * @version 1.0
 */
public class EmptyBagException extends Exception{
    /**
     * Constructor.
     */
    public EmptyBagException(){
        super("Not enough students in the bag, this is the last round!");
    }
}
