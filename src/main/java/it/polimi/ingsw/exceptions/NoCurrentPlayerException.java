package it.polimi.ingsw.exceptions;

/**
 * Exception thrown if it's nobody's turn.
 * @author Alessandro F.
 * @version 1.0
 */

public class NoCurrentPlayerException extends Exception{

    public NoCurrentPlayerException(){

        super("ERROR! There's not a current player");
    }
}
