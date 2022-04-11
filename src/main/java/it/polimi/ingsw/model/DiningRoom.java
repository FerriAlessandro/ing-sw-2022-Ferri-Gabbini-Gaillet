package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.FullDestinationException;
import it.polimi.ingsw.model.enumerations.Color;

/**
 * Every player board has a dining room, where students of each school are hosted.
 * Max 9 students per school.
 * @author AlessandroG
 * @version 1.0
 */
public class DiningRoom extends TileWithStudents {
    private final int maxStudents = 10;

    public DiningRoom() {
        super();
    }

    public int getMaxStudents() {
        return this.maxStudents;
    }

    /**
     * Method overridden because a max of 10 students is allowed per color.
     * @throws FullDestinationException when there are 10 students of a color and someone
     * tries to add another student of that color.
     * @throws RuntimeException when a coin must be given to the DiningRoom's owner.
     */
    @Override
    public void addStudent(Color color) throws FullDestinationException, RuntimeException {
        if(getNumStudents(color) == maxStudents)
            throw new FullDestinationException();
        super.addStudent(color);
        for(int i = 1; i <= 3; i++)
            if(getNumStudents(color) == 3 * i)
                throw new RuntimeException("A coin must be added!");
    }

}
