package it.polimi.ingsw.view;

import it.polimi.ingsw.model.enumerations.Color;

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

    /**
     * Start coloring text of provided color.
     * @param color
     * @return ANSI color escape sequence
     */
    public static String colored(Color color){
        switch (color){
            case GREEN: return GREEN;
            case YELLOW: return YELLOW;
            case RED: return RED;
            case BLUE: return BLUE;
            case PINK: return PINK;
            default: return WHITE;
        }
    }

    /**
     * End text coloring.
     * @return ANSI color escape code for reset
     */
    public static String endColor(){
        return RESET;
    }
}
