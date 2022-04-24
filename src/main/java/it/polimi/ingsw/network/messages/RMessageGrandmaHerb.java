package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.model.enumerations.Characters;

/**
 * This Class represents the Message received by the Controller containing the parameters needed for the Grandma Herb Character Card's activation
 * @author Alessandro F.
 * @version 1.0
 */

public class RMessageGrandmaHerb extends Message{

    public int islandIndex;
    public Characters characterName;
    public String nickName;

    /**
     * Constructor
     * @param characterName Name of the Character Card
     * @param nickName NickName of the player
     * @param islandIndex Index of the Island on which to place the no entry tile
     */
    public RMessageGrandmaHerb(Characters characterName, String nickName, int islandIndex){

        this.type = MessageType.R_GRANDMAHERB;
        this.characterName = characterName;
        this.nickName = nickName;
        this.islandIndex = islandIndex;

    }
}
