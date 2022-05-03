package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.model.enumerations.Characters;
import it.polimi.ingsw.model.enumerations.Color;

/**
 * This Class represents the Message received by the Controller containing the parameters needed for the Monk/Spoiled_Princess/Rogue Character Cards activation
 * @author Alessandro F.
 * @version 1.0
 */

public class RMessageMonkPrincess extends Message{

    public Color chosenColor;
    public Characters characterName;
    public String nickName;
    public int islandIndex;

    /**
     * Constructor
     * @param color The Color of the student to move (case Monk/Princess) or the color of the student to remove from each Dining Room (case Rogue)
     * @param characterName Name of the Character Card
     * @param nickName NickName of the Player
     * @param islandIndex Index of the Island on which to move the chosen Student (Case Monk)
     */
    public RMessageMonkPrincess(Characters characterName, String nickName, int islandIndex, Color color){

        this.type = MessageType.R_MONKPRINCESS;
        this.chosenColor = color;
        this.characterName = characterName;
        this.nickName = nickName;
        this.islandIndex = islandIndex;
    }
}
