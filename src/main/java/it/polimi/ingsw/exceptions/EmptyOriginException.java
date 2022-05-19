package it.polimi.ingsw.exceptions;

/**
 * Custom exception to be thrown were the origin of a move or remove is already empty.
 * @author A.G. Gaillet
 * @version 1.0
 */
public class EmptyOriginException extends Exception{
    /**
     * Constructor.
     */
    public EmptyOriginException(){
        super("Given origin tile is empty");
    }
}
