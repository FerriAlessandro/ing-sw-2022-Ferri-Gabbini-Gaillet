package it.polimi.ingsw;

import it.polimi.ingsw.view.cli.AdvancedCli;
import it.polimi.ingsw.view.cli.Cli;

public class CliClient {
    public static void main(String[] args){
        Cli cli = new AdvancedCli();
        cli.startGame();
    }
}
