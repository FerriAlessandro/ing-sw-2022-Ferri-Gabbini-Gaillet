package it.polimi.ingsw.exceptions;

/**
 * Custom exception to be thrown when only three islands remain.
 * @author A.G. Gaillet
 * @version 1.0
 */
public class NumOfIslandsException extends Exception{
    public NumOfIslandsException() {
        super("The minimum number of islands has been reached");
    }
}
