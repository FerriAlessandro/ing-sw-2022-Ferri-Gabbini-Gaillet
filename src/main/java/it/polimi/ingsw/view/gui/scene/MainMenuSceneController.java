package it.polimi.ingsw.view.gui.scene;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class MainMenuSceneController {

    @FXML
    void playButtonPressed(ActionEvent event) {
        //TODO ora bisogna far partire la loginScene
    }

    @FXML
    //Ovviamente da sistemare, al momento è giusto per chiudere il gioco cliccando su QUIT
    void quitButtonPressed(ActionEvent event) {
        System.exit(0);
    }

}
