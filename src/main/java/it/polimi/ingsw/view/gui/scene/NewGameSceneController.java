package it.polimi.ingsw.view.gui.scene;

import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.RMessageLoadGame;
import it.polimi.ingsw.view.gui.Gui;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.util.Optional;

public class NewGameSceneController implements SceneController {

    private Gui gui;

    @FXML
    private Button BackButton;

    @FXML
    private MenuButton GameModeButton;

    @FXML
    private Button LoadGameButton;

    @FXML
    private Button NewGameButton;

    @FXML
    private ImageView NewGameImageView;

    @FXML
    private Pane NewGamePane;

    @FXML
    private TextField NicknameField;

    @FXML
    private MenuButton NumOfPlayersButton;

    @FXML
    private MenuButton NumOfPlayersButton1;



    public void askUseSavedGame() {





            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Load saved game");
            alert.setHeaderText("A saved game matching this settings has been found");
            alert.setContentText("Do you want restore it?");
            ButtonType buttonOne = new ButtonType("Yes");
            ButtonType buttonTwo = new ButtonType("No");
            alert.getButtonTypes().setAll(buttonOne, buttonTwo);

            Optional<ButtonType> result = alert.showAndWait();
            if(result.isPresent()) {
                if (result.get() == buttonOne)
                    adapter.sendMessage(new RMessageLoadGame(true));
                else
                    adapter.sendMessage(new RMessageLoadGame(false));
            }
    }

    @Override
    public void setGui(Gui gui) {
        this.gui= gui;
    }

    @Override
    public void setMessage(Message message) {

    }

    @Override
    public void createScene() {

    }
}
