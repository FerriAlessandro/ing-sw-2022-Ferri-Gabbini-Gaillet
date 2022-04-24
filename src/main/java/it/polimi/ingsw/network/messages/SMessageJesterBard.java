package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.model.enumerations.Characters;
import it.polimi.ingsw.model.enumerations.Color;

import java.util.EnumMap;

public class SMessageJesterBard extends SMessage{

    public EnumMap<Color, Integer> origin;
    public EnumMap<Color, Integer> entrance;
    public int maxStudents;
    public Characters characterName;

    public SMessageJesterBard(EnumMap<Color, Integer> origin, EnumMap<Color, Integer> entrance, Characters characterName){

        super(MessageType.S_JESTERBARD);
        this.origin = origin; //If it's the Jester the origin is the card, if it's the Bard the origin is the Player's Dining Room
        this.entrance = entrance;
        this.characterName = characterName;
        if(characterName.equals(Characters.JESTER))
            this.maxStudents = 3;
        else this.maxStudents = 2;
    }
}
