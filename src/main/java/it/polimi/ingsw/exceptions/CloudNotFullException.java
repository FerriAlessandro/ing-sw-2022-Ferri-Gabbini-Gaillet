package it.polimi.ingsw.exceptions;

/**
 * Custom exception that occurs when a cloud is not full and removeAllStudents is invoked.
 */
public class CloudNotFullException extends Exception {
    /**
     * Constructor.
     */
    public CloudNotFullException() {
        super("The cloud is not full!");
    }
}
