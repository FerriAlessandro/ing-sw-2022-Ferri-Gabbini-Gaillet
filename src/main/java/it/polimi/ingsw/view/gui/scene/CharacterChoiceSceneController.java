package it.polimi.ingsw.view.gui.scene;

import it.polimi.ingsw.model.enumerations.Characters;
import it.polimi.ingsw.model.enumerations.Color;
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
import java.util.Map;

public class CharacterChoiceSceneController implements SceneController {

    private Gui gui;
    private Characters characterChosen;
    private SMessageCharacter message;
    private String nickname;

    @FXML
    private GridPane gridPane;

    @FXML
    private ImageView stud11;
    @FXML
    private ImageView stud12;
    @FXML
    private ImageView stud13;
    @FXML
    private ImageView stud14;
    @FXML
    private ImageView stud15;
    @FXML
    private ImageView stud16;
    @FXML
    private ImageView stud21;
    @FXML
    private ImageView stud22;
    @FXML
    private ImageView stud23;
    @FXML
    private ImageView stud24;
    @FXML
    private ImageView stud25;
    @FXML
    private ImageView stud26;
    @FXML
    private ImageView stud31;
    @FXML
    private ImageView stud32;
    @FXML
    private ImageView stud33;
    @FXML
    private ImageView stud34;
    @FXML
    private ImageView stud35;
    @FXML
    private ImageView stud36;

    @FXML
    private Button cardOneButton;

    @FXML
    private Button cardThreeButton;

    @FXML
    private Button cardTwoButton;

    @FXML
    private Button noneButton;

    private final ArrayList<ImageView> imageNode = new ArrayList<>();


    private final Image redStudent = new Image("/images/student_red.png");
    private final Image greenStudent = new Image("/images/student_green.png");
    private final Image blueStudent = new Image("/images/student_blue.png");
    private final Image yellowStudent = new Image("/images/student_yellow.png");
    private final Image pinkStudent = new Image("/images/student_pink.png");
    private final Image noEntryTiles = new Image("/images/deny_island_icon.png");

    /**
     * It builds the scene with the available choice
     */
    @FXML
    void initialize() {
        for(int i = 1; i <= 3; i++){
            for(int j = 1; j <= 6 ; j++){
                try {
                    imageNode.add((ImageView) CharacterChoiceSceneController.class.getDeclaredField("stud" + i + j).get(this));
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
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
     * @param index     position on the gridPane
     */
    private void addCharacterCard(Characters character, int index) {
        boolean isPlayable = !(character.getCost() > gui.getCoins().get(nickname));
        Image characterToAdd = new Image("/images/characters/" + character + ".jpg");
        ImageView imageView = new ImageView();
        imageView.setFitHeight(569.5);
        imageView.setFitWidth(342.5);
        imageView.setImage(characterToAdd);

        switch (index) {
            case 0 -> attachImageToButton(cardOneButton, imageView, isPlayable);
            case 1 -> attachImageToButton(cardTwoButton, imageView, isPlayable);
            case 2 -> attachImageToButton(cardThreeButton, imageView, isPlayable);
            default -> {}
        }
        //TODO aggiungere la finestrina di informazioni quando si passa con il mouse su una delle carte
    }

    /**
     * Utility method that attaches the character card image to the button.
     * @param button is the button on which the image will be attached on.
     * @param imageView contains the image of the character card.
     * @param isPlayable indicates if the character card is playable.
     */
    private void attachImageToButton(Button button, ImageView imageView, boolean isPlayable) {
        button.setGraphic(imageView);
        if(isPlayable) {
            imageView.setCursor(Cursor.HAND);
            button.setDisable(false);
        }
        else {
            imageView.setCursor(Cursor.DEFAULT);
            imageView.setOpacity(0.25);
            button.setDisable(true);
        }

    }

    /**
     * send the effective message to the adapter.
     */
    private void sendChoice() {
        gui.setCharacter(characterChosen);
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
     * Display students on the cards that have them.
     */
    private void addStudents() {
        for (Characters character : message.effects)
            if (message.students.get(character) != null && !message.students.get(character).values().stream().allMatch(x -> x == 0)) {
                int studentAdded = 0;
                for (int j = 0, k = 1; j < 3; j++, k++) {
                    Map<Color, Integer> mapOfStudents = message.students.get((message.effects.get(j)));
                    for (Color color : mapOfStudents.keySet()) {
                        switch (color) {
                            case GREEN -> {
                                for (int i = 0; i < mapOfStudents.get(color); i++) {
                                    imageNode.get(studentAdded).setImage(greenStudent);
                                    studentAdded++;
                                }
                            }
                            case PINK -> {
                                for (int i = 0; i < mapOfStudents.get(color); i++) {
                                    imageNode.get(studentAdded).setImage(pinkStudent);
                                    studentAdded++;
                                }
                            }
                            case RED -> {
                                for (int i = 0; i < mapOfStudents.get(color); i++) {
                                    imageNode.get(studentAdded).setImage(redStudent);
                                    studentAdded++;
                                }
                            }
                            case YELLOW -> {
                                for (int i = 0; i < mapOfStudents.get(color); i++) {
                                    imageNode.get(studentAdded).setImage(yellowStudent);
                                    studentAdded++;
                                }
                            }
                            case BLUE -> {
                                for (int i = 0; i < mapOfStudents.get(color); i++) {
                                    imageNode.get(studentAdded).setImage(blueStudent);
                                    studentAdded++;
                                }
                            }

                        }
                    }
                    studentAdded = k * 6;
                }
            }
    }

    /**
     * Displays no entry tiles on grandmaHerb card, if present.
     */
    private void addNoEntryTiles() {
        int numOfNoEntryTiles;
        int grandMaIndex;
        int imageViewIndex;

        for(Characters character : message.effects) {
            if(character.equals(Characters.GRANDMA_HERB)) {
                numOfNoEntryTiles = message.noEntryTiles;
                grandMaIndex = message.effects.indexOf(character);
                imageViewIndex = grandMaIndex * 6;

                for(int i = 0; i < numOfNoEntryTiles; i++, imageViewIndex++)
                    imageNode.get(imageViewIndex).setImage(noEntryTiles);
            }
        }
    }


    /**
     * It builds the scene with the available choice
     */
    @Override
    public void createScene() {
        nickname = gui.getNickName();
        ArrayList<Characters> charactersAvailable = message.effects;
        int i = 0;
        for (Characters character : charactersAvailable) {
            addCharacterCard(character, i);
            i++;
        }
        addStudents();
        addNoEntryTiles();
    }
}
