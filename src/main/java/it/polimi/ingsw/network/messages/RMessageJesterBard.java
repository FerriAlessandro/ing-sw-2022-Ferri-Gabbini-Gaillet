package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.model.enumerations.Characters;
import it.polimi.ingsw.model.enumerations.Color;

import java.util.ArrayList;

public class RMessageJesterBard extends Message{

    public ArrayList<Color> origin;
    public ArrayList<Color> entrance;
    public Characters characterName;
    public String nickName;

    public RMessageJesterBard(ArrayList<Color> origin, ArrayList<Color> entrance, Characters characterName, String nickName){

        this.type = MessageType.R_JESTERBARD;
        this.origin = new ArrayList<>(origin);
        this.entrance = new ArrayList<>(entrance);
        this.characterName = characterName;
        this.nickName = nickName;


    }
}
