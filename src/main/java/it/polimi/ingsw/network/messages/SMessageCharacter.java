package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.model.CharacterCard;
import it.polimi.ingsw.model.enumerations.Characters;
import it.polimi.ingsw.model.enumerations.Color;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

public class SMessageCharacter extends SMessage{
    public ArrayList<Characters> effects = new ArrayList<>();
    public Map <Characters, Map<Color, Integer>> students = new HashMap<>();
    public Map <Characters, Integer> cardCost = new HashMap<>();
    public int noEntryTiles;


    public SMessageCharacter(ArrayList<CharacterCard> characters) {

        super(MessageType.S_CHARACTER);
        for (CharacterCard characterCard : characters) {
            effects.add(characterCard.getName());
            students.put(characterCard.getName(), new EnumMap<>(characterCard.getState())); //Copy of the current Map
            cardCost.put(characterCard.getName(), characterCard.getCost());
            if(characterCard.getName().equals(Characters.GRANDMA_HERB))
                noEntryTiles = characterCard.getNoEntryTiles();
        }
    }
}
