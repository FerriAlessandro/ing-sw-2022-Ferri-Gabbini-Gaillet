package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.FullDestinationException;
import it.polimi.ingsw.model.enumerations.*;

/**
 * This class hosts the students extracted from the bag. It's part of the player board.
 * @author AlessandroG
 * @version 1.0
 */
public class Entrance extends TileWithStudents {
    // the number of students in the entrance changes with the players number. 7 for 2 players, 9 for 3 players.
    private final int maxStudents;

    /**
     * Constructor needed because an entrance has a fixed maximum number of students.
     * @param numOfPlayers determines the number of player of the game, hence
     * how many students the entrance can host: 7 for 2 players-game, 9 for 3 players-game.
     * @throws IllegalArgumentException when numOfPlayers is neither 2 nor 3.
     */
    public Entrance(int numOfPlayers) throws IllegalArgumentException {
        super();
        switch (numOfPlayers) {
            case 2: maxStudents = 7;
                    break;
            case 3: maxStudents = 9;
                    break;
            default: throw new IllegalArgumentException("Illegal number of players");
        }
    }

    public int getMaxStudents() {
        return this.maxStudents;
    }

    /**
     * The method is overridden because the entrance can't have more than 7 or 9 students per color.
     * @throws FullDestinationException when the entrance is full and someone tries to add a student
     */
    @Override
    public void addStudent(Color color) throws FullDestinationException {
        if(getNumStudents() == maxStudents)
            throw new FullDestinationException();
        super.addStudent(color);
    }
}
