package it.polimi.ingsw;

import it.polimi.ingsw.view.cli.AdvancedCli;
import it.polimi.ingsw.view.cli.Cli;

/** Entrypoint for playing using CLI
 * @author A.G. Gaillet
 * @version 1.0
 */
public class CliClient {
    public static void main(String[] args){
        Cli cli = new AdvancedCli();
        cli.startGame();
    }
}
