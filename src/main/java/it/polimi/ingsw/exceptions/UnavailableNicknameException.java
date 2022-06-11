package it.polimi.ingsw.exceptions;

/**
 * Custom exception to be thrown when trying to add a player with a nickname that has already been taken.
 * @author A.G. Gaillet
 * @version 1.0
 */
public class UnavailableNicknameException extends Exception{
    /**
     * Constructor.
     */
    public UnavailableNicknameException() {
        super("This nickname has already been taken. Please select another nickname.");
    }
}