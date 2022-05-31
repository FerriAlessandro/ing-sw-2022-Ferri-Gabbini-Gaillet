package it.polimi.ingsw.view.gui.scene;
import it.polimi.ingsw.model.enumerations.TowerColor;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.SMessageAssistantStatus;
import it.polimi.ingsw.network.messages.SMessageGameState;
import it.polimi.ingsw.view.gui.*;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.*;

/**
 * This class represents the main game scene
 * @author Alessandro F.
 * @version 1.0
 */
public class GameBoardSceneController implements SceneController {

    private Gui gui;
    @FXML
    public Pane mainPane;

    @FXML
    private ImageView background;

    @FXML
    private ImageView chosen_assistant_1;

    @FXML
    private ImageView chosen_assistant_2;

    @FXML
    private ImageView chosen_assistant_3;

    @FXML
    private ImageView cloud_1;

    @FXML
    private ImageView cloud_2;

    @FXML
    private ImageView cloud_3;

    @FXML
    private ImageView island_1;

    @FXML
    private ImageView island_2;

    @FXML
    private ImageView island_3;

    @FXML
    private ImageView island_4;

    @FXML
    private ImageView island_5;

    @FXML
    private ImageView island_6;

    @FXML
    private ImageView island_7;

    @FXML
    private ImageView island_8;

    @FXML
    private ImageView island_9;

    @FXML
    private ImageView island_10;

    @FXML
    private ImageView island_11;

    @FXML
    private ImageView island_12;

    @FXML
    private Label player_name_1;

    @FXML
    private Label player_name_2;

    @FXML
    private Label player_name_3;

    @FXML
    private ImageView playerboard_1;

    @FXML
    private ImageView playerboard_2;

    @FXML
    private ImageView playerboard_3;

    @FXML
    private Label hintsLabel;

    @FXML
    private ImageView speaker;

    private final ArrayList<FXisland> islands = new ArrayList<>();

    private final ArrayList<FXPlayerBoard> playerBoards = new ArrayList<>();

    private final ArrayList<FXCloud> clouds = new ArrayList<>();

    private final ArrayList<Label> nicknames = new ArrayList<>();
    



    /**
     * Initialize Method, it's called by JavaFX when the FXML is loaded, it constructs everything that does not depend on parameters
     */
    @FXML
    public void initialize() {

        for (int i = 0; i < 12; i++) {
            islands.add(new FXisland(this, getIslandById(i + 1)));
            islands.get(i).createIsland(i + 1);

        }
        chosen_assistant_1.setVisible(false);
        chosen_assistant_2.setVisible(false);
        chosen_assistant_3.setVisible(false);
        hintsLabel.setFont(new Font("Arial", 17));
        hintsLabel.setStyle("-fx-font-weight: bold;");
        
    }

    /**
     * Setup method, it's used to create the parts of the board that depend on parameters given by the user
     * @param numOfPlayers Number of players in the game
     * @param expert Flag to represent an expert game
     * @param firstGameState First message that comunicates the state of the board (entrances, clouds, motherNature position, students on islands)
     */
    public void setupGameBoard(int numOfPlayers, boolean expert, SMessageGameState firstGameState){

        ArrayList<String> nicknames = new ArrayList<>(firstGameState.towerColor.keySet());
        for(String name : nicknames){
            if(firstGameState.towerColor.get(name).equals(TowerColor.WHITE))
                player_name_1.setText(name);
            else if(firstGameState.towerColor.get(name).equals(TowerColor.BLACK))
                player_name_2.setText(name);
            else player_name_3.setText(name);
        }
        nicknames = new ArrayList<>();
        nicknames.add(player_name_1.getText());
        nicknames.add(player_name_2.getText());
        nicknames.add(player_name_3.getText());
        this.nicknames.add(player_name_1);
        this.nicknames.add(player_name_2);
        this.nicknames.add(player_name_3);

        if (numOfPlayers != 3) {
            playerboard_3.setVisible(false);
            cloud_3.setVisible(false);
        }

        for(int i=0;i<numOfPlayers;i++){
            playerBoards.add(new FXPlayerBoard(i+1, this, numOfPlayers, nicknames.get(i)));
            playerBoards.get(i).createPlayerBoard();
            if(expert)
                playerBoards.get(i).createCoins();

            clouds.add(new FXCloud(i+1, this, nicknames.size(), getCloudById(i+1)));
            clouds.get(i).createCloud();
        }

        for(Label label : this.nicknames)
            label.setStyle("-fx-font-weight: bold;");



    }


    @Override
    public void setGui(Gui gui) {
        this.gui = gui;
    }

    public Gui getGui(){
        return this.gui;
    }

    public Label getHintsLabel(){return this.hintsLabel;}


    @Override
    public void setMessage(Message message) {

    }


