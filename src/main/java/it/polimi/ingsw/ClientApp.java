package it.polimi.ingsw;

import it.polimi.ingsw.view.gui.Gui;
import javafx.application.Application;
import it.polimi.ingsw.view.cli.AdvancedCli;
import it.polimi.ingsw.view.cli.Cli;

/**
 * This class represents the client application. It creates a CLI or a GUI based on Client choice.
 * @author AlesssandroG, A.G. Gaillet
 * @version 1.1
 */
public class ClientApp {

    public static void main(String[] args) {

        boolean useCli = false;
        boolean simpleCli = false;

        for(String arguments : args) {
            if (arguments.equals("--cli") || arguments.equals("-c")) {
                useCli = true;
            } else if (arguments.equals("--simple") || arguments.equals("-s")) {
                simpleCli = true;
            }
        }

        if(useCli) {
            Cli cli;
            if(simpleCli) {
                cli = new AdvancedCli();
            }else {
                cli = new Cli();
            }
            cli.startGame();
        } else {
            Application.launch(Gui.class);
        }
    }

}
