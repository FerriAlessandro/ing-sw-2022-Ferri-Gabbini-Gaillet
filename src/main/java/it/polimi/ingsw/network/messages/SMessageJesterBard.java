package it.polimi.ingsw.network.messages;
import it.polimi.ingsw.model.enumerations.Characters;
import it.polimi.ingsw.model.enumerations.Color;
import java.util.EnumMap;
/**
 * This Class represents the Message sent by the Controller asking for the parameters needed for the Jester/Bard Character Cards activation
 * @author Alessandro F.
 * @version 1.0
 */

public class SMessageJesterBard extends SMessage{

    public final EnumMap<Color, Integer> origin;
    public final EnumMap<Color, Integer> entrance;
    public final int maxStudents;
    public final Characters characterName;

    /**
     * Constructor
     * @param origin The Jester card itself if the played card is Jester, the Dining Room if the played card is Bard
     * @param entrance The player's Entrance
     * @param characterName Name of the Character Card
     */
    public SMessageJesterBard(EnumMap<Color, Integer> origin, EnumMap<Color, Integer> entrance, Characters characterName){
        super(MessageType.S_JESTERBARD);
        this.origin = origin; //If it's the Jester the origin is the card, if it's the Bard the origin is the Player's Dining Room
        this.entrance = entrance;
        this.characterName = characterName;
        if(characterName.equals(Characters.JESTER))
            this.maxStudents = 3;
        else this.maxStudents = 2;
    }

}
