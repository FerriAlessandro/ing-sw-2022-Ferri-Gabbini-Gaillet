package it.polimi.ingsw.view.gui.scene;

import it.polimi.ingsw.model.enumerations.Characters;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.RMessageCharacter;
import it.polimi.ingsw.network.messages.SMessageCharacter;
import it.polimi.ingsw.view.gui.Gui;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.image.Image;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import java.util.ArrayList;

public class CharacterChoiceSceneController implements SceneController{

    private Gui gui;
    private Characters characterChosen;
    private SMessageCharacter message;
    private String nickname;

    @FXML
    private GridPane gridPane;

    @FXML
    private Button cardOneButton;

    @FXML
    private Button cardThreeButton;

    @FXML
    private Button cardTwoButton;

    @FXML
    private Button noneButton;

    /**
     * It build the scene with the available choice
     */
    @FXML
    void initialize() {

    }

    @FXML
    void onCardOneClicked(ActionEvent event) {
        characterChosen = message.effects.get(0);
        sendChoice();
    }

    @FXML
    void onCardTwoClicked(ActionEvent event) {
        characterChosen = message.effects.get(1);
        sendChoice();
    }

    @FXML
    void onCardThreeClicked(ActionEvent event) {
        characterChosen = message.effects.get(2);
        sendChoice();
    }

    @FXML
    void onNoneButtonPressed(ActionEvent event) {
        characterChosen = Characters.NONE;
        sendChoice();
    }


    /**
     * Utility method to avoid code repetitions. It adds the correct character to the gridPane
     * @param character is the actual character to add
     * @param index position on the gridPane
     */
    private void addCharacterCard(Characters character, int index) {
        Image characterToAdd = new Image("/images/characters/" + character + ".jpg");
        ImageView imageView = new ImageView();
        imageView.setFitHeight(569.5);
        imageView.setFitWidth(342.5);
        imageView.setImage(characterToAdd);
        gridPane.add(imageView, index, 0);

        if(character.getCost() > gui.getCoins().get(nickname)) {
            imageView.setOpacity(0.5);
            imageView.setCursor(Cursor.DEFAULT); //forse superflua
            gridPane.getChildren().get(index).setCursor(Cursor.DEFAULT);
            gridPane.getChildren().get(index).setDisable(true);
        }
        else {
            imageView.setCursor(Cursor.HAND);
            gridPane.getChildren().get(index).setCursor(Cursor.HAND); //forse superflua
            gridPane.getChildren().get(index).setDisable(false);
        }
    }

    /**
     * send the effective message to the adapter.
     */
    private void sendChoice() {
        gui.adapter.sendMessage(new RMessageCharacter(characterChosen, gui.getNickName()));
    }

    @Override
    public void setGui(Gui gui) {
        this.gui = gui;
    }

    @Override
    public void setMessage(Message message) {
        this.message = (SMessageCharacter) message;
    }

    /**
     * It builds the scene with the available choice
     */
    @Override
    public void createScene() {
        nickname = gui.getNickName();
        ArrayList<Characters> charactersAvailable = message.effects;
        int i = 0;

        for(Characters character : charactersAvailable) {
            addCharacterCard(character, i);
            i ++;
        }
    }
}
