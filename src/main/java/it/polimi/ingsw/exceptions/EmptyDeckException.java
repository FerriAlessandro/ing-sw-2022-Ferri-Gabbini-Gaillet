package it.polimi.ingsw.exceptions;

/**
 * Exception to throw when a player plays his last card
 * @author Alessandro F.
 * @version 1.0
 */
public class EmptyDeckException extends Exception{

    public EmptyDeckException(String message){
        super(message);
    }
}
