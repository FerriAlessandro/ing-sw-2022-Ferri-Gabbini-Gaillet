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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;
import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * This class is the real Graphical User Interface.
 * @author AlessandroG
 * @version 2.0
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
    private SceneController controller;
    private Message currentMessage;
    public static final String ASSISTANT = "/fxml/AssistantChoiceScene.fxml";
    public static final String CHARACTER = "/fxml/CharacterChoiceScene.fxml";
    public static final String GAMEBOARD = "/fxml/GameBoard.fxml";
    public static final String LOGIN = "/fxml/LoginScene.fxml";
    public static final String MENU = "/fxml/MainMenuScene.fxml";
    public static final String NICKNAME = "/fxml/NicknameScene.fxml";
    private final HashMap<String, Scene> scenes = new HashMap<>();
    private final HashMap<String, SceneController> controllers = new HashMap<>();

    /** Coins owned by each player */
    private Map<String, Integer> coins;
    
    /**
     * Method called when the gui starts. It sets the main menu scene.
     * @param stage is the main stage
     */

    @Override
    public void start(Stage stage) {
        stage.getIcons().add(new Image("/images/Eriantys.png"));
        initializeGui();
        coins = new HashMap<>();
        currentScene = scenes.get(MENU);
        this.stage = stage;
        stage.setTitle("Eriantys");
        resizeStage(stage);
    }

    /**
     * Method called at the beginning of start() method. It creates every scene and respective controller and puts them in two maps.
     */
    public void initializeGui(){

        ArrayList<String> sceneFXML = new ArrayList<>(Arrays.asList(ASSISTANT, CHARACTER, GAMEBOARD, LOGIN, MENU, NICKNAME));
        for(String fxml : sceneFXML){
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            try {
                Parent root = loader.load();
                SceneController controller = loader.getController();
                controllers.put(fxml, controller);
                controller.setGui(this);
                final Scene scene = new Scene(scaleRootScene(root, fxml));
                scenes.put(fxml, scene);
            }
            catch(Exception e) {
                e.printStackTrace();
                return;
            }
        }

    }

    /**
     * Scales the provided {@link Parent} to screen resolution.
     * @param root {@link Parent} to be scaled.
     * @return the scaled {@link Parent}.
     */
    private Parent scaleRootScene(Parent root, String type){
        final int initWidth = 1920;      //initial width
        final int initHeight = 1080;    //initial height

        Dimension resolution = Toolkit.getDefaultToolkit().getScreenSize();
        double width = resolution.getWidth();
        double height = resolution.getHeight();
        Scale scale = new Scale(width/initWidth, height/initHeight, 0, 0);
        if(type.equals(MENU) || type.equals(LOGIN)) {
            scale.pivotXProperty().setValue(width/2);
            scale.pivotYProperty().setValue(height/2);
        } else {
            scale.pivotXProperty().setValue(0);
            scale.pivotYProperty().setValue(0);
        }

        root.getTransforms().add(scale);
        return root;
    }

    /**
     * Function called every time it's necessary change the current scene.
     * @param scene is the new scene that will be attached to the stage and showed.
     */
    public void changeScene(String scene) {

        //Every scene is already initialized and every controller already instantiated
        controller = controllers.get(scene);
        controller.setMessage(currentMessage);
        controller.createScene();
        currentScene = scenes.get(scene);
        resizeStage(stage);

    }

    /**
     * Sets size of stage to screen size.
     * @param stage to be shown.
     */
    private void resizeStage(Stage stage) {
        stage.setScene(currentScene);
        Dimension resolution = Toolkit.getDefaultToolkit().getScreenSize();
        double width = resolution.getWidth();
        double height = resolution.getHeight();
        stage.setWidth(width);
        stage.setHeight(height);
        stage.setResizable(false);
        stage.setMaximized(true);
        //stage.setFullScreen(true);
        stage.show();
    }

    /**
     * Asks the player to pick a nickname
     */
    @Override
    public void askNickName() {

        Platform.runLater(()-> {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Nickname selection");
            dialog.setHeaderText("Look, it seems we have a new wizard");
            dialog.setContentText("Please enter your battle name:");
            Tooltip tooltip = new Tooltip();
            tooltip.setText(
                    "Nickname must be at least\n" +
                    "1 characters in length!\n"
            );
            dialog.getEditor().setTooltip(tooltip);
            ImageView imageView = addImage("/images/miscellaneous/MAGO 1_1.jpg", false);
            dialog.setGraphic(imageView);

            Optional<String> result = dialog.showAndWait();
            if(result.isPresent()) {
                nickname = result.get();
                if(result.get().length() == 0)
                    askNickName();
                else
                    adapter.sendMessage(new RMessageNickname(nickname));
            }
            else
                askNickName();
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
            ImageView imageView = addImage("/images/miscellaneous/settingIcon.png", true);
            dialog.setGraphic(imageView);
            Optional<Integer> result = dialog.showAndWait();
            if(result.isPresent()) {
                numOfPlayer = result.get();
                List<String> choices2 = new ArrayList<>();
                choices2.add("For dummies");
                choices2.add("Expert");
                ChoiceDialog<String> dialog2 = new ChoiceDialog("For dummies", choices2);
                dialog2.setTitle("Game settings");
                dialog2.setHeaderText("Are you an expert or a noob?");
                dialog2.setContentText("Which version of the game do you want?");
                dialog2.setGraphic(imageView);
                Optional<String> result2 = dialog2.showAndWait();
                if(result2.isPresent()) {
                    if(result2.get().equals("Expert"))
                        expert = true;
                    adapter.sendMessage((new RMessageGameSettings(numOfPlayer, expert)));
                }
                else
                    askGameSettings();
            }
            else
                askGameSettings();
        });

    }

    @Override
    public void askMotherNatureMove(SMessageMotherNature message) {

        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Next move");
            alert.setHeaderText("Choose the destination of Mother Nature");
            String contentText = "You can move Mother Nature up to " + message.maxNumTiles + " island";
            if(message.maxNumTiles > 1)
                contentText += "s";
            alert.setContentText(contentText);
            ImageView imageView = addImage("/images/miscellaneous/MADRE NATURA_1.jpg", false);
            alert.setGraphic(imageView);
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
            alert.setHeaderText("The game cannot procede");
            alert.setContentText("A connection problem has occurred - closing application");
            ImageView imageView = addImage("/images/miscellaneous/connectionImage.jpg", true);
            alert.setGraphic(imageView);
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
            ImageView imageView = addImage("/images/characters/KNIGHT.jpg", false);
            alert.setGraphic(imageView);

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
            ImageView imageView = addImage("/images/cloud_card.png", true);
            alert.setGraphic(imageView);
            alert.showAndWait();
            try {
                changeScene(GAMEBOARD);
                GameBoardSceneController gameBoardSceneController = (GameBoardSceneController) controllers.get(GAMEBOARD);
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

                ImageView imageView = addImage("/images/miscellaneous/informationIcon.png", true);
                alert.setGraphic(imageView);
                alert.showAndWait();
            });
    }

    @Override
    public String getNickName() {
        return nickname;
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
            result.ifPresent(color -> adapter.sendMessage(new RMessageRogueMushroomPicker(characterChosen, nickname, color)));
        });
    }

    @Override
    public void jesterBardScene(SMessageJesterBard message) {

        ArrayList<Color> origin = new ArrayList<>(); //Selected students from the origin
        ArrayList<Color> entrance = new ArrayList<>(); //Selected students from the entrance
        Map<Color, Integer> currentEntrance;
        GameBoardSceneController gameBoardSceneController = (GameBoardSceneController) controllers.get(GAMEBOARD);
        currentEntrance = gameBoardSceneController.getEntranceColors();
        Platform.runLater(()->{

            List<Integer> numOfPossibleMoves = new ArrayList<>();
            numOfPossibleMoves.add(1);
            numOfPossibleMoves.add(2);
            int selectedMoves; //Num of moved students
            int currentMoves = 0;
            Characters chosenCharacter = message.characterName;
            String originText;
            String entranceText = "Select the student you want to remove from you entrance";

            if(chosenCharacter.equals(Characters.JESTER)) {
                numOfPossibleMoves.add(3);
                originText = "Select the student you want to take from the Jester";
            }
            else {
                originText = "Select the student you want to remove from the Dining Room";
            }

            ChoiceDialog<Integer> numOfMoves = new ChoiceDialog<>(1, numOfPossibleMoves);
            numOfMoves.setContentText("Choose the number of swaps you want to do");
            numOfMoves.setHeaderText("Choose a number");
            numOfMoves.setTitle("Disclaimer");
            Optional<Integer> result = numOfMoves.showAndWait();
            if (result.isPresent()) {
                selectedMoves = result.get();
                do {
                    List<Color> choices = new ArrayList<>();
                    for (Color color : message.origin.keySet())
                        if (message.origin.get(color) > 0) {
                            choices.add(color);
                        }

                    ChoiceDialog<Color> originDialog = new ChoiceDialog<>(choices.get(0), choices);
                    originDialog.setContentText(originText);
                    originDialog.setTitle("Disclaimer");
                    originDialog.setHeaderText("You have chosen " + characterChosen.name() + " card!");
                    Optional<Color> chosenColor = originDialog.showAndWait();
                    if (chosenColor.isPresent()) {
                        ArrayList<Color> availableEntranceColors = new ArrayList<>();
                        message.origin.put(chosenColor.get(), message.origin.get(chosenColor.get()) - 1);
                        origin.add(chosenColor.get());
                        for (Color color : currentEntrance.keySet())
                            if (currentEntrance.get(color) > 0)
                                availableEntranceColors.add(color);

                        ChoiceDialog<Color> entranceDialog = new ChoiceDialog<>(availableEntranceColors.get(0), availableEntranceColors);
                        entranceDialog.setContentText(entranceText);
                        entranceDialog.setTitle("Disclaimer");
                        entranceDialog.setHeaderText("Choose a Color");
                        Optional<Color> entranceResult = entranceDialog.showAndWait();
                        if (entranceResult.isPresent()) {
                            entrance.add(entranceResult.get());
                            currentEntrance.put(entranceResult.get(), currentEntrance.get(entranceResult.get()) - 1);
                            currentMoves += 1;
                        }

                    }

                } while (currentMoves < selectedMoves);

                adapter.sendMessage(new RMessageJesterBard(message.characterName, nickname, origin, entrance));
            }

        });
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

    /**
     * Utility method to avoid code duplication
     * @param imageURL is the image to add URL
     * @param isSquare indicates if the image to add has width and height equal
     * @return the imageView that will be added to the dialog
     */
    private ImageView addImage(String imageURL, boolean isSquare) {
        Image image = new Image(imageURL);
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(60);
        imageView.setFitHeight(70);
        if(isSquare)
            imageView.setFitWidth(70);
        return imageView;
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
