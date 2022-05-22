package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.model.enumerations.Characters;
import it.polimi.ingsw.model.enumerations.Color;
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

import java.util.*;

/**
 * This class is the real Graphical User Interface.
 * @author AlessandroG
 * @version 1.0
 */
public class Gui extends Application implements ViewInterface {

    public Adapter adapter;
    private Scene currentScene;
    private Stage stage;
    private String nickname;
    private boolean expert = false;
    private boolean firstGameStateMessage = true;
    private int numOfPlayer;
    private Characters characterChosen = Characters.NONE;
    private FXMLLoader loader;
    private Parent root;
    private SceneController controller;
    private Message currentMessage;
    public static final String ASSISTANT = "/fxml/AssistantChoiceScene.fxml";
    public static final String CHARACTER = "/fxml/CharacterChoiceScene.fxml";
    public static final String GAMEBOARD = "/fxml/GameBoard.fxml";
    public static final String LOGIN = "/fxml/LoginScene.fxml";
    public static final String MENU = "/fxml/MainMenuScene.fxml";
    public static final String NICKNAME = "/fxml/NicknameScene.fxml";
    private HashMap<String, Scene> scenes = new HashMap<>();
    private HashMap<String, SceneController> controllers = new HashMap<>();



    /** Coins owned by each player */
    private Map<String, Integer> coins;
    
    /**
     * Method called when the gui starts. It sets the main menu scene.
     * @param stage is the main stage
     * @throws Exception
     */

    @Override
    public void start(Stage stage) throws Exception {

        initializeGui();
        coins = new HashMap<>();
        currentScene = scenes.get(MENU);
        this.stage = stage;
        stage.setTitle("Eriantys");
        stage.setScene(currentScene);
        stage.setWidth(1920d);
        stage.setHeight(1080d);
        stage.show();
    }

    public void initializeGui(){

        ArrayList<String> sceneFXML = new ArrayList<>(Arrays.asList(ASSISTANT, CHARACTER, GAMEBOARD, LOGIN, MENU, NICKNAME));
        for(String fxml : sceneFXML){
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            try {
                Parent root = loader.load();
                scenes.put(fxml, new Scene(root));
                SceneController controller = loader.getController();
                controllers.put(fxml, controller);
                controller.setGui(this);
            }catch(Exception e){
                e.printStackTrace();
                return;
            }


        }

    }

    /**
     * Function called every time it's necessary change the current scene
     * @param scene is the new scene that will be attached to the stage and showed
     * @throws Exception
     */
    public void changeScene(String scene) throws Exception{


        //Every scene is already initialized and every controller already instantiated
        controller = controllers.get(scene);
        controller.setMessage(currentMessage);
        controller.createScene();
        currentScene = scenes.get(scene);
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

        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Next move");
            alert.setHeaderText("Choose the destination of mother nature");
            alert.setContentText(null);
            alert.showAndWait();
            try {
                changeScene(GAMEBOARD);
                GameBoardSceneController gameBoardSceneController = (GameBoardSceneController) controller;
                gameBoardSceneController.getIslandChoice();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Display lobby
     * @param message containing information on connected and desired players.
     */
    @Override
    public void showLobby(SMessageLobby message) {
        Platform.runLater(()-> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Lobby information");
            alert.setHeaderText("We can't start the game right now, we need other wizard(s)!");
            alert.setContentText("Waiting for " + message.numPlayersTotal + " players to be connected...\n");
            //TODO add currentPlayers connected list
            alert.showAndWait();
        });
    }

    /**
     * Display a disconnection alert
     */
    @Override
    public void showDisconnectionMessage() {
        Platform.runLater(()-> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Connection problem");
            alert.setHeaderText("You are now alone.\nThe game cannot procede.");
            alert.setContentText("Someone lost connection - ending game");
            alert.showAndWait();
            System.exit(0);
        });
    }

