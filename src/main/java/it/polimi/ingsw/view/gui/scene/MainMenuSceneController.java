package it.polimi.ingsw.view.gui.scene;

import it.polimi.ingsw.view.gui.Gui;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;


/**
 * This is the Main Menu Scene Controller, it shows the Login Scene if play button is pressed,
 * close the application if quit button is pressed.
 * @author AlessandroG
 * @version 1.0
 */
public class MainMenuSceneController implements SceneController {

    private Gui gui;

    @FXML
    void playButtonPressed(ActionEvent event) throws Exception{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/LoginScene.fxml"));
        gui.changeScene("/fxml/LoginScene.fxml");
    }

    @FXML
    //TODO handle server-side disconnection
    void quitButtonPressed(ActionEvent event) {
        System.exit(0);
    }

    public void setGui(Gui gui) {
        this.gui = gui;
    }

}
