package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.FullDestinationException;
import it.polimi.ingsw.model.enumerations.Color;
import java.util.EnumMap;
import java.util.Map;

/**
 * This abstract class provides methods and attributes that every other tile with students has.
 * @author AlessandroG
 * @version 1.1
 */

public abstract class TileWithStudents {

    // students is a map that associates the number of students on the tile with the correspondent color
    private final EnumMap<Color, Integer> students;

    /**
     * Constructor simply initializes the map's Color to 0.
     */
    public TileWithStudents() {
        this.students = new EnumMap<>(Color.class);
        for (Color color : Color.values())
            students.put(color, 0);
    }

    public int getNumStudents(Color color) {
        return students.get(color);
    }

    /**
     * @return the total of the students on the island
     */
    public int getNumStudents() {
        int totalStudents = 0;
        for (Color color : Color.values())
            totalStudents += getNumStudents(color);
        return totalStudents;
    }

    /**
     * Add one student of the color received by the caller, to the tile.
     */
    public void addStudent(Color color) throws FullDestinationException {
        students.put(color, (students.get(color)) + 1);
    }

    /**
     * Method for removing a student (any purpose).
     *
     * @throws RuntimeException when trying to remove a student even if he's not present
     */
    public void removeStudent(Color color) throws RuntimeException {
        if (students.get(color) == 0)
            throw new RuntimeException("There are no students of this color");
        students.put(color, (students.get(color)) - 1);
    }

    /**
     * Getter for the internal map of students.
     * @return the reference to the memorized map.
     */
    public Map<Color, Integer> getState(){
        return students;
    }
}

