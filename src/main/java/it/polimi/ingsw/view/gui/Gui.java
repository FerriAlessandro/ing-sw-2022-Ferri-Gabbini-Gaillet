package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.network.Adapter;
import it.polimi.ingsw.network.messages.*;
import it.polimi.ingsw.view.ViewInterface;
import it.polimi.ingsw.view.gui.scene.GameBoardSceneController;
import it.polimi.ingsw.view.gui.scene.SceneController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

/**
 * This class is the real Graphical User Interface.
 * @author AlessandroG
 * @version 1.0
 */
public class Gui extends Application implements ViewInterface {

    public Adapter adapter;
    private final HashMap<String, Scene> sceneMap = new HashMap<>(); //Could be useful
    public static Scene currentScene;
    private Stage stage;
    private String nickname;
    private boolean expert = false;
    private int numOfPlayer;
    FXMLLoader loader;
    Parent root;
    SceneController controller;
    public static Message currentMessage;
    
    /**
     * Method called when the gui starts. It sets the main menu scene.
     * @param stage is the main stage
     * @throws Exception
     */

    @Override
    public void start(Stage stage) throws Exception {

        loader = new FXMLLoader(getClass().getResource("/fxml/GameBoard.fxml"));
        root = loader.load();
        controller = loader.getController();
        controller.setGui(this);
        currentScene = new Scene(root);
        this.stage = stage;
        stage.setTitle("Eriantys");
        stage.setScene(currentScene);
        stage.setWidth(1920d);
        stage.setHeight(1080d);
        stage.show();
        this.askMotherNatureMove(new SMessageMotherNature(5));
    }

    /**
     * Function called every time it's necessary change the current scene
     * @param scene is the new scene that will be attached to the stage and showed
     * @throws Exception
     */
    public void changeScene(String scene) throws Exception{

        loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(scene));
        root = loader.load();
        controller = loader.getController();
        //For the moment currentMessage is a static attribute
        //controller.setMessage(this.currentMessage);
        controller.setGui(this);
        currentScene = new Scene(root);
        stage.setScene(currentScene);
        stage.setWidth(1920d);
        stage.setHeight(1080d);
        stage.sizeToScene(); //TODO resizing
        stage.show();

    }

    /**
     * Asks the player to pick a nickname
     */
    @Override
    public void askNickName() {
        Platform.runLater(()-> {
            //TODO avoid blank nickname
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Nickname selection");
            dialog.setHeaderText("Look, it seems we have a new wizard");
            dialog.setContentText("Please enter your battle name:");
            Optional<String> result = dialog.showAndWait();
            if(result.isPresent()) {
                nickname = result.get();
                adapter.sendMessage(new RMessageNickname(nickname));
            }
        });
    }

    /**
     * Ask the player to specify the game settings. It happens only for the first player.
     */
    @Override
    public void askGameSettings() {
        Platform.runLater(()->{

            List<Integer> choices = new ArrayList<>();
            choices.add(2);
            choices.add(3);
            ChoiceDialog<Integer> dialog = new ChoiceDialog(2, choices);
            dialog.setTitle("Game settings");
            dialog.setHeaderText("You can't fight alone");
            dialog.setContentText("How many wizards will be there?");
            Optional<Integer> result = dialog.showAndWait();
            result.ifPresent(integer -> numOfPlayer = integer);

            List<String> choices2 = new ArrayList<>();
            choices2.add("For dummies");
            choices2.add("Expert");
            ChoiceDialog<String> dialog2 = new ChoiceDialog("For dummies", choices2);
            dialog2.setTitle("Game settings");
            dialog2.setHeaderText("Are you an expert or a noob?");
            dialog2.setContentText("Which version of the game do you want?");
            Optional<String> result2 = dialog2.showAndWait();
            if(result2.isPresent()) {
                if(result2.get().equals("Expert"))
                    expert = true;
                adapter.sendMessage((new RMessageGameSettings(numOfPlayer, expert)));
            }

        });

    }

    @Override
    public void askMotherNatureMove(SMessageMotherNature message) {
        GameBoardSceneController gameBoardSceneController = (GameBoardSceneController) controller;
        gameBoardSceneController.makeIslandsSelectable();
    }

    @Override
    public void showLobby(SMessageLobby message) {

    }

    @Override
    public void showDisconnectionMessage() {

    }

    @Override
    public void showBoard(SMessageGameState gameState) {

    }

    @Override
    public void showWinMessage(SMessageWin message) {

    }

    @Override
    public void showGenericMessage(SMessageInvalid message) {

    }

    /**
     * Ask the player to choose an assistant. Unavailable assistants are not clickable.
     * @param message message containing available assistants
     */
    @Override
    public void showAssistantChoice(SMessageShowDeck message) {

        Platform.runLater(() -> {
            try {
                currentMessage = message;
                loader.setLocation(getClass().getResource("/fxml/AssistantChoiceScene.fxml"));
                changeScene("/fxml/AssistantChoiceScene.fxml");
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        });

    }

    @Override
    public void showCharacterChoice(SMessageCharacter messageCharacter) {

    }

    @Override
    public void askMove() {

    }

    @Override
    public void askCloud() {

    }

    @Override
    public void askAgain() {

    }

    @Override
    public void showCurrentPlayer(SMessageCurrentPlayer messageCurrentPlayer) {

    }

    @Override
    public String getNickName() {
        return nickname;
    }

    @Override
    public void grandmaHerbHeraldScene(SMessageGrandmaherbHerald message) {

    }

    @Override
    public void monkPrincessScene(SMessageMonkPrincess message) {

    }

    @Override
    public void rogueMushroomPickerScene(SMessageRogueMushroomPicker message) {

    }

    @Override
    public void jesterBardScene(SMessageJesterBard message) {

    }

    /**
     * If game settings fits with a previous saved game, it asks the first player whether to use the saved game or not.
     */
    @Override
    public void askUseSavedGame() {

        Platform.runLater(()-> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Load saved game");
            alert.setHeaderText("A saved game matching this settings has been found");
            alert.setContentText("Do you want restore it?");
            ButtonType buttonOne = new ButtonType("Yes");
            ButtonType buttonTwo = new ButtonType("No");
            alert.getButtonTypes().setAll(buttonOne, buttonTwo);

            Optional<ButtonType> result = alert.showAndWait();
            if(result.isPresent()) {
                if(result.get() == buttonOne)
                    adapter.sendMessage(new RMessageLoadGame(true));
                else
                    adapter.sendMessage(new RMessageLoadGame(false));
            }
        });
    }

    /**
     * Used to set the client flag for expert game handling.
     *
     * @param messageExpert message containing the flag value
     */
    @Override
    public void setExpert(SMessageExpert messageExpert) {
        expert = messageExpert.expert;
    }
}
