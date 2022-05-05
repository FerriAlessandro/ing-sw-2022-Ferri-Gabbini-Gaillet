package it.polimi.ingsw.model;
import it.polimi.ingsw.exceptions.EmptyBagException;
import it.polimi.ingsw.exceptions.FullDestinationException;
import it.polimi.ingsw.model.enumerations.Characters;
import it.polimi.ingsw.model.enumerations.Color;

import java.util.ArrayList;


/**
 * This Class represents Character Cards
 * @author Alessandro F.
 * @version 1.0
 */

public class CharacterCard extends TileWithStudents{

    private final Characters name;
    private final int cost;
    private boolean increased;
    private int noEntryTiles;
    private int maxStudents;
    private boolean isActive = false;
    private Color forbiddenColor;

    /**
     * Class constructor
     * @param name Name of the chosen Character
     */
    public CharacterCard(Characters name){

        super();
        this.name = name;
        this.cost = name.getCost();
        this.increased = false;
        this.maxStudents = 0;
        if(name.equals(Characters.GRANDMA_HERB))
            this.noEntryTiles = 4;
        else this.noEntryTiles = 0;

    }

    /**
     * @return The name of the Character Card
     */
    public Characters getName(){
        return this.name;
    }

    /**
     * @return The cost of the card, if it has already been used returns the cost + 1
     */
    public int getCost(){
        int totalCost;
        totalCost = this.increased? this.cost +1 : this.cost;
        return totalCost;

    }

    /**
     * The card is set to 'Used'
     */
    public void use(){
        this.increased = true;
    }

    /**
     * Removes the No Entry Tiles from the card (if the Character is Grandma Herb)
     */

    public void removeNoEntryTile (){ //if there are no tiles return false... we avoid throwing an exception
        if(noEntryTiles > 0) {
            noEntryTiles--;
        }
}

    /**
     * Adds a No Entry Tile to the card (if the Character is Grandma Herb)
     */
    public void addNoEntryTile(){
        if(name.equals(Characters.GRANDMA_HERB) && noEntryTiles < 4)
            noEntryTiles++;

    }

    /**
     * @return Number of No Entry Tiles
     */
    public int getNoEntryTiles(){
        return this.noEntryTiles;
    }

    /**
     * This method fills certain Character Cards with students
     * @param bag The bag from which students are drawn
     * @throws EmptyBagException Thrown if the bag is empty
     */
    public void setStudents(Bag bag) throws EmptyBagException {

        switch (name){

            case MONK :

            case SPOILED_PRINCESS: {
                maxStudents = 4;
                ArrayList<Color> students = bag.getStudents(maxStudents);
                for (int i = 0; i < maxStudents; i++)
                    try {
                        addStudent(students.get(i));
                    } catch (FullDestinationException ignored){}
                break;
            }

            case JESTER: {
                maxStudents = 6;
                ArrayList<Color> students = bag.getStudents(maxStudents);
                for (int i = 0; i < maxStudents; i++)
                    try {
                        addStudent(students.get(i));
                    } catch (FullDestinationException ignored){}
                break;
            }

        }
    }

    /**
     * @return True if it's one of the cards that can have students on them, false otherwise
     */
    public boolean containsStudents(){
        return name.equals(Characters.MONK) || name.equals(Characters.SPOILED_PRINCESS) || name.equals(Characters.JESTER);
    }

    /**
     * @return True if the card is currently active, false otherwise
     */
    public boolean isActive(){
        return isActive;
    }

    /**
     * Sets the card as active or inactive
     * @param active True if is active, false if it's not
     */
    public void setActive(boolean active){
        this.isActive = active;
    }

    /**
     * Sets the color to ignore in the influence check (only if the card is Mushroom Picker)
     * @param color The color to ignore in the influence check
     */
    public void setForbiddenColor(Color color){
        this.forbiddenColor = color;
    }

    /**
     * @return The color to ignore in the influence check (Only if the card is Mushroom Picker)
     */
    public Color getForbiddenColor(){
        return this.forbiddenColor;
    }


}
