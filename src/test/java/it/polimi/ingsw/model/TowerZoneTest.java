package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.TowerWinException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.security.spec.ECField;

import static org.junit.jupiter.api.Assertions.*;

class TowerZoneTest {
    TowerZone towerZone;

    @BeforeEach
    @Test
    @DisplayName("Test the normal operation of the constructor")
    void constructorTest() {
        assertDoesNotThrow(() -> this.towerZone = new TowerZone(2));
        assertDoesNotThrow(() -> this.towerZone = new TowerZone(3));
    }

    @Test
    @DisplayName("Test if IllegalArgumentException is thrown properly when numOfPlayer is an unacceptable")
    void constructorExcTest() {
        IllegalArgumentException e = new IllegalArgumentException();
        assertThrowsExactly(e.getClass(), () -> towerZone = new TowerZone(1));
    }

    @Test
    @DisplayName("Test the normal operation of add method")
    void addTest() {
        int valueToCheck = towerZone.getNumOfTowers();
        towerZone.add();
        assertEquals(valueToCheck + 1, towerZone.getNumOfTowers());
    }

    @Test
    @DisplayName("Test the normal operation of remove method")
    void removeTest() throws Exception {
        int valueToCheck = towerZone.getNumOfTowers();
        towerZone.remove();
        assertEquals(valueToCheck - 1, towerZone.getNumOfTowers());
    }

    @Test
    @DisplayName("Test if TowerWinException is thrown properly when after remove remains 0 tower")
    public void removeExc1Test() throws Exception {
        for(int i = 0, j = towerZone.getNumOfTowers(); i < j - 1; i++)
            towerZone.remove();
        RuntimeException e = new RuntimeException();
        assertThrowsExactly(TowerWinException.class, () -> towerZone.remove());
    }

}