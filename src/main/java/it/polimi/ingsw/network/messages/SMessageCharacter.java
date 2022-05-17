package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.model.CharacterCard;
import it.polimi.ingsw.model.enumerations.Characters;
import it.polimi.ingsw.model.enumerations.Color;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

/**
 * This class represents the message sent to ask the user to pick a character card.
 * @author Alessandro F., A.G. Gaillet
 * @version 1.0
 * @see Characters
 * @see Character
 * @see CharacterCard
 */
public class SMessageCharacter extends SMessage{
    public final ArrayList<Characters> effects = new ArrayList<>();
    public final Map <Characters, Map<Color, Integer>> students = new HashMap<>();
    public final Map <Characters, Integer> cardCost = new HashMap<>();
    public int noEntryTiles;

    /**
     * Constructor.
     * @param characters {@link ArrayList} of available character cards
     */
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
