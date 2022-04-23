package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.model.enumerations.Characters;
import it.polimi.ingsw.model.enumerations.Color;

public class RMessageMonkPrincessRogue extends Message{

    public Color chosenColor;
    public Characters characterName;
    public String nickName;
    public int islandIndex;

    public RMessageMonkPrincessRogue(Color color, Characters name, String nickName, int islandIndex){

        this.type = MessageType.R_MONKPRINCESSROGUE;
        this.chosenColor = color;
        this.characterName = name;
        this.nickName = nickName;
        this.islandIndex = islandIndex;
    }
}