    @Override
    public void showBoard(SMessageGameState gameState) {

        Platform.runLater(()-> {
            try {
                changeScene(GAMEBOARD);

            }
            catch (Exception e) {
                e.printStackTrace();
            }

            GameBoardSceneController gameBoardSceneController = (GameBoardSceneController) controllers.get(GAMEBOARD);
            if(firstGameStateMessage){
                firstGameStateMessage = false;
                gameBoardSceneController.setupGameBoard(gameState.towerNumber.keySet().size(),expert, gameState);

            }


            gameBoardSceneController.refreshEntrances(gameState.studEntrance);
            gameBoardSceneController.refreshDiningRooms(gameState.studDining);
            gameBoardSceneController.refreshProfessors(gameState.professors);
            gameBoardSceneController.refreshTowerZones(gameState.towerNumber);
            gameBoardSceneController.refreshIslandsStudents(gameState.studIslands);
            gameBoardSceneController.refreshIslandsTowersColor(gameState.colorTowerIslands);
            gameBoardSceneController.refreshIslandsTowersNum(gameState.numTowersIslands);
            gameBoardSceneController.refreshMotherNature(gameState.motherNaturePosition);
            gameBoardSceneController.refreshNoEntryTiles(gameState.forbiddenTokens);
            gameBoardSceneController.refreshClouds(gameState.studClouds);
            if(expert) {
                gameBoardSceneController.refreshCoins(gameState.coins);
                this.coins = new HashMap<>(gameState.coins);
            }


        });


    }

    @Override
    public void showWinMessage(SMessageWin message) {

    }

