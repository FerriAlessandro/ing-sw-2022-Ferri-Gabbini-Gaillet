package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.model.enumerations.Characters;

/**
 * This Class represents the Message sent by the Controller asking for the parameters needed for the Grandma Herb Character Card activation
 * @author Alessandro F.
 * @version 1.0
 */

public class SMessageGrandmaherbHerald extends SMessage{

    public Characters characterName;

    /**
     * Constructor
     * @param characterName of the {@link Character}
     */
    public SMessageGrandmaherbHerald(Characters characterName){
        super(MessageType.S_GRANDMAHERBHERALD);
        this.characterName = characterName;
    }

}
