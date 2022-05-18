package it.polimi.ingsw.network.messages;
import it.polimi.ingsw.model.enumerations.Characters;
import it.polimi.ingsw.model.enumerations.Color;
import java.util.ArrayList;

/**
 * This Class represents the Message received by the Controller containing the parameters needed for the Jester/Bard Character Cards activation
 * @author Alessandro F.
 * @version 1.0
 */

public class RMessageJesterBard extends Message{

    public final ArrayList<Color> origin; //This ArrayList contains the students that the user wants to swap FROM THE ORIGIN! If the origin is the Dining Room,
                                    // it contains the students that the user wants to remove from its Dining Room!
    public final ArrayList<Color> entrance;//Same as above
    public final Characters characterName;
    public final String nickName;

    /**
     * Constructor
     * @param origin The students to remove from Jester Card if the played card is Jester, from the Dining Room if the played card is Bard
     * @param entrance The students to remove from the player's Entrance
     * @param characterName Name of the Character card
     * @param nickName NickName of the player
     */
    public RMessageJesterBard(Characters characterName, String nickName, ArrayList<Color> origin, ArrayList<Color> entrance){
        this.type = MessageType.R_JESTERBARD;
        this.origin = new ArrayList<>(origin);
        this.entrance = new ArrayList<>(entrance);
        this.characterName = characterName;
        this.nickName = nickName;

    }

}
