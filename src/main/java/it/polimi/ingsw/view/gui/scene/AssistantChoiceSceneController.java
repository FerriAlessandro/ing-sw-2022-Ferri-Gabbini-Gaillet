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

public class AssistantChoiceSceneController implements SceneController {

    private Gui gui;

    private final ArrayList<ImageView> imageViews = new ArrayList<>();

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

    private ArrayList<Integer> listOfCardsAvailable = new ArrayList<>();

    @FXML
    void initialize() {

        SMessageShowDeck message = (SMessageShowDeck) Gui.currentMessage; //for the moment message is a static Gui attribute

        for(int i = 1; i <= 10; i++)
            listOfCards.add(i);

        for(AssistantCard cards : message.cards)
            listOfCardsAvailable.add(cards.getCardValue());

        for(Integer num : listOfCards) {
            if(!listOfCardsAvailable.contains(num)) {
                gridPane.getChildren().get(num - 1).setOpacity(0.5);
                gridPane.getChildren().get(num-1).setCursor(Cursor.DEFAULT);
                gridPane.getChildren().get(num-1).setDisable(true);
            }
        }

    }

    @FXML
    void onCatClicked(MouseEvent event) {
        gui.adapter.sendMessage(new RMessageAssistant(AssistantCard.CAT, gui.getNickName()));
    }

    @FXML
    void onCheetahClicked(MouseEvent event) {
        gui.adapter.sendMessage(new RMessageAssistant(AssistantCard.CHEETAH, gui.getNickName()));
    }

    @FXML
    void onDogClicked(MouseEvent event) {
        gui.adapter.sendMessage(new RMessageAssistant(AssistantCard.DOG, gui.getNickName()));
    }

    @FXML
    void onEagleClicked(MouseEvent event) {
        gui.adapter.sendMessage(new RMessageAssistant(AssistantCard.EAGLE, gui.getNickName()));
    }

    @FXML
    void onElephantClicked(MouseEvent event) {
        gui.adapter.sendMessage(new RMessageAssistant(AssistantCard.ELEPHANT, gui.getNickName()));
    }

    @FXML
    void onFoxClicked(MouseEvent event) {
        gui.adapter.sendMessage(new RMessageAssistant(AssistantCard.FOX, gui.getNickName()));
    }

    @FXML
    void onLizardClicked(MouseEvent event) {
        gui.adapter.sendMessage(new RMessageAssistant(AssistantCard.LIZARD, gui.getNickName()));
    }

    @FXML
    void onOctopusClicked(MouseEvent event) {
        gui.adapter.sendMessage(new RMessageAssistant(AssistantCard.OCTOPUS, gui.getNickName()));
    }

    @FXML
    void onOstrichClicked(MouseEvent event) {
        gui.adapter.sendMessage(new RMessageAssistant(AssistantCard.OSTRICH, gui.getNickName()));
    }

    @FXML
    void onTurtleClicked(MouseEvent event) {
        gui.adapter.sendMessage(new RMessageAssistant(AssistantCard.TURTLE, gui.getNickName()));
    }

    public void setGui(Gui gui) {
        this.gui = gui;
    }

    public void setMessage(Message message) {
        //this.message = (SMessageShowDeck)message;
    }

}