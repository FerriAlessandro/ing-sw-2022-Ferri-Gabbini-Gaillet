package it.polimi.ingsw.view.cli.drawables;

import it.polimi.ingsw.model.enumerations.Color;
import it.polimi.ingsw.view.cli.CliColors;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

/**
 * This class provides a {@link Drawable} cloud.
 * @author A.G. Gaillet
 * @version 1.2
 */
public class DrawableCloud extends Drawable{
    private final ArrayList<Color> students;
    private final int index;

    /** The height of the drawable object in number of lines */
    public final static int height = 3;

    public DrawableCloud(Map<Color, Integer> students, int index){
        this.students = new ArrayList<>();
        for(Color color: students.keySet()){
            for(int i = 0; i < students.get(color); i++){
                this.students.add(color);
            }
        }
        Collections.shuffle(this.students);
        this.index = index;
    }

    /**
     * Converts the element to a printable version
     * @return {@link String}
     */
    public String toString(){
        String value = "";
        value = value.concat("┌" + index + "━");
        for(int i = 0; i < students.size() - 1; i++){
            value = value.concat("━━");
        }
        value = value.concat("━┑\n");
        value = value.concat("│ ");
        for(Color student : students){
            value = value.concat(CliColors.colored(student) + "● " + CliColors.endColor());
        }
        if(students.size() == 0){
            value = value.concat("  ");
        }
        value = value.concat("│\n");
        value = value.concat("┕");
        for(int i = 0; i < students.size(); i++){
            value = value.concat("━━");
        }
        if(students.size() == 0){
            value = value.concat("━━");
        }
        value = value.concat("━┙\n");

        return value;
    }

}
