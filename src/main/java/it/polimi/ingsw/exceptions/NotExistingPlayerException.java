package it.polimi.ingsw.exceptions;

/**
 * Thrown when a nickname is not available in a loaded save.
 * @author A.G. Gaillet
 * @version 1.1
 */
public class NotExistingPlayerException extends Exception{
    /**
     * Constructor.
     */
    public NotExistingPlayerException(){ super("No pre-existing player had this nickname"); }
}
