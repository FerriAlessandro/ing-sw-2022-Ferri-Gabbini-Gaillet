package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enumerations.TowerColor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class MotherNatureTest {

    @Test
    @DisplayName("Test move method")
    void move() {
        IslandTile isl1 = new IslandTile();
        isl1.swapTower(TowerColor.BLACK);
        IslandTile isl2 = new IslandTile();
        isl2.swapTower(TowerColor.GRAY);
        ArrayList<IslandTile> islands = new ArrayList<>();
        islands.add(isl1);
        islands.add(isl2);
        MotherNature mn = new MotherNature(islands);
        IslandTile currIsl = mn.getCurrentIsland();
        int pos = 3;
        mn.move(pos);
        assertEquals(mn.getCurrentIsland(), islands.get((islands.indexOf(currIsl) + pos)%(islands.size())));
        IslandTile isl3 = new IslandTile();
        isl3.swapTower(TowerColor.WHITE);
        islands.add(isl3);
        mn.move(1);
        IslandTile isl4 = mn.getCurrentIsland();
        mn.move(1);
        IslandTile isl5 = mn.getCurrentIsland();
        assertTrue((isl4.equals(isl3) && currIsl.equals(isl1) )|| (isl5.equals(isl3) && currIsl.equals(isl2)));
    }
}
