package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.model.enumerations.Characters;
import it.polimi.ingsw.model.enumerations.Color;

/**
 * This Class represents the Message received by the Controller containing the parameters needed for the Monk/Spoiled_Princess/Rogue Character Cards activation
 * @author Alessandro F.
 * @version 1.0
 */

public class RMessageMonkPrincess extends RMessage{
    /** The chosen {@link Color}*/
    public final Color chosenColor;
    /** The character choice which originated this message*/
    public final Characters characterName;
    /** The index of the chosen {@link it.polimi.ingsw.model.IslandTile}*/
    public final int islandIndex;

    /**
     * Constructor
     * @param color The Color of the student to move (case Monk/Princess) or the color of the student to remove from each Dining Room (case Rogue)
     * @param characterName Name of the Character Card
     * @param nickname NickName of the Player
     * @param islandIndex Index of the Island on which to move the chosen Student (Case Monk)
     */
    public RMessageMonkPrincess(Characters characterName, String nickname, int islandIndex, Color color){

        this.type = MessageType.R_MONKPRINCESS;
        this.chosenColor = color;
        this.characterName = characterName;
        this.nickname = nickname;
        this.islandIndex = islandIndex;
    }

}
