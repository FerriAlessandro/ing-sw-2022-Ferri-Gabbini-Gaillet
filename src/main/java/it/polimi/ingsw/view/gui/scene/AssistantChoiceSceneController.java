package it.polimi.ingsw.view.gui.scene;


import it.polimi.ingsw.model.enumerations.AssistantCard;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.RMessageAssistant;
import it.polimi.ingsw.network.messages.SMessageShowDeck;
import it.polimi.ingsw.view.gui.Gui;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;

/**
 * Scene Controller of the AssistantChoice Scene.
 * Methods handle the choice of an assistant card, sending the message containing the choice to the adapter.
 * @author AlessandroG
 * @version 1.0
 */
public class AssistantChoiceSceneController implements SceneController {

    private Gui gui;
    private SMessageShowDeck message;

    @FXML
    private GridPane gridPane;
    @FXML
    private ImageView catView;
    @FXML
    private ImageView cheetahView;
    @FXML
    private ImageView dogView;
    @FXML
    private ImageView eagleView;
    @FXML
    private ImageView elephantView;
    @FXML
    private ImageView foxView;
    @FXML
    private ImageView lizardView;
    @FXML
    private ImageView octopusView;
    @FXML
    private ImageView ostrichView;
    @FXML
    private ImageView turtleView;

    @FXML ArrayList<Integer> listOfCards = new ArrayList<>();

    /**
     * Initialize method creates an array containing the index of the ten assistant cards
     */
    @FXML
    void initialize() {
        for(int i = 1; i <= 10; i++) { //inizializziamo qui perchè tanto questo non cambia mai, il controller non viene più istanziato ogni volta!
            listOfCards.add(i);
        }
    }

    @FXML
    void onCatClicked(MouseEvent event) {
        gui.adapter.sendMessage(new RMessageAssistant(AssistantCard.CAT, gui.getNickName()));
        gameBoardScene();
    }

    @FXML
    void onCheetahClicked(MouseEvent event) {
        gui.adapter.sendMessage(new RMessageAssistant(AssistantCard.CHEETAH, gui.getNickName()));
        gameBoardScene();
    }

    @FXML
    void onDogClicked(MouseEvent event) {
        gui.adapter.sendMessage(new RMessageAssistant(AssistantCard.DOG, gui.getNickName()));
        gameBoardScene();
    }

    @FXML
    void onEagleClicked(MouseEvent event) {
        gui.adapter.sendMessage(new RMessageAssistant(AssistantCard.EAGLE, gui.getNickName()));
        gameBoardScene();
    }

    @FXML
    void onElephantClicked(MouseEvent event) {
        gui.adapter.sendMessage(new RMessageAssistant(AssistantCard.ELEPHANT, gui.getNickName()));
        gameBoardScene();
    }

    @FXML
    void onFoxClicked(MouseEvent event) {
        gui.adapter.sendMessage(new RMessageAssistant(AssistantCard.FOX, gui.getNickName()));
        gameBoardScene();
    }

    @FXML
    void onLizardClicked(MouseEvent event) {
        gui.adapter.sendMessage(new RMessageAssistant(AssistantCard.LIZARD, gui.getNickName()));
        gameBoardScene();

    }

    @FXML
    void onOctopusClicked(MouseEvent event) {
        gui.adapter.sendMessage(new RMessageAssistant(AssistantCard.OCTOPUS, gui.getNickName()));
        gameBoardScene();
    }

    @FXML
    void onOstrichClicked(MouseEvent event) {
        gui.adapter.sendMessage(new RMessageAssistant(AssistantCard.OSTRICH, gui.getNickName()));
        gameBoardScene();
    }

    @FXML
    void onTurtleClicked(MouseEvent event) {
        gui.adapter.sendMessage(new RMessageAssistant(AssistantCard.TURTLE, gui.getNickName()));
        gameBoardScene();
    }


    @Override
    public void setGui(Gui gui) {
        this.gui = gui;
    }

    @Override
    public void setMessage(Message message) {
        this.message = (SMessageShowDeck)message;
    }

    /**
     * Builds the JavaFX scene containing the 10 available assistant cards.
     * If a card has already been played, the method changes the graphical appearance of the card and makes it not clickable.
     */
    @Override
    public void createScene() {

        ArrayList<Integer> listOfCardsAvailable = new ArrayList<>(); //Dobbiamo dichiararlo qui, altrimenti visto che il controller è creato una sola volta si tiene in memoria le vecchie carte
        for(AssistantCard cards : message.cards) {
            listOfCardsAvailable.add(cards.getCardValue());
        }

        for(Integer num : listOfCards) {
            if(!listOfCardsAvailable.contains(num)) {
                gridPane.getChildren().get(num - 1).setOpacity(0.25);
                gridPane.getChildren().get(num-1).setCursor(Cursor.DEFAULT);
                gridPane.getChildren().get(num-1).setDisable(true);
            }
        }
    }

    /**
     * Handles the passage between the assistant choice scene and the gameBoard scene.
     */
    public void gameBoardScene(){
        try{
            gui.changeScene(Gui.GAMEBOARD);
            gui.getScenes().get(Gui.GAMEBOARD).getRoot().setCursor(Cursor.DEFAULT);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

}