package it.polimi.ingsw.exceptions;

/**
 * Thrown when a nickname is not available in a loaded save.
 */
public class NotExistingPlayerException extends Exception{
    public NotExistingPlayerException(){ super("This nickname doesn't "); }
}
