package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enumerations.TowerColor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IslandTileTest {
    IslandTile islandTile;
    @BeforeEach
    void setup() {
        this.islandTile = new IslandTile();
    }
    @Test
    @DisplayName("Test the normal operation of method swapTower")
    void swapTowerTest() {
        islandTile.swapTower(TowerColor.BLACK);
        assertEquals(TowerColor.BLACK, islandTile.getTowerColor());
    }

    @Test
    @DisplayName("Test the corner case of swapTower when the param is NONE")
    void swapTowerNONETest() {
        IllegalArgumentException e = new IllegalArgumentException();
        assertThrowsExactly(e.getClass(), () -> this.islandTile.swapTower(TowerColor.NONE));
    }

    @Test
    @DisplayName("Test the corner case of swapTower when the param is the same of the actual one")
    void swapTowerSAMETest() {
        IllegalArgumentException e = new IllegalArgumentException();
        islandTile.swapTower(TowerColor.BLACK);
        assertThrowsExactly(e.getClass(), () -> islandTile.swapTower(TowerColor.BLACK));
    }
    @Test
    @DisplayName("Test the normal operation of method mergeTower")
    void mergeTowerTest() {
        islandTile.mergeTower();
        assertEquals(1, islandTile.getNumTowers());
    }

    @Test
    @DisplayName("Test the normal operation of method addNoEntry")
    void addNoEntryTest() {
        islandTile.addNoEntry();
        assertEquals(1, islandTile.getNumOfNoEntryTiles());
    }

    @Test
    @DisplayName("Test the normal operation of method removeNoEntry")
    void removeNoEntryTest() {
        islandTile.addNoEntry();
        islandTile.removeNoEntry();
        assertEquals(0, islandTile.getNumOfNoEntryTiles());
    }

    @Test
    @DisplayName("Test the corner case of removeNoEntry when there aren't noEntryTile")
    void removeNoEntryAbsentTest() {
        RuntimeException e = new RuntimeException();
        assertThrowsExactly(e.getClass(), () -> islandTile.removeNoEntry());
    }

    @Test
    @DisplayName("Test the normal operation of isForbidden method")
    void isForbiddenFalseTest() {
        assertFalse(islandTile.isForbidden());
    }

    @Test
    @DisplayName("Test the normal operation of isForbidden method")
    void isForbiddenTrueTest() {
        islandTile.addNoEntry();
        assertTrue(islandTile.isForbidden());
    }
}