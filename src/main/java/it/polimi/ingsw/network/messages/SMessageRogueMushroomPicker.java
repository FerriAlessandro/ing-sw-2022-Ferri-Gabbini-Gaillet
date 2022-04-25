package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.model.enumerations.Characters;

public class SMessageRogueMushroomPicker extends SMessage{

    Characters characterName;

    public SMessageRogueMushroomPicker(Characters characterName){
        super(MessageType.S_ROGUEMUSHROOMPICKER);
        this.characterName = characterName;
    }
}
