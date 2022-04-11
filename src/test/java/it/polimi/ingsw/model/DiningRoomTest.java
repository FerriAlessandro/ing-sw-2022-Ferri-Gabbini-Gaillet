package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.FullDestinationException;
import it.polimi.ingsw.model.enumerations.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DiningRoomTest {
    DiningRoom diningRoom;

    @BeforeEach
    void setup() {
        this.diningRoom = new DiningRoom();
    }

    @Test
    @DisplayName("Test the correct operation of addStudent method")
    void addStudentTest() throws FullDestinationException {
        diningRoom.addStudent(Color.GREEN);
        assertEquals(1, diningRoom.getNumStudents(Color.GREEN));
    }

    @Test
    @DisplayName("Test if method throws exception properly")
    void addStudentExc1Test() throws FullDestinationException {
        for(int i = 0; i < diningRoom.getMaxStudents(); i++) {
            try {diningRoom.addStudent(Color.GREEN);}
            catch(RuntimeException e) {e.printStackTrace();}
        }
        FullDestinationException e = new FullDestinationException();
        assertThrowsExactly(e.getClass(), () -> diningRoom.addStudent(Color.GREEN));
    }

    @Test
    @DisplayName("Test if method throws exception only when it has to")
    void addStudentExc2Test() throws FullDestinationException {
        for(int i = 0; i < diningRoom.getMaxStudents(); i++) {
            try {diningRoom.addStudent(Color.GREEN); }
            catch (RuntimeException e) {e.printStackTrace();}
        }
        assertDoesNotThrow(() -> diningRoom.addStudent(Color.YELLOW));
        assertThrowsExactly(FullDestinationException.class, () -> diningRoom.addStudent(Color.GREEN));
    }

    @Test
    @DisplayName("Test if method throws RunTimeException properly, when a coin must be given")
    void addStudentCoinExcTest() throws Exception {
        diningRoom.addStudent(Color.RED);
        diningRoom.addStudent(Color.RED);
        assertThrowsExactly(RuntimeException.class, () -> diningRoom.addStudent(Color.RED));
    }
}