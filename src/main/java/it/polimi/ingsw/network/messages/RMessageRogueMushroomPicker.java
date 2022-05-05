package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.model.enumerations.Characters;
import it.polimi.ingsw.model.enumerations.Color;

public class RMessageRogueMushroomPicker extends Message{

    public Color chosenColor;
    public Characters characterName;
    public String nickName;

    public RMessageRogueMushroomPicker(Characters characterName, String nickName, Color color){
        this.type = MessageType.R_ROGUEMUSHROOMPICKER;
        this.characterName = characterName;
        this.nickName = nickName;
        this.chosenColor = color;
    }

    public Color getChosenColor() {
        return chosenColor;
    }

    public Characters getCharacterName() {
        return characterName;
    }
}
