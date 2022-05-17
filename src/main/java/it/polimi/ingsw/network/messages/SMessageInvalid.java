package it.polimi.ingsw.network.messages;

/**
 * This class represents a generic text message, usually used to communicate errors.
 * @author Alessandro F.
 * @version 1.0
 */
public class SMessageInvalid extends SMessage{

    public final String error;

    /**
     * Constructor
     * @param error {@link String} text message
     */
    public SMessageInvalid(String error){
        super(MessageType.S_INVALID);
        this.error = error;
    }

}
