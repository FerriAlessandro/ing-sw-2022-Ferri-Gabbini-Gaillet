package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.model.enumerations.Characters;
import it.polimi.ingsw.model.enumerations.Color;

import java.util.EnumMap;

public class SMessageMonkPrincessRogue extends SMessage{

    public Characters characterName;
    public EnumMap <Color, Integer> colors;

    public SMessageMonkPrincessRogue(EnumMap<Color, Integer> colors, Characters characterName){
        super(MessageType.S_MONKPRINCESSROGUE);
        this.colors = new EnumMap<>(colors);
        this.characterName = characterName;
    }
}
