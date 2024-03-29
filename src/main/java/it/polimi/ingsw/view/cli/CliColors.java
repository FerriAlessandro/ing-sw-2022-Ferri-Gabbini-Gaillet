package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.model.enumerations.Color;
import it.polimi.ingsw.model.enumerations.TowerColor;

/**
 * Provides ANSI escape codes for coloring text in the Command Line Interface.
 * @author A.G. Gaillet
 * @version 1.0
 */
public final class CliColors {
    private static final String RESET = "\033[0m";
    private static final String RED = "\033[0;31m";
    private static final String GREEN = "\033[0;32m";
    private static final String YELLOW = "\033[0;33m";
    private static final String BLUE = "\033[0;34m";
    private static final String PINK = "\033[38;5;206m";
    private static final String WHITE = "\033[0;37m";
    private static final String T_GRAY = "\u001b[48;5;8m";
    private static final String T_BLACK = "\u001b[48;5;0m";
    private static final String T_WHITE = "\u001b[48;5;231m";


    /**
     * Start coloring text of provided color.
     * @param color
     * @return ANSI color escape sequence
     */
    public static String colored(Color color){
        return switch (color) {
            case GREEN -> GREEN;
            case YELLOW -> YELLOW;
            case RED -> RED;
            case BLUE -> BLUE;
            case PINK -> PINK;
        };
    }

    /**
     * Start coloring text of provided tower color.
     * @param color
     * @retuen ANSI color escape sequence
     */
    public static String towerColored(TowerColor color){
        return switch (color) {
            case WHITE -> T_WHITE;
            case BLACK -> T_BLACK;
            case GRAY -> T_GRAY;
            default -> WHITE;
        };
    }

    /**
     * End text coloring.
     * @return ANSI color escape code for reset
     */
    public static String endColor(){
        return RESET;
    }
}
