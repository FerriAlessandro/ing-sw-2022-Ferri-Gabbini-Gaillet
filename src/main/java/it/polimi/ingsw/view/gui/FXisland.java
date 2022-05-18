package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.view.gui.scene.GameBoardSceneController;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.Objects;

public class FXisland {

    private final Coordinates towers = new Coordinates(162.5, 92); //Top middle circle of the first island of the first row
    private final Coordinates green_student = new Coordinates(127, 138); //Center-left student, the green one, we save these coordinates as a starting point for the cycle
    private int index;
    private final ArrayList<Circle> pawns = new ArrayList<>(); //Index 0 is the tower, index 1 is mothernature, index 2 is green student, index 3 is red, index 4 is yellow, index 5 is pink, index 6 is blue, index 7 is NoEntryTile
    private final ArrayList<Label> quantity = new ArrayList<>(); //Same as Pawns, but with the quantity of each pawn
    private final GameBoardSceneController gameBoardSceneController;
    private double islandsHorizontalDistance = 205;
    private double islandsVerticalDistance = 205;
    private double pawnsRadius = 12.5;
    private double labelsVerticalOffset = 10.5;
    private double labelsHorizontalOffset = 12.5;
    private double pawnsHorizontalOffset = 35.5;
    private double pawnsVerticalOffset = 46;

    public FXisland(GameBoardSceneController controller){
        this.gameBoardSceneController = controller;

    }
    public void createIsland(int index){

        this.index = index;
        double offset = islandsHorizontalDistance * (index-1); //We multiply the starting coordinates by (205 * 1/2/3/4/5) to get the right coordinates of each island [205 is the space between two islands]; Island with index 1 doesn't have an offset


        if(index >= 7) {
            towers.setY(towers.getY() + islandsVerticalDistance); //top middle circle of the first island of the second row
            offset = islandsHorizontalDistance * (index % 7); //island 7 doesn't have an offset, is below island 1
            green_student.setY(green_student.getY() + islandsVerticalDistance);
        }


        pawns.add(new Circle(towers.getX() + offset, towers.getY(), pawnsRadius)); //Tower (12.5 is the radius)
        quantity.add(new Label());
        quantity.get(0).setLayoutX(towers.getX() + offset - labelsHorizontalOffset); //each label is shifted of -12.5 in X and +10.5 on Y from its pawn's coordinates
        quantity.get(0).setLayoutY(towers.getY() + labelsVerticalOffset);


        pawns.add(new Circle((towers.getX() + pawnsHorizontalOffset) + offset, towers.getY() , pawnsRadius)); //MotherNature
        quantity.add(new Label());
        quantity.get(1).setLayoutX(towers.getX() + pawnsHorizontalOffset + offset - labelsHorizontalOffset);
        quantity.get(1).setLayoutY(towers.getY() + labelsVerticalOffset);

        double horizontal_offset = 0;
        double vertical_offset = 0;
        int labelCounter=2;
        for(int j=0; j<2 ;j++, vertical_offset += pawnsVerticalOffset){ //Vertical
            horizontal_offset = 0;

            for(int i=0; i<3;i++, horizontal_offset += pawnsHorizontalOffset, labelCounter++){
                pawns.add(new Circle(green_student.getX() + horizontal_offset + offset, green_student.getY() + vertical_offset, pawnsRadius ));
                quantity.add(new Label());
                quantity.get(labelCounter).setLayoutX(green_student.getX() +  horizontal_offset + offset - labelsHorizontalOffset);
                quantity.get(labelCounter).setLayoutY(green_student.getY() + vertical_offset + labelsVerticalOffset);

            }
        }

        for(int i=0;i<8;i++) {
            gameBoardSceneController.mainPane.getChildren().add(pawns.get(i));
            gameBoardSceneController.mainPane.getChildren().add(quantity.get(i));
        }
        colorPawns();



    }

    private void colorPawns(){

        Image motherNature = new Image(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("images/mother_nature.png")));
        Image noEntry = new Image(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("images/deny_island_icon.png")));
        Image green = new Image(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("images/student_green.png")));
        Image red = new Image(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("images/student_red.png")));
        Image yellow = new Image(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("images/student_yellow.png")));
        Image pink = new Image(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("images/student_pink.png")));
        Image blue = new Image(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("images/student_blue.png")));

        pawns.get(1).setFill(new ImagePattern(motherNature));
        pawns.get(2).setFill(new ImagePattern(green));
        pawns.get(3).setFill(new ImagePattern(red));
        pawns.get(4).setFill(new ImagePattern(yellow));
        pawns.get(5).setFill(new ImagePattern(pink));
        pawns.get(6).setFill(new ImagePattern(blue));
        pawns.get(7).setFill(new ImagePattern(noEntry));


        for(Circle pawn : pawns) {
            if(pawns.indexOf(pawn) != 0 && pawns.indexOf(pawn) != 1)
                pawn.setStroke(Color.BLACK);

            pawn.setVisible(false);
        }

        for(Label label : quantity){
            label.setVisible(false);
            label.setFont(new Font("Arial", 15));
            label.setStyle("-fx-font-weight: bold;");
        }


    }




}
