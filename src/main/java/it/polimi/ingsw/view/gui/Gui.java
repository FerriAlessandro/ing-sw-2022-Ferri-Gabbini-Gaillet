package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.network.Adapter;
import it.polimi.ingsw.network.messages.*;
import it.polimi.ingsw.view.ViewInterface;
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

    @Override
    public void start(Stage stage) throws Exception {

        loader = new FXMLLoader(getClass().getResource("/fxml/mainMenuScene.fxml"));
        root = loader.load();
        controller = loader.getController();
        controller.setGui(this);
        currentScene = new Scene(root);
        this.stage = stage;
        stage.setTitle("Eriantys");
        stage.setScene(currentScene);
        stage.setWidth(1280d);
        stage.setHeight(720d);
        stage.show();

    }

    public void changeScene(String scene) throws Exception{

        loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(scene));
        root = loader.load();
        controller = loader.getController();
        controller.setGui(this);
        currentScene = new Scene(root);
        stage.setScene(currentScene);
        stage.setWidth(1280d);
        stage.setHeight(720d);
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

    @Override
    public void showAssistantChoice(SMessageShowDeck message) {

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
        return null;
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

    @Override
    public void askUseSavedGame() {

    }

    /**
     * Used to set the client flag for expert game handling.
     *
     * @param messageExpert message containing the flag value
     */
    @Override
    public void setExpert(SMessageExpert messageExpert) {

    }
}
