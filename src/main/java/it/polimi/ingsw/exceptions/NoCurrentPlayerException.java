package it.polimi.ingsw.exceptions;

public class NoCurrentPlayerException extends Exception{
/**
 * Exception thrown if it's nobody's turn.
 * @author Alessandro F.
 * @version 1.0
 */
    public NoCurrentPlayerException(){

        super("ERROR! There's not a current player");
    }
}
