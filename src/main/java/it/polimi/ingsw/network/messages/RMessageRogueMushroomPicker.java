package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.model.enumerations.Characters;
import it.polimi.ingsw.model.enumerations.Color;

/**
 * This class represents the message received by the server and containing the parameters necessary to activate the
 * {@link Characters#MUSHROOM_PICKER} or {@link Characters#ROGUE} character cards.
 * @author Alessandro F.
 * @version 1.0
 */
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

}
