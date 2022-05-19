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
    /** List of available {@link Characters} */
    public final ArrayList<Characters> effects = new ArrayList<>();
    /** Students currently present on each character that has them */
    public final Map <Characters, Map<Color, Integer>> students = new HashMap<>();
    /** Cost in number of coins per character card */
    public final Map <Characters, Integer> cardCost = new HashMap<>();
    /** Number of no-entry-tiles present on the {@link Characters#GRANDMA_HERB} card */
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
