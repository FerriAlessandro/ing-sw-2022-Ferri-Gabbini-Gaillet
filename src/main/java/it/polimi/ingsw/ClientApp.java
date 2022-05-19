package it.polimi.ingsw;

import it.polimi.ingsw.view.gui.Gui;
import javafx.application.Application;
import it.polimi.ingsw.view.cli.AdvancedCli;
import it.polimi.ingsw.view.cli.Cli;

/**
 * This class represents the client application. It creates a CLI or a GUI based on Client choice.
 * @author AlesssandroG
 * @version 1.0
 */
public class ClientApp {

    public static void main(String[] args) {

        //TODO we have to decide if we want a standalone launcher
        /*boolean cliUsage = false;

        for(String arguments : args)
            if(arguments.equals("--cli") || arguments.equals("-c")) {
                cliUsage = true;
                break;
            }

        if(cliUsage) {
            Cli cli = new AdvancedCli();
            cli.startGame();
        }
        else
            Application.launch(Gui.class);*/
        Application.launch(Gui.class);
    }
}
