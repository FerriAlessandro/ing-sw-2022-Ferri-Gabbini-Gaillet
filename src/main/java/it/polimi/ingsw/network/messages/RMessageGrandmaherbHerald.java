package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.model.enumerations.Characters;

/**
 * This Class represents the Message received by the Controller containing the parameters needed for the Grandma Herb Character Card's activation
 * @author Alessandro F.
 * @version 1.0
 */

public class RMessageGrandmaherbHerald extends RMessage{

    /** Index of the chosen {@link it.polimi.ingsw.model.IslandTile}*/
    public final int islandIndex;
    /** The character choice originating this message*/
    public final Characters characterName;

    /**
     * Constructor.
     * @param characterName Name of the Character Card
     * @param nickname NickName of the player
     * @param islandIndex Index of the Island on which to place the no entry tile
     */
    public RMessageGrandmaherbHerald(Characters characterName, String nickname, int islandIndex){

        this.type = MessageType.R_GRANDMAHERBHERALD;
        this.characterName = characterName;
        this.nickname = nickname;
        this.islandIndex = islandIndex;

    }

}
