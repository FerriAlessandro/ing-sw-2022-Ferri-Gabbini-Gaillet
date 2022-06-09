package it.polimi.ingsw.view.gui.scene;

import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.SMessageWin;
import it.polimi.ingsw.view.gui.Gui;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class WinSceneController implements SceneController {


    @FXML
    private Pane pane;

    private SMessageWin message;
    private Gui gui;


    @Override
    public void setGui(Gui gui) {
        this.gui = gui;
    }

    @Override
    public void setMessage(Message message) {
        this.message = (SMessageWin) message;
    }

    @Override
    public void createScene() {
        Image image = new Image("/images/miscellaneous/gameOverImage.png");
        ImageView imageView;
        if(message.tie)
            image = new Image("/images/miscellaneous/drawImage.png");
        if(message.nickname.equals(gui.getNickName()))
            image = new Image("/images/miscellaneous/winImage.png");
        imageView = new ImageView(image);
        imageView.setFitWidth(1080);
        imageView.setFitHeight(680);
        pane.getChildren().add(imageView);
        pane.getChildren().get(1).setLayoutX(420);
        pane.getChildren().get(1).setLayoutY(250);
    }

}
