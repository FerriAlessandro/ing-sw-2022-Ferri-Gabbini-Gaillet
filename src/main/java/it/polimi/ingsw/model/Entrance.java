package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enumerations.*;

/**
 * @author AlessandroG
 * @version 1.0
 * This class hosts the students extracted from the bag. It's part of the player board.
 */
public class Entrance extends TileWithStudents{
    // the number of students in the entrance changes with the players number. 7 for 2 players, 9 for 3 players.
    private final int maxStudents;

    /**
     * Constructor needed because an entrance has a fixed maximum number of students.
     * @param maxStudents 7 for 2 players-game, 9 for 3 players-game
     */
    public Entrance(int maxStudents) {
        super();
        this.maxStudents = maxStudents;
    }

    /**
     * The function is overridden because the entrance can't have more than 7 or 9 students per color.
     * @throws RuntimeException when the entrance is full and someone tries to add a student.
     */
    @Override
    public void addStudent(Color color) {
        int currentNumOfStudents = 0;
        for(Color color1 : Color.values()) {
            currentNumOfStudents += getNumStudent(color1);
        }
        if(currentNumOfStudents == maxStudents)
            throw new RuntimeException("The entrance is already full");
        super.addStudent(color);
    }
}
