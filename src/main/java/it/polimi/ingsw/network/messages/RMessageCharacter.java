package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.model.enumerations.Characters;

/**
 * Message received by the server containing a character card choice. If no choice was made the attribute character is null.
 * @author A.G. Gaillet
 * @version 1.0
 */
public class RMessageCharacter extends Message {
    public Characters character;
    public String nickname;

    /**
     * Constructor.
     * @param character chosen character
     * @param nickname of the player
     */
    public RMessageCharacter(Characters character, String nickname){
        this.type = MessageType.R_CHARACTER;
        this.character = character;
        this.nickname = nickname;
    }

    public Characters getCharacter() { return this.character; }
}

