package it.polimi.ingsw.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import it.polimi.ingsw.model.enumerations.Color;

import java.security.InvalidParameterException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class BagTest {
    Bag bag;
    @BeforeEach
    void setUp(){
        bag = new Bag();
    }

    @Test
    @DisplayName("Test the getStudents method. Ensure all students are eventually returned and in the correct number")
    void getStudentsAll() {
        ArrayList<Color> value = new ArrayList<>(bag.getStudents(26 * Color.values().length));
        for(Color color:Color.values()) {
            assertEquals(26, value.stream()
                    .filter(x -> x.equals(color))
                    .count());
        }
        ArrayList<Color> other = new ArrayList<>(bag.getStudents(1));
        assertEquals(0, other.size());
    }

    @Test
    @DisplayName("Test the getStudents method. Ensure compliance in corner case (more students required than available)")
    void getStudentsLessThanRequested(){
        bag.getStudents(26 * Color.values().length - 1);
        ArrayList<Color> checkValue = new ArrayList<>(bag.getStudents(2));
        assertEquals(1, checkValue.size());
    }

    @Test
    @DisplayName("Test the numRemaining method")
    void numRemaining() {
        bag.getStudents(50);
        ArrayList<Color> checkValue = new ArrayList<>(bag.getStudents(26 * Color.values().length));
        assertEquals(26 * Color.values().length - 50, checkValue.size());
    }

    @Test
    @DisplayName("Test the numRemaining method. Ensure compliance with corner case: no students remaining")
    void NumRemainingCorner() {
        bag.getStudents(26 * Color.values().length);
        int checkValue = bag.numRemaining();
        assertEquals(0, checkValue);
    }

    @Test
    @DisplayName("Test the addStudents method. Ensure general compliance")
    void addStudents() {
        bag.getStudents(26 * Color.values().length);
        for (Color color: Color.values()) {
            bag.addStudents(2, color);
            assertEquals(2, bag.numRemaining(color));
        }
    }

    @Test
    @DisplayName("Test the addStudent method. Ensure compliance with corner case that throws an exception")
    void addStudentsCorner(){
        for(Color color : Color.values()){
            InvalidParameterException exc = new InvalidParameterException();
            assertThrowsExactly(exc.getClass(), () -> bag.addStudents(1, color));
        }
    }
}