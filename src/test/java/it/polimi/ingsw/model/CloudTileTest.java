package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.CloudNotFullException;
import it.polimi.ingsw.exceptions.FullDestinationException;
import it.polimi.ingsw.model.enumerations.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Method removeStudent inherited from TileWithStudents won't be tested because
 * it's already been tested in EntranceTest, the operation is absolutely the same.
 */

class CloudTileTest {
    CloudTile cloud;

    /**
     * Utility function to test quickly removeAllStudents method.
     */
    public int numOfColor(List<Color> listOfColor, Color color) {
        int numOfCol = 0;
        for(Color c : listOfColor) {
            if(c == color)
                numOfCol ++;
        }
        return numOfCol;
    }

    @BeforeEach
    void setUp() {
        this.cloud = new CloudTile(2);
    }

    @Test
    @DisplayName("Test normal operation of constructor method")
    void constructorTest() {
        assertDoesNotThrow(() -> this.cloud = new CloudTile(3));
    }

    @Test
    @DisplayName("Test the constructor method with illegal parameter")
    void cloudTileConstructorTest() {
        IllegalArgumentException e = new IllegalArgumentException();
        assertThrowsExactly(e.getClass(), () -> this.cloud = new CloudTile(5));
    }

    @Test
    @DisplayName("Test the normal operation of addStudent method")
    void addStudentTest() throws FullDestinationException {
        cloud.addStudent(Color.GREEN);
        assertEquals(1, cloud.getNumStudents());
    }

    @Test
    @DisplayName("Test if method addStudent throws FullDestinationException properly")
    void addStudentExcTest() throws FullDestinationException {
        for(int i = 0; i < cloud.getMaxStudents(); i++)
            cloud.addStudent(Color.GREEN);
        FullDestinationException e = new FullDestinationException();
        assertThrowsExactly(e.getClass(), () -> cloud.addStudent(Color.GREEN));
    }

    @Test
    @DisplayName("Test the normal operation of method removeAllStudents")
    void removeAllStudentsTest() throws Exception {
        cloud.addStudent(Color.YELLOW);
        cloud.addStudent(Color.GREEN);
        cloud.addStudent(Color.YELLOW);
        List<Color> studentsReturned = cloud.removeAllStudents();
        int numOfYellow = numOfColor(studentsReturned, Color.YELLOW);
        int numOfGreen = numOfColor(studentsReturned, Color.GREEN);
        assertEquals(2, numOfYellow);
        assertEquals(1, numOfGreen);
    }

    @Test
    @DisplayName("Test if removeAllStudents throws correctly the Exception")
    void removeAllStudentsExcTest() throws FullDestinationException{
        cloud.addStudent(Color.YELLOW);
        assertThrows(CloudNotFullException.class, () -> cloud.removeAllStudents());
    }
    
}
