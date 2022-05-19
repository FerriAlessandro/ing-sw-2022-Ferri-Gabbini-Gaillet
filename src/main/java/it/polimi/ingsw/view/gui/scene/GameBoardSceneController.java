package it.polimi.ingsw.view.gui.scene;

import it.polimi.ingsw.view.gui.FXCloud;
import it.polimi.ingsw.view.gui.FXPlayerBoard;
import it.polimi.ingsw.view.gui.FXisland;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import org.w3c.dom.css.Rect;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class GameBoardSceneController {

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
    private ImageView island_10;

    @FXML
    private ImageView island_11;

    @FXML
    private ImageView island_12;

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
            islands.add(new FXisland(this));
            islands.get(i).createIsland(i+1);
        }
        for(int i=0;i<3;i++){
            playerBoards.add(new FXPlayerBoard(i+1, this, 3));
            playerBoards.get(i).createPlayerBoard();
        }
        player_name_1.setText("Alessandro");
        player_name_2.setText("Gabbo");
        player_name_3.setText("Angelo");
        player_name_1.setStyle("-fx-font-weight: bold;");
        player_name_2.setStyle("-fx-font-weight: bold;");
        player_name_3.setStyle("-fx-font-weight: bold;");
        for(int i=0;i<3;i++){
            clouds.add(new FXCloud(i+1, this, 2));
            clouds.get(i).createCloud();
        }





    }



}
