package it.polimi.ingsw;

import it.polimi.ingsw.network.Server;
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
        boolean server = false;

        for(String arguments : args) {
            switch (arguments) {
                case "--cli", "-c" -> useCli = true;
                case "--simple", "-s" -> simpleCli = true;
                case "--server", "-server" -> server = true;
                case "--author", "--authors" -> printAuthors();
                case "--info" -> printInfo();
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
        } else if (server) {
            if (args.length == 2) {
                String[] serverArgs = new String[1];
                serverArgs[0] = args[1];
                Server.main(serverArgs);
            } else {
                Server.main(new String[0]);
            }
        }
        else
            Application.launch(Gui.class);
    }

    /**
     * Prints authors to command line.
     */
    private static void printAuthors(){
        System.out.print("""
                                    
                                    
                                    This version of Eryantis has been programmed with love by:
                                     - Alessandro Ferri
                                     - Alessandro Gabbini
                                     - Angelo G. Gaillet
                                     
                                     
                                     """);
        System.exit(0);
    }

    /**
     * Prints software info to command line.
     */
    private static void printInfo(){
        System.out.println("""
                                    
                                    
                                    This program was developed in accordance with the requirements of the final examination of Software Engineering
                                    at Politecnico di Milano. For further information visit the repository of the project at the following link:
                                    https://github.com/FerriAlessandro/ing-sw-2022-Ferri-Gabbini-Gaillet
                                    
                                    
                                    """);
        System.exit(0);
    }

}
