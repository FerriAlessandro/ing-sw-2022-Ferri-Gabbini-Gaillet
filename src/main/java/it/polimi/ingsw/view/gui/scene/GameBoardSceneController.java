package it.polimi.ingsw.view.gui.scene;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.SMessageGameState;
import it.polimi.ingsw.network.messages.SMessageMotherNature;
import it.polimi.ingsw.view.gui.*;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Map;

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

    private ArrayList<FXisland> islands = new ArrayList<>();

    private ArrayList<FXPlayerBoard> playerBoards = new ArrayList<>();

    private ArrayList<FXCloud> clouds = new ArrayList<>();


    @FXML
    public void initialize(){
        for(int i=0; i<12; i++){
            islands.add(new FXisland(this, getIslandById(i+1)));
            islands.get(i).createIsland(i+1);
        }
        for(int i=0;i<3;i++){
            playerBoards.add(new FXPlayerBoard(i+1, this, 2, "Alessandro"));
            playerBoards.get(i).createPlayerBoard();
        }
        playerBoards.get(0).nickname = "Gui";
        player_name_1.setText("Alessandro");
        player_name_2.setText("Gabbo");
        player_name_3.setTextFill(Color.RED);
        player_name_3.setText("Angelo");
        player_name_1.setStyle("-fx-font-weight: bold;");
        player_name_2.setStyle("-fx-font-weight: bold;");
        player_name_3.setStyle("-fx-font-weight: bold;");
        for(int i=0;i<3;i++){
            clouds.add(new FXCloud(i+1, this, 2, getCloudById(i+1)));
            clouds.get(i).createCloud();
        }
        getPlayerBoardByNickname("Gui").addStudentEntrance(it.polimi.ingsw.model.enumerations.Color.RED);
        getPlayerBoardByNickname("Gui").addStudentEntrance(it.polimi.ingsw.model.enumerations.Color.GREEN);
        getPlayerBoardByNickname("Gui").addStudentEntrance(it.polimi.ingsw.model.enumerations.Color.BLUE);
        getPlayerBoardByNickname("Gui").addStudentEntrance(it.polimi.ingsw.model.enumerations.Color.PINK);
        getPlayerBoardByNickname("Gui").addStudentEntrance(it.polimi.ingsw.model.enumerations.Color.YELLOW);
        getPlayerBoardByNickname("Gui").addStudentEntrance(it.polimi.ingsw.model.enumerations.Color.PINK);
        System.out.println("Schifo refreshed");
        clouds.get(0).addStudentCloud(it.polimi.ingsw.model.enumerations.Color.RED);
        clouds.get(0).addStudentCloud(it.polimi.ingsw.model.enumerations.Color.BLUE);
        clouds.get(0).addStudentCloud(it.polimi.ingsw.model.enumerations.Color.GREEN);
    }


    @Override
    public void setGui(Gui gui) {
        this.gui = gui;
    }

    public Gui getGui(){
        return this.gui;
    }

    @Override
    public void setMessage(Message message) {

    }


    @Override
    public void createScene() {
    }

    public void getIslandChoice(){ //this one is used for a student's movement
        for(FXisland island : islands)
            island.makeSelectable();
    }

    public void getIslandChoice(FXStudent student){ //This one is used for a student's movement
        for(FXisland island : islands){
            island.makeSelectable(student);
        }
    }

    public void getEntranceChoice(){
        //FXPlayerBoard board = getPlayerBoardByNickname(getGui().getNickName());
        FXPlayerBoard board = getPlayerBoardByNickname("Gui");
        board.makeEntranceSelectable();
    }

    public void getCloudChoice(){
        for(FXCloud cloud : clouds){
            cloud.makeSelectable();
        }
    }

    public void removeSelectableCloud(){
        for(FXCloud cloud : clouds)
            cloud.removeSelectableCloud();
    }


    public void removeSelectableIslands(){
        for(FXisland island : islands)
            island.removeSelectable();
    }

    public void removeSelectableDiningRoom(it.polimi.ingsw.model.enumerations.Color color){
        getPlayerBoardByNickname(getGui().getNickName()).removeSelectableDiningRoom(color);
    }

    public void refreshEntrances(Map<String, Map<it.polimi.ingsw.model.enumerations.Color, Integer>> entrances){
       /* for(String nickname : entrances.keySet())
            getPlayerBoardByNickname(nickname).refreshEntrance(entrances.get(nickname));*/
        getPlayerBoardByNickname("Gui").refreshEntrance(entrances.get("Gui"));

    }

    public void refreshDiningRooms(Map<String, Map<it.polimi.ingsw.model.enumerations.Color, Integer>> diningRooms){
       /* for(String nickname : diningRooms.keySet())
            getPlayerBoardByNickname(nickname).refreshDiningRooms(diningRooms.get(nickname));*/
        getPlayerBoardByNickname("Gui").refreshDiningRooms(diningRooms.get("Gui"));
    }

    public void refreshProfessors(Map<it.polimi.ingsw.model.enumerations.Color, String> professors){
        /*for(String nickname : professors.values())
            getPlayerBoardByNickname(nickname).refreshProfessors(professors);*/
        getPlayerBoardByNickname("Gui").refreshProfessors(professors);

    }

    public void refreshTowerZones(Map<String, Integer> towers){
       /* for(String nickname : towers.keySet())
            getPlayerBoardByNickname(nickname).refreshTowerZones(towers.get(nickname));*/
        getPlayerBoardByNickname("Gui").refreshTowerZones(towers.get("Gui"));
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
        for(FXPlayerBoard playerboard : playerBoards)
            if(playerboard.getNickname().equals(nickname))
                return playerboard;
            return null;
    }

}

