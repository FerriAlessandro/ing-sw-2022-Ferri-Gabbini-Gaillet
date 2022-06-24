package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.FullDestinationException;
import it.polimi.ingsw.model.enumerations.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EntranceTest {
    Entrance entrance;

    @BeforeEach
    void setup() {
        entrance = new Entrance(2);
    }

    @Test
    @DisplayName("Test normal operation of constructor method")
    void constructorTest() {
        assertDoesNotThrow(() -> entrance = new Entrance(3));
    }

    @Test
    @DisplayName("Test the constructor method with illegal parameter")
    void entranceConstructorTest() {
        assertThrowsExactly(IllegalArgumentException.class, () -> entrance = new Entrance(1));
    }

    @Test
    @DisplayName("Test a simple case of removeStudent")
    void removeStudentTest() throws Exception {
        for(Color color : Color.values())
            entrance.addStudent(color);
        for(Color color : Color.values())
            entrance.removeStudent(color);
        for(Color color : Color.values())
            assertEquals(0, entrance.getNumStudents(color));
    }

    @Test
    @DisplayName("Test the corner case of removeStudent when when there are no students.")
    void removeStudentCornerTest() {
        for(Color color : Color.values()){
            assertThrows(RuntimeException.class, () -> entrance.removeStudent(color));
        }
    }
    @Test
    @DisplayName("Test a simple case of adding a student")
    void AddStudentTest() throws FullDestinationException{
        for(Color color : Color.values())
            entrance.addStudent(color);
        for(Color color : Color.values())
            assertEquals(1, entrance.getNumStudents(color));
    }
    @Test
    @DisplayName("Test the corner case of attempting to add a student even though the entrance is full")
    void addStudentCornerTest() throws FullDestinationException {
        for(int i = 0; i < entrance.getMaxStudents(); i++)
            entrance.addStudent(Color.GREEN);
        assertThrowsExactly(FullDestinationException.class, () -> entrance.addStudent(Color.GREEN));
    }

}