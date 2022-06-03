package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.EmptyBagException;
import it.polimi.ingsw.model.enumerations.Characters;
import it.polimi.ingsw.model.enumerations.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

public class CharacterCardTest {

    ArrayList<CharacterCard> characters = new ArrayList<>();

    @BeforeEach
    void setup(){
        characters.add(new CharacterCard(Characters.GRANDMA_HERB));
        characters.add(new CharacterCard(Characters.MONK));
        characters.add(new CharacterCard(Characters.JESTER));
    }

    @Test
    @DisplayName("This test checks if the class works correctly")
    public void generalTest() throws EmptyBagException {

        assertEquals(characters.get(0).getName(), Characters.GRANDMA_HERB);
        assertEquals(characters.get(0).getCost(), Characters.GRANDMA_HERB.getCost());
        characters.get(0).use();
        assertEquals(characters.get(0).getCost(), Characters.GRANDMA_HERB.getCost()+1);

        characters.get(2).setStudents(new Bag());
        assertEquals(characters.get(2).getNumStudents(), 6);

        characters.get(1).setStudents(new Bag());
        assertEquals(characters.get(1).getNumStudents(), 4);

        characters.get(0).removeNoEntryTile();
        assertEquals(characters.get(0).getNoEntryTiles(), 3);

        characters.get(0).addNoEntryTile();
        assertEquals(characters.get(0).getNoEntryTiles(), 4);

        characters.get(0).setActive(true);
        assertTrue(characters.get(0).isActive());

        characters.get(0).setForbiddenColor(Color.RED);
        assertEquals(Color.RED, characters.get(0).getForbiddenColor());

        assertEquals("", Characters.NONE.getEffect());
    }
}
