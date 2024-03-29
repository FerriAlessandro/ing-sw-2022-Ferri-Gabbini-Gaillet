package it.polimi.ingsw.view.cli.drawables;

import it.polimi.ingsw.model.enumerations.Color;
import it.polimi.ingsw.model.enumerations.TowerColor;
import it.polimi.ingsw.view.cli.CliColors;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

/**
 * This class provides a {@link Drawable} player-board.
 * @author A.G. Gaillet
 * @version 1.0
 */
public class DrawablePlayerBoard extends Drawable{
    private final EnumMap<Color, Integer> dining;
    private final EnumMap<Color, Integer> entrance;
    private final int towers;
    private final TowerColor towerColor;
    private final ArrayList<Color> professors;
    private final String nickname;

    /** The height of the drawable object in number of lines */
    public final static int height = 19;

    /**
     * Constructor.
     * @param studentsDining
     * @param studentsEntrance
     * @param numTowers
     * @param towerColor
     * @param professors
     * @param nickname
     */
    public DrawablePlayerBoard(Map<Color, Integer> studentsDining, Map<Color, Integer> studentsEntrance, int numTowers, TowerColor towerColor, ArrayList<Color> professors, String nickname) {
        this.dining = new EnumMap<>(studentsDining);
        this.entrance = new EnumMap<>(studentsEntrance);
        this.towers = numTowers;
        this.towerColor = towerColor;
        this.professors = new ArrayList<>(professors);
        this.nickname = nickname;
    }

    /**
     * Converts the element to a printable version
     * @return {@link String}
     */
    @Override
    public String toString() {
        String nick = nickname.toUpperCase();
        if(nick.length() > 9){
            nick = nick.substring(0,9);
        }
        String value = "";
        value = value.concat("┍" + nick);
        for(int i = 0; i < 9 - nick.length(); i ++){
            value = value.concat("╾");
        }
        value = value.concat("┑\n");

        value = value.concat("│");
        for(int i = 0; i < towers; i++){
            value = value.concat( CliColors.towerColored(towerColor) + "x" + CliColors.endColor());
        }

        for(int i = towers; i < 9; i++) {
            value = value.concat(" ");
        }
        value = value.concat("│\n");
        value = value.concat("├╾╾╾╾╾╾╾╾╾┤\n");

        value = value.concat("│");
        for (Color color : Color.values()){
            if(professors.contains(color)){
                value = value.concat(CliColors.colored(color) + "●" + CliColors.endColor());
            }else{
                value = value.concat(" ");
            }
            if(color.equals(Color.GREEN)){
                value = value.concat("│\n");
            }else{
                value = value.concat(" ");
            }
        }
        value = value.concat("├╾╾╾╾╾╾╾╾╾┤\n");

        for(int i = 0; i < 10; i++){
            value = value.concat("│");
            for(Color color : Color.values()){
                if(dining.get(color) >= 10 - i){
                    value = value.concat(CliColors.colored(color) + "●" + CliColors.endColor());
                }else{
                    value = value.concat(" ");
                }
                if(color.equals(Color.GREEN)){
                    value = value.concat("│\n");
                }else{
                    value = value.concat(" ");
                }
            }
        }

        value = value.concat("├╾╾╾╾╾╾╾╾╾┤\n");

        ArrayList<Color> entranceArray = new ArrayList<>();
        for(Color color : entrance.keySet()){
            for(int i = 0; i < entrance.get(color); i++){
                entranceArray.add(color);
            }
        }
        Collections.shuffle(entranceArray);
        String entranceString = "│ ";
        int entrance2linewidth = 2;
        for(int i = 0; i < entranceArray.size(); i++){
            entranceString = entranceString.concat(CliColors.colored(entranceArray.get(i)) + "●" + CliColors.endColor());
            if(i != 8){
                entranceString = entranceString.concat(" ");
            }
            entrance2linewidth += 2;
            if(i == 3){
                entranceString = entranceString.concat("│\n");
                entrance2linewidth = 1;
                value = value.concat(entranceString);
                entranceString = "│";
                if(entranceArray.size()!=9){
                    entranceString = entranceString.concat(" ");
                    entrance2linewidth ++;
                    if(entranceArray.size()!=8){
                        entranceString = entranceString.concat(" ");
                        entrance2linewidth ++;
                    }
                }
            }
        }

        for(int i = entrance2linewidth; i < 10; i++){
            entranceString = entranceString.concat(" ");
        }
        value = value.concat(entranceString +"│\n");


        value = value.concat("└╾╾╾╾╾╾╾╾╾┙\n");

        return value;
    }
}
