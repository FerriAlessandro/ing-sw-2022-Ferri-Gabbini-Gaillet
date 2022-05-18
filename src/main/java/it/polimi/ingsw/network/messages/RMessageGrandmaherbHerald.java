package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.model.enumerations.Characters;

/**
 * This Class represents the Message received by the Controller containing the parameters needed for the Grandma Herb Character Card's activation
 * @author Alessandro F.
 * @version 1.0
 */

public class RMessageGrandmaherbHerald extends Message{

    public final int islandIndex;
    public final Characters characterName;
    public final String nickName;

    /**
     * Constructor.
     * @param characterName Name of the Character Card
     * @param nickName NickName of the player
     * @param islandIndex Index of the Island on which to place the no entry tile
     */
    public RMessageGrandmaherbHerald(Characters characterName, String nickName, int islandIndex){

        this.type = MessageType.R_GRANDMAHERBHERALD;
        this.characterName = characterName;
        this.nickName = nickName;
        this.islandIndex = islandIndex;

    }

}
