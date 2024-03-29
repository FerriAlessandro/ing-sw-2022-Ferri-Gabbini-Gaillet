package it.polimi.ingsw.network.messages;
import it.polimi.ingsw.model.enumerations.Characters;
import it.polimi.ingsw.model.enumerations.Color;
import java.util.EnumMap;

/**
 * This Class represents the Message sent by the Controller asking for the parameters needed for the Monk/Spoiled_princess/Rogue Character Cards activation
 * @author Alessandro F.
 * @version 1.0
 */

public class SMessageMonkPrincess extends SMessage{
    /** Character choice that originated this message */
    public final Characters characterName;

    /** The color of the student to remove from the card and add to the destination (case Monk/Princess) or the color of the student to remove from each
     *               player's Dining Room (case Rogue). */
    public final EnumMap <Color, Integer> colors;

    /**
     * Constructor
     * @param colors The color of the student to remove from the card and add to the destination (case Monk/Princess) or the color of the student to remove from each
     *               player's Dining Room (case Rogue)
     * @param characterName Name of the Character Card
     */
    public SMessageMonkPrincess(EnumMap<Color, Integer> colors, Characters characterName){
        super(MessageType.S_MONKPRINCESS);
        this.colors = new EnumMap<>(colors);
        this.characterName = characterName;
    }
}
