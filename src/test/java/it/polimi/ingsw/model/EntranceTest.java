package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enumerations.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EntranceTest {
    Entrance entrance;
    @BeforeEach
    void setup() {
        this.entrance = new Entrance(9);
    }
    @Test
    @DisplayName("Tests a simple case of removing a student")
    void removeStudentTest() {
        for(Color color : Color.values())
            entrance.addStudent(color);
        for(Color color : Color.values())
            entrance.removeStudent(color);
        for(Color color : Color.values())
            assertEquals(0, entrance.getNumStudent(color));
    }

    @Test
    @DisplayName("Tests the corner case of attempting to remove a student even though he's not present")
    void removeStudentCornerTest() {
        for(Color color : Color.values()){
            RuntimeException e = new RuntimeException();
            assertThrowsExactly(e.getClass(), () -> entrance.removeStudent(color));
        }
    }
    @Test
    @DisplayName("Tests a simple case of adding a student")
    void AddStudentTest() {
        for(Color color : Color.values())
            entrance.addStudent(color);
        for(Color color : Color.values())
            assertEquals(1, entrance.getNumStudent(color));
    }
    @Test
    @DisplayName("Tests the corner case of attempting to add a student even though the entrance is full")
    void addStudentCornerTest() {
        for(int i = 0; i < 9; i++)
            entrance.addStudent(Color.GREEN);
            RuntimeException e = new RuntimeException();
            assertThrowsExactly(e.getClass(), () -> entrance.addStudent(Color.GREEN));
    }

}