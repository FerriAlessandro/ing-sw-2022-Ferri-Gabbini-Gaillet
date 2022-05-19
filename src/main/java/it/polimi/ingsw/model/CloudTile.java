package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.CloudNotFullException;
import it.polimi.ingsw.exceptions.FullDestinationException;
import it.polimi.ingsw.model.enumerations.Color;
import java.util.ArrayList;

/**
 * Represents clouds that host students (3 for 2 players-game, 4 for 3 players-game).
 * At the end of the action phase, every player choose a cloud and picks the students it hosts.
 * @author AlessandroG
 * @version 1.0
 */
public class CloudTile extends TileWithStudents {
    // the number of students in the cloud changes with the players number. 3 for 2 players, 4 for 3 players.
    private final int maxStudents;

    /**
     * Constructor needed because an entrance has a fixed maximum number of students.
     *
     * @param numOfPlayers determines the number of player of the game, hence
     *                     how many students the entrance can host: 3 for 2 players-game, 4 for 3 players-game.
     * @throws IllegalArgumentException when numOfPlayers is neither 2 nor 3.
     */
    public CloudTile(int numOfPlayers) throws IllegalArgumentException {
        super();
        switch (numOfPlayers) {
            case 2 -> maxStudents = 3;
            case 3 -> maxStudents = 4;
            default -> throw new IllegalArgumentException("Illegal number of players");
        }
    }

    /**
     * @return an int value representing the maximum number of students allowed on the tile.
     */
    public int getMaxStudents() {
        return maxStudents;
    }

    /**
     * The method is overridden because the cloud can't have more than 3 or 4 students.
     * @throws FullDestinationException when the cloud is full and someone tries to add a student.
     */
    @Override
    public void addStudent(Color color) throws FullDestinationException {
        if(getNumStudents() == maxStudents)
            throw new FullDestinationException();
        super.addStudent(color);
    }

    /**
     * Function called when the cloud is chosen, it empties the cloud and returns the students.
     * @throws CloudNotFullException when the cloud is not full, yet somebody tries to invoke this method.
     */
    public ArrayList<Color> removeAllStudents() throws CloudNotFullException {
        if(getNumStudents() < maxStudents)
            throw new CloudNotFullException();
        ArrayList<Color> studentsToPick = new ArrayList<>();
        for (Color color : Color.values()) {
            for(int i = 0; i < getNumStudents(color); i++)
                studentsToPick.add(color);
        }
        for (Color color : Color.values()) {
            for(int i = 0, j = getNumStudents(color); i < j; i++)
                removeStudent(color);
        }
        return studentsToPick;
    }
}
