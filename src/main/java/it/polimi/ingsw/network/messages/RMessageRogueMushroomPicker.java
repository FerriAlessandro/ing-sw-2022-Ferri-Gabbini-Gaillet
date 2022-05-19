package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.model.enumerations.Characters;
import it.polimi.ingsw.model.enumerations.Color;

/**
 * This class represents the message received by the server and containing the parameters necessary to activate the
 * {@link Characters#MUSHROOM_PICKER} or {@link Characters#ROGUE} character cards.
 * @author Alessandro F.
 * @version 1.0
 */
public class RMessageRogueMushroomPicker extends RMessage{
    /** The chosen {@link Color}*/
    public final Color chosenColor;
    /** The character choice which originated this message*/
    public final Characters characterName;

    /**
     * Constructor
     * @param characterName either {@link Characters#MUSHROOM_PICKER} or {@link Characters#ROGUE}
     * @param nickname of the player
     * @param color of students on which the effect is applied
     */
    public RMessageRogueMushroomPicker(Characters characterName, String nickname, Color color){
        this.type = MessageType.R_ROGUEMUSHROOMPICKER;
        this.characterName = characterName;
        this.nickname = nickname;
        this.chosenColor = color;
    }

}
