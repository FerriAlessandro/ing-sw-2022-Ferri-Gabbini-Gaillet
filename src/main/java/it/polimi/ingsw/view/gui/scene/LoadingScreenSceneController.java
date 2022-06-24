package it.polimi.ingsw.view.gui.scene;

import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.view.gui.Gui;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;

public class LoadingScreenSceneController implements SceneController{

    @FXML
    private Pane loadingPane;

    @FXML
    private ImageView loadingImage;

    @FXML
    public void initialize(){
        Label loadingText = new Label("Waiting for other players...");
        loadingText.setLayoutX(650);
        loadingText.setLayoutY(75);
        loadingText.setFont(new Font("Arial", 50));
        loadingText.setStyle("-fx-font-weight: bold;");
        loadingPane.getChildren().add(loadingText);

    }
    @Override
    public void setGui(Gui gui) {
    }

    @Override
    public void setMessage(Message message) {

    }

    @Override
    public void createScene() {

    }
}
