package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.model.enumerations.Characters;

/**
 * This class represents the message sent by the server to ask for the parameters necessary to activate the
 * {@link Characters#MUSHROOM_PICKER} or {@link Characters#ROGUE} character cards.
 * @author Alessandro F.
 * @version 1.0
 */
public class SMessageRogueMushroomPicker extends SMessage{

    public Characters characterName;

    /**
     * Constructor
     * @param characterName of the {@link Character}
     */
    public SMessageRogueMushroomPicker(Characters characterName){
        super(MessageType.S_ROGUEMUSHROOMPICKER);
        this.characterName = characterName;
    }
}
