package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.CloudNotFullException;
import it.polimi.ingsw.model.enumerations.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Method addStudent, removeStudent and other method inherited from TileWithStudents won't be tested because
 * they've already been tested in EntranceTest, the operation is absolutely the same.
 */

class CloudTileTest {
    CloudTile cloud;

    /**
     * Utility function to test quickly removeAllStudents method.
     */
    public int numOfColor(List<Color> listOfColor, Color color) {
        int numOfCol = 0;
        for(Color c : listOfColor) {
            if(c.equals(color))
                numOfCol ++;
        }
        return numOfCol;
    }

    @BeforeEach
    void setUp() {
        this.cloud = new CloudTile(2);
    }

    @Test
    @DisplayName("Test the constructor method with illegal parameter")
    void cloudTileConstructorTest() {
        IllegalArgumentException e = new IllegalArgumentException();
        assertThrowsExactly(e.getClass(), () -> this.cloud = new CloudTile(5));
    }

    @Test
    @DisplayName("Test the normal operation of method removeAllStudents")
    void removeAllStudentsTest() {
        cloud.addStudent(Color.YELLOW);
        cloud.addStudent(Color.GREEN);
        cloud.addStudent(Color.YELLOW);
        try {
            List<Color> studentsReturned = cloud.removeAllStudents();
            int numOfYellow = numOfColor(studentsReturned, Color.YELLOW);
            int numOfGreen = numOfColor(studentsReturned, Color.GREEN);
            assertEquals(2, numOfYellow);
            assertEquals(1, numOfGreen);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("Test if removeAllStudents throws correctly the Exception")
    void removeAllStudentsExcTest() {
        cloud.addStudent(Color.YELLOW);
        try {
            ArrayList<Color> list = cloud.removeAllStudents();
            assertEquals(1, list.size());
            assertEquals(Color.YELLOW, list.get(0));
        }catch (CloudNotFullException e){
            fail();
        }
        try {
            cloud.removeStudent(Color.YELLOW);
        }catch (Exception e){
            fail();
        }
        assertThrows(CloudNotFullException.class, () -> cloud.removeAllStudents());
    }
    
}
