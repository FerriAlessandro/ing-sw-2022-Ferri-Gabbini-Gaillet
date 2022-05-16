package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.view.gui.scene.GameBoardSceneController;
import it.polimi.ingsw.view.gui.scene.MainMenuSceneController;
import javafx.application.Application;
import javafx.application.Platform;
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
    public void start(Stage stage) throws Exception{

        Gui gui = new Gui();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/GameBoard.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setTitle("Eriantys");
        stage.setScene(scene);
        stage.show();



    }

}
