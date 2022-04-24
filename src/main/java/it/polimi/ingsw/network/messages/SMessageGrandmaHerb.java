package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.model.enumerations.Characters;

/**
 * This Class represents the Message sent by the Controller asking for the parameters needed for the Grandma Herb Character Card activation
 * @author Alessandro F.
 * @version 1.0
 */

public class SMessageGrandmaHerb extends SMessage{

    public Characters characterName;

    /**
     * Constructor
     */

    public SMessageGrandmaHerb(){
        super(MessageType.S_GRANDMAHERB);
        this.characterName = Characters.GRANDMA_HERB;
    }

}
