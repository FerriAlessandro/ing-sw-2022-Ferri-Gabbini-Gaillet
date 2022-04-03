package it.polimi.ingsw.exceptions;

/**
 * Thrown when a player won by placing all of its towers
 * @author Alessandro F.
 * @version 1.0
 */
public class TowerWinException extends Exception{

    public TowerWinException(){
        super("There's a Winner!");
    }
    public TowerWinException(String message){super(message);}
}
