package it.polimi.ingsw.network.messages;
import it.polimi.ingsw.model.enumerations.Characters;
import it.polimi.ingsw.model.enumerations.Color;
import java.util.ArrayList;

/**
 * This Class represents the Message received by the Controller containing the parameters needed for the Jester/Bard Character Cards activation
 * @author Alessandro F.
 * @version 1.0
 */

public class RMessageJesterBard extends RMessage{

    /** This ArrayList contains the students that the user wants to swap FROM THE ORIGIN! If the origin is the Dining Room,
        it contains the students that the user wants to remove from its Dining Room!*/
    public final ArrayList<Color> origin;
    /** This ArrayList contains the students that the user wants to remove (swap) from the entrance*/
    public final ArrayList<Color> entrance;
    /** The character choice which originated this message*/
    public final Characters characterName;

    /**
     * Constructor
     * @param origin The students to remove from Jester Card if the played card is Jester, from the Dining Room if the played card is Bard
     * @param entrance The students to remove from the player's Entrance
     * @param characterName Name of the Character card
     * @param nickname NickName of the player
     */
    public RMessageJesterBard(Characters characterName, String nickname, ArrayList<Color> origin, ArrayList<Color> entrance){
        this.type = MessageType.R_JESTERBARD;
        this.origin = new ArrayList<>(origin);
        this.entrance = new ArrayList<>(entrance);
        this.characterName = characterName;
        this.nickname = nickname;

    }

}