    @Override
    public void createScene() {
    }

    /**
     * Method called when the user needs to select an island as a destination for mother nature, it makes the island clickable and, if clicked, moves mother nature there
     */
    public void getIslandChoice(){ //this one is used for a student's movement
        for(FXisland island : islands)
            island.makeSelectable();
    }

    /**
     * Method called when the user needs to select an island as a choice for character card Monk.
     * @param color is the color of the student that will be added on the chosen island.
     */
    public void getIslandChoiceMonk(it.polimi.ingsw.model.enumerations.Color color) {
        for(FXisland island : islands)
            island.makeSelectableMonk(color);
    }

    /**
     * Method called when the user needs to select an island as a choice for character card GrandmaHerb or Herald.
     */
    public void getIslandChoiceGrandmaHerald() {
        for(FXisland island : islands)
            island.makeSelectableGrandmaHerald();
    }

    /**
     * Method called when the user needs to select an island as a destination for a student, it makes the island clickable and, if clicked, puts the student there
     */
    public void getIslandChoice(FXStudent student){ //This one is used for a student's movement
        for(FXisland island : islands){
            island.makeSelectable(student);
        }
    }

    /**
     * Method called when the user needs to select a student from the entrance, it makes the entrance selectable and, if clicked, makes the dining Room and islands selectable as destinations
     */
    public void getEntranceChoice(){
        FXPlayerBoard board = getPlayerBoardByNickname(getGui().getNickName());
        assert board != null;
        board.makeEntranceSelectable();
    }

    /**
     * Method called when the user needs to select a student from the cloud, it makes the cloud selectable and, if clicked, moves the students from the cloud to the player's entrance
     */
    public void getCloudChoice(){
        for(FXCloud cloud : clouds){
            cloud.makeSelectable();
        }
    }

    /**
     * @return A Map containing the Entrance's students
     */
    public HashMap<it.polimi.ingsw.model.enumerations.Color, Integer> getEntranceColors(){
        return Objects.requireNonNull(getPlayerBoardByNickname(getGui().getNickName())).getEntranceColors();
    }

    /**
     * Makes a cloud not selectable
     */
    public void removeSelectableCloud(){
        for(FXCloud cloud : clouds)
            cloud.removeSelectableCloud();
    }

    /**
     * Makes an island not selectable
     */
    public void removeSelectableIslands(){
        for(FXisland island : islands)
            island.removeSelectable();
    }

    /**
     * Makes a dining room of a color not selectable
     * @param color The color of the dining room
     */
    public void removeSelectableDiningRoom(it.polimi.ingsw.model.enumerations.Color color){
        Objects.requireNonNull(getPlayerBoardByNickname(getGui().getNickName())).removeSelectableDiningRoom(color);
    }

    /**
     * Updates the entrances with the informations sent from the server
     * @param entrances The map containing the updates, maps a user's nickname to another Map that maps a color with the number of students of that color
     */
    public void refreshEntrances(Map<String, Map<it.polimi.ingsw.model.enumerations.Color, Integer>> entrances){
        for(String nickname : entrances.keySet())
            Objects.requireNonNull(getPlayerBoardByNickname(nickname)).refreshEntrance(entrances.get(nickname));

    }

    /**
     * Updates the Dining Rooms with the informations sent from the server
     * @param diningRooms The map containing the updates, maps a user's nickname to another Map that maps a color with the number of students of that color
     */
    public void refreshDiningRooms(Map<String, Map<it.polimi.ingsw.model.enumerations.Color, Integer>> diningRooms){
        for(String nickname : diningRooms.keySet())
            Objects.requireNonNull(getPlayerBoardByNickname(nickname)).refreshDiningRooms(diningRooms.get(nickname));
    }

    /**
     * Updates the Professors Zone with the informations sent from the server
     * @param professors The map containing the updates, maps a color to the nickname of the owner of the professor of that color
     */
    public void refreshProfessors(Map<it.polimi.ingsw.model.enumerations.Color, String> professors){
        for(String nickname : professors.values())
            Objects.requireNonNull(getPlayerBoardByNickname(nickname)).refreshProfessors(professors);

    }

    /**
     * Updates the Towers Zone with the informations sent from the server
     * @param towers The map containing the updates, maps a user's nickname to the number of towers in its dining room
     */
    public void refreshTowerZones(Map<String, Integer> towers){
       for(String nickname : towers.keySet())
            Objects.requireNonNull(getPlayerBoardByNickname(nickname)).refreshTowerZones(towers.get(nickname));
    }

    /**
     * Updates the islands with the informations sent from the server (Updates the students)
     * @param islands The map containing the updates, maps an island's index to a Map that maps a color to the number of students of that color on the island
     */
    public void refreshIslandsStudents(Map<Integer, Map<it.polimi.ingsw.model.enumerations.Color, Integer>> islands){
        for(int i=islands.keySet().size(); i<12;i++)
            this.islands.get(i).hideIsland();
        for(Integer index : islands.keySet()){
            this.islands.get(index).refreshStudents(islands.get(index));
        }
    }

