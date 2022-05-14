package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.view.gui.scene.MainMenuSceneController;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

import java.io.IOException;

/**
 * This class creates the GUI and starts the main stage and the first scene.
 * @author AlessandroG
 * @version 1.0
 */
public class FXGui extends Application {

    /**
     * main method of the class.
     * @param stage
     */
    @Override
    public void start(Stage stage) {

        Gui gui = new Gui();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/mainMenuScene.fxml"));
        Parent root = null;

        try {
            root = loader.load();
        }
        catch (IOException e) {
          //TODO exception handling
        }

        MainMenuSceneController controller = loader.getController();

        //TODO understand the observer controller question

        //TODO load the start menu scene

        Scene scene = new Scene(root);
        stage.setScene(scene);

        //Graphical resolution subject to changes
        stage.setWidth(1280d);
        stage.setHeight(720d);
        stage.setFullScreen(true);
        stage.show();

        //TODO set stop method for closing the application
    }
}
