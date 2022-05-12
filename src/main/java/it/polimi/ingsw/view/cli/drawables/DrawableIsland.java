package it.polimi.ingsw.view.cli.drawables;

import it.polimi.ingsw.model.enumerations.Color;
import it.polimi.ingsw.model.enumerations.TowerColor;
import it.polimi.ingsw.view.cli.CliColors;

import java.util.EnumMap;
import java.util.Map;

public class DrawableIsland extends Drawable{
    private final Map<Color, Integer> students;
    private final int towerNumber;
    private final TowerColor towerColor;
    private final int forbiddenTokens;
    private final boolean motherNature;
    private final int index;
    public final static int height = 8;



    public DrawableIsland (Map<Color, Integer> students, int towerNumber, TowerColor towerColor, int forbiddenTokens, int index, boolean motherNature){
        this.students = new EnumMap<>(students);
        this.towerNumber = towerNumber;
        this.towerColor = towerColor;
        this.forbiddenTokens = forbiddenTokens;
        this.index = index;
        this.motherNature = motherNature;
    }

    public String toString(){
        String value;
        if (index < 10) {
            value = "╔" + index + "══════════╗\n║ ";
        }else {
            value = "╔" + index + "═════════╗\n║ ";
        }

        int maxStudRow = 5;

        if(students.values().stream().reduce(0, Integer::sum) > maxStudRow){
            int numNotPresent = 0;
            for (Color color : Color.values()) {
                if (students.containsKey(color) && students.get(color) > 0) {
                    value = value.concat(CliColors.colored(color) + students.get(color) + CliColors.endColor() + " ");
                } else {
                    numNotPresent++;
                }
            }
            for (int i = 0; i < numNotPresent; i++) {
                value = value.concat("  ");
            }
        }else{
            int studentNumber = 0;
            for (Color color : students.keySet()){
                for(int i = 0; i < students.get(color); i++){
                    value = value.concat(CliColors.colored(color) + "●" + CliColors.endColor() + " ");
                    studentNumber ++;
                }
            }
            for(int i = studentNumber; i < maxStudRow; i++){
                value = value.concat("  ");
            }
        }

        value = value.concat("║\n║           ║\n");

        if(towerNumber>0){
            value = value.concat("║ " + CliColors.towerColored(towerColor) + towerNumber + " towers" + CliColors.endColor() + "  ║\n");
        }else{
            value = value.concat("║           ║\n");
        }

        value = value.concat("║           ║\n");

        if(forbiddenTokens>0){
            value = value.concat("║ "+ forbiddenTokens + " no-entry" + "║\n");
        }else{
            value = value.concat("║           ║\n");
        }

        if(motherNature){
            value = value.concat("║     \u001b[48;5;47m\u001b[30mℕ\033[0m     ║\n");
        }else {
            value = value.concat("║           ║\n");
        }

        value = value.concat("╚═══════════╝\n");

        return value;
    }

}
