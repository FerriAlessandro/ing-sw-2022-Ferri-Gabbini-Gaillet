package it.polimi.ingsw.view.gui.scene;

import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.SMessageWin;
import it.polimi.ingsw.view.gui.Gui;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class WinSceneController implements SceneController {


    @FXML
    private ImageView WinLoseTieImage;
    @FXML
    private Button CloseButton;

    private SMessageWin message;
    private Gui gui;
    private final String WIN = "/images/miscellaneous/Winner.png";
    private final String LOSE = "/images/miscellaneous/Loser.png";
    private final String TIE = "/images/miscellaneous/Tie.png";


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
        Image image = new Image(LOSE);
        if(message.tie)
            image = new Image(TIE);
        if(message.nickname.equals(gui.getNickName()))
            image = new Image(WIN);
        WinLoseTieImage.setImage(image);

    }

    @FXML
    public void initialize(){
        CloseButton.setOnAction((e)-> {
            System.exit(1);
        });
    }

}
