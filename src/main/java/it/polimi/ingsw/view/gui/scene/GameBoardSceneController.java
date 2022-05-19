package it.polimi.ingsw.view.gui.scene;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.SMessageMotherNature;
import it.polimi.ingsw.view.gui.FXCloud;
import it.polimi.ingsw.view.gui.FXPlayerBoard;
import it.polimi.ingsw.view.gui.FXisland;
import it.polimi.ingsw.view.gui.Gui;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

import java.util.ArrayList;

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
            playerBoards.add(new FXPlayerBoard(i+1, this, 3));
            playerBoards.get(i).createPlayerBoard();
        }
        player_name_1.setText("Alessandro");
        player_name_2.setText("Gabbo");
        player_name_3.setTextFill(Color.RED);
        player_name_3.setText("Angelo");
        player_name_1.setStyle("-fx-font-weight: bold;");
        player_name_2.setStyle("-fx-font-weight: bold;");
        player_name_3.setStyle("-fx-font-weight: bold;");
        for(int i=0;i<3;i++){
            clouds.add(new FXCloud(i+1, this, 2));
            clouds.get(i).createCloud();
        }





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

    public void makeIslandsSelectable(){
        for(FXisland island : islands)
            island.makeSelectable();
    }

    public void removeIslandsSelectable(){
        for(FXisland island : islands)
            island.removeSelectable();
    }

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
            default -> null;
        };
    }
}