    /**
     * Updates the islands with the informations sent from the server (Updates the towers color)
     * @param islandsTowerColor The map containing the updates, maps an island's index to the color of the tower on that island
     */
    public void refreshIslandsTowersColor(Map<Integer, TowerColor> islandsTowerColor){
        for(Integer index : islandsTowerColor.keySet())
            this.islands.get(index).refreshIslandsTowersColor(islandsTowerColor.get(index));
    }

    /**
     * Updates the islands with the informations sent from the server (updates the towers number)
     * @param islandsTowersNum The map containing the updates, maps an island's index to the number of towers on that island
     */
    public void refreshIslandsTowersNum(Map<Integer, Integer> islandsTowersNum){
        for(Integer index : islandsTowersNum.keySet()){
            this.islands.get(index).refreshIslandsTowersNum(islandsTowersNum.get(index));
        }
    }

    /**
     * Updates mother nature's position
     * @param position The index of the island containing mother nature
     */
    public void refreshMotherNature(int position){
        for(FXisland island : islands){
            island.refreshMotherNature(position);
        }
    }

    /**
     * Updates the islands with the informations sent from the server (Updates the number of No Entry Tiles)
     * @param tokens The map containing the updates, maps an island's index to the number of No Entry Tiles on that island
     */
    public void refreshNoEntryTiles(Map<Integer, Integer> tokens){
        for(Integer index : tokens.keySet())
            islands.get(index).refreshNoEntryTiles(tokens.get(index));
    }

    /**
     * Updates the clouds with the informations sent from the server
     * @param clouds The map containing the updates, maps a cloud's index to a Map that maps a color to the number of students of that color on the cloud
     */
    public void refreshClouds(Map<Integer, Map<it.polimi.ingsw.model.enumerations.Color, Integer>> clouds){
        for(Integer index : clouds.keySet()){
            this.clouds.get(index).refreshClouds(clouds.get(index));
        }
    }

    /**
     * Updates the number of coins of each player
     * @param coins The map containing the coins, maps a player's nickname to its number of coins
     */
    public void refreshCoins(Map<String, Integer> coins){
        for(String nickname : coins.keySet()){
            Objects.requireNonNull(getPlayerBoardByNickname(nickname)).refreshCoins(coins.get(nickname));

        }
    }

    /**
     * Updates the Assistant Card image based on the player's played assistant
     * @param message The message containing the played assistant and the nickname of the player who played it
     */
    public void refreshAssistant (SMessageAssistantStatus message){
        String imagePath = "images/"+message.chosenAssistant.toString().toUpperCase(Locale.ROOT)+".png";
        if(message.nickname.equals(player_name_1.getText())) {
            chosen_assistant_1.setImage(new Image(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(imagePath))));
            chosen_assistant_1.setVisible(true);
        }

        else if(message.nickname.equals(player_name_2.getText())) {
            chosen_assistant_2.setImage(new Image(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(imagePath))));
            chosen_assistant_2.setVisible(true);
        }

        else if(message.nickname.equals(player_name_3.getText())){
            chosen_assistant_3.setImage(new Image(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(imagePath))));
            chosen_assistant_3.setVisible(true);
        }
    }

    /**
     * Colors the nickname of the current player
     * @param nickname The nickname of the current player
     */
    public void colorCurrentPlayer(String nickname){
        for(Label playername : nicknames){
            if(playername.getText().toLowerCase(Locale.ROOT).equals(nickname.toLowerCase(Locale.ROOT)))
                playername.setTextFill(Color.RED);
            else playername.setTextFill(Color.BLACK);
        }
    }


    // ---------------------UTILITY METHODS -----------------------
    private ImageView getIslandById(int id){
        return switch (id) {
            case 1 -> island_1;
            case 2 -> island_2;
            case 3 -> island_3;
            case 4 -> island_4;
            case 5 -> island_5;
            case 6 -> island_6;
            case 7 -> island_7;
            case 8 -> island_8;
            case 9 -> island_9;
            case 10 -> island_10;
            case 11 -> island_11;
            case 12 -> island_12;
            default->null;
        };
    }

    private ImageView getCloudById(int id){
        return switch(id){
            case 1->cloud_1;
            case 2->cloud_2;
            case 3->cloud_3;
            default->null;
        };
    }

    private FXPlayerBoard getPlayerBoardByNickname(String nickname){
        for(FXPlayerBoard playerBoard : playerBoards)
            if(playerBoard.getNickname().equals(nickname))
                return playerBoard;
        return null;
    }

}

