package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.TowerWinException;
import it.polimi.ingsw.model.enumerations.TowerColor;
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
    void setup() {
        assertDoesNotThrow(() -> this.towerZone = new TowerZone(2, TowerColor.WHITE));
    }

    @Test
    @DisplayName("Test the normal operation of the constructor in another scenario")
    void constructorTest1() {
        assertDoesNotThrow(() -> this.towerZone = new TowerZone(3, TowerColor.BLACK));
    }

    @Test
    @DisplayName("Test the normal operation of the constructor in another scenario")
    void constructorTest2() {
        assertDoesNotThrow(() -> this.towerZone = new TowerZone(3, TowerColor.GRAY));
    }

    @Test
    @DisplayName("Test if IllegalArgumentException is thrown properly when numOfPlayer is an unacceptable")
    void constructorExc1Test() {
        IllegalArgumentException e = new IllegalArgumentException();
        assertThrowsExactly(e.getClass(), () -> towerZone = new TowerZone(1, TowerColor.BLACK));
    }

    @Test
    @DisplayName("Test if IllegalArgumentException is thrown properly when towerColor is an unacceptable value")
    void constructorExc2Test() {
        IllegalArgumentException e = new IllegalArgumentException();
        assertThrowsExactly(e.getClass(), () -> towerZone = new TowerZone(2, TowerColor.NONE));
    }

    @Test
    @DisplayName("Test if IllegalArgumentException is thrown properly when towerColor is incompatible with numOfPlayer")
    void constructorExc3Test() {
        IllegalArgumentException e = new IllegalArgumentException();
        assertThrowsExactly(e.getClass(), () -> towerZone = new TowerZone(2, TowerColor.GRAY));
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
    void removeTest() {
        int valueToCheck = towerZone.getNumOfTowers();
        try {
            towerZone.remove();
        }catch (Exception e){
            fail();
        }
        assertEquals(valueToCheck - 1, towerZone.getNumOfTowers());
    }

    @Test
    @DisplayName("Test if RunTimeException and TowerWinException are thrown properly")
    public void removeTestExcTest() {
        for(int i = 0, j = towerZone.getNumOfTowers() - 1; i < j; i++) {
            try {
                towerZone.remove();
            } catch (Exception e) {
                e.printStackTrace();
                fail();
            }
        }
        assertThrows(TowerWinException.class, () -> towerZone.remove());
        RuntimeException e = new RuntimeException();
        assertThrowsExactly(e.getClass(), () -> towerZone.remove());
    }
}