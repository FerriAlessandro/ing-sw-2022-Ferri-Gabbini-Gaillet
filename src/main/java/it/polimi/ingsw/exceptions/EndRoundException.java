package it.polimi.ingsw.exceptions;

/**
 * Exception thrown when the round is over and a new round has to start.
 * @author Alessandro F.
 * @version 1.0
 */
public class EndRoundException extends Exception{
    /**
     * Constructor.
     */
    public EndRoundException(){
        super("Round over, prepare next Round");
    }
}