    /**
     * Display an error alert dialog, the text content depends on the message received.
     * @param message containing the {@link String} to be displayed.
     */
    @Override
    public void showGenericMessage(SMessageInvalid message) {
        Platform.runLater(()-> {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Disclaimer");
            alert.setHeaderText("\n" + message.error.toUpperCase() + "\n");
            alert.setContentText(null);
            alert.showAndWait();
        });
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
                changeScene(ASSISTANT);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        });

    }

    /**
     * Display the player the available character to be chosen.
     * @param messageCharacter message containing available characters
     */
    @Override
    public void showCharacterChoice(SMessageCharacter messageCharacter) {
        Platform.runLater(() -> { //TODO non mostrare se non si può giocare nulla
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Character card choice");
            alert.setHeaderText("You have the possibility to play a character card");
            alert.setContentText("Do you wanna play one?");
            ButtonType buttonOne = new ButtonType("Yes");
            ButtonType buttonTwo = new ButtonType("No");
            alert.getButtonTypes().setAll(buttonOne, buttonTwo);

            Optional<ButtonType> result = alert.showAndWait();
            if(result.isPresent()) {
                if(result.get() == buttonOne) {
                    try {
                        currentMessage = messageCharacter;
                        changeScene(CHARACTER);
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                else
                    adapter.sendMessage(new RMessageCharacter(Characters.NONE, nickname));
            }

        });
    }

    @Override
    public void askMove() {
        Platform.runLater(() -> {
            try {

                changeScene(GAMEBOARD);
                GameBoardSceneController gameBoardSceneController = (GameBoardSceneController) controllers.get(GAMEBOARD);

                gameBoardSceneController.getEntranceChoice();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        });

    }

    @Override
    public void askCloud() {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Next move");
            alert.setHeaderText("Choose the cloud tile to pick students from");
            alert.setContentText(null);
            alert.showAndWait();
            try {
                changeScene(GAMEBOARD);
                GameBoardSceneController gameBoardSceneController = (GameBoardSceneController) controller;
                gameBoardSceneController.getCloudChoice();

            }
            catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Display an alert warning dialog when user performs a wrong action.
     */
    @Override
    public void askAgain() {
        Platform.runLater(()-> {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Wrong action");
            alert.setHeaderText("Invalid request. Please try again...\n");
            alert.setContentText(null);
            alert.showAndWait();
        });
    }

    /**
     * Display an information alert dialog just to show who's playing right now
     * @param messageCurrentPlayer contains the current player nickname
     */
    @Override
    public void showCurrentPlayer(SMessageCurrentPlayer messageCurrentPlayer) {

        GameBoardSceneController gameBoardSceneController = (GameBoardSceneController) controllers.get(GAMEBOARD);
        Platform.runLater(() ->gameBoardSceneController.colorCurrentPlayer(messageCurrentPlayer.nickname));

        if(!messageCurrentPlayer.nickname.equals(nickname))
            Platform.runLater(()-> {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Current player");
                alert.setHeaderText("It's now " + messageCurrentPlayer.nickname + "'s turn\n");
                alert.setContentText(null);
                alert.showAndWait();
            });
    }

    @Override
    public String getNickName() {
        return nickname;
    }

    public HashMap<String, SceneController> getControllers() {
        return controllers;
    }

    public HashMap<String, Scene> getScenes() {
        return scenes;
    }

    /**
     * Asks additional information on chosen character effect of
     * {@link it.polimi.ingsw.model.enumerations.Characters#GRANDMA_HERB} or
     * {@link it.polimi.ingsw.model.enumerations.Characters#HERALD}.
     * @param message request message
     */
    @Override
    public void grandmaHerbHeraldScene(SMessageGrandmaherbHerald message) {

        Platform.runLater(() -> {
            Characters characterChosen = message.characterName;
            String text;
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Disclaimer");
            alert.setHeaderText("You have chosen " + characterChosen.name() + " card!");
            if(characterChosen.equals(Characters.GRANDMA_HERB))
                text = "Select the island where the no entry tile will be applied";
            else
                text = "Select the island to resolve";
            alert.setContentText(text);

            try {
                changeScene(GAMEBOARD);
                GameBoardSceneController gameBoardSceneController = (GameBoardSceneController) controller;
                gameBoardSceneController.getIslandChoiceGrandmaHerald();

            }
            catch (Exception e) {
                e.printStackTrace();
            }

        });
    }

    /**
     * Asks additional information on chosen character effect of
     * {@link it.polimi.ingsw.model.enumerations.Characters#MONK} or
     * {@link it.polimi.ingsw.model.enumerations.Characters#SPOILED_PRINCESS}.
     * @param message request message
     */
    @Override
    public void monkPrincessScene(SMessageMonkPrincess message) {
        Platform.runLater(() -> {
            Characters characterChosen = message.characterName;
            int islandChosenIndex = -1;
            String text;
            //This for cycle build a list of the color of the students on the character card
            ArrayList<Color> list = new ArrayList<>();
            for(Color color : message.colors.keySet()) {
                if(message.colors.get(color) > 0)
                    list.add(color);
            }

            ChoiceDialog<Color> dialog = new ChoiceDialog<>(list.get(0), list);
            dialog.setTitle("Disclaimer");
            dialog.setHeaderText("You have chosen " + characterChosen.name() + " card!");
            if(characterChosen.equals(Characters.MONK))
                text = "Select the color of the student to put on the island that you desire";
            else
                text = "Select the color of the student to add to your dining room";
            dialog.setContentText(text);

            Optional<Color> result = dialog.showAndWait();

            if(result.isPresent()) {
                if (characterChosen.equals(Characters.MONK)) {
                    try {
                        changeScene(GAMEBOARD);
                        GameBoardSceneController gameBoardSceneController = (GameBoardSceneController) controller;
                        gameBoardSceneController.getIslandChoiceMonk(result.get());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else { //Case PRINCESS
                    adapter.sendMessage(new RMessageMonkPrincess(characterChosen, nickname, islandChosenIndex, result.get()));
                }
            }
        });
    }

    @Override
    public void rogueMushroomPickerScene(SMessageRogueMushroomPicker message) {

        Platform.runLater(() -> {
            Characters characterChosen = message.characterName;
            String text;
            List<Color> choices = new ArrayList<>();
            choices.add(Color.RED);
            choices.add(Color.GREEN);
            choices.add(Color.BLUE);
            choices.add(Color.YELLOW);
            choices.add(Color.PINK);

            ChoiceDialog<Color> dialog = new ChoiceDialog(choices.get(0), choices);
            dialog.setTitle("Disclaimer");
            dialog.setHeaderText("You have chosen " + characterChosen.name() + " card!");
            if(characterChosen.equals(Characters.ROGUE))
                text = "Select the color of the three students that every wizard must return";
            else
                text = "Select the color that will not count during this turn's influence calculation";
            dialog.setContentText(text);
            Optional<Color> result = dialog.showAndWait();
            if(result.isPresent())
                adapter.sendMessage(new RMessageRogueMushroomPicker(characterChosen, nickname, result.get()));
        });
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
     * @param messageExpert message containing the flag value
     */
    @Override
    public void setExpert(SMessageExpert messageExpert) {
        expert = messageExpert.expert;
    }

    /**
     * Used to notify all clients and update the assistant card chosen by a player.
     *
     * @param messageAssistantStatus containing nickname and chosen assistant
     */
    @Override
    public void showAssistantStatus(SMessageAssistantStatus messageAssistantStatus) {

        Platform.runLater(()->{ GameBoardSceneController controller =  (GameBoardSceneController) controllers.get(GAMEBOARD);
            controller.refreshAssistant(messageAssistantStatus);
        });
    }


    public Map<String,Integer> getCoins() {
        return coins;
    }

    public void setCharacter(Characters character) {
        characterChosen = character;
    }

    public Characters getCharacter() {
        return characterChosen;
    }
}
