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
     */
    @Override
    public void addStudent(Color color) throws FullDestinationException {
        if(getNumStudents(color) == maxStudents)
            throw new FullDestinationException();
        super.addStudent(color);
    }
}
