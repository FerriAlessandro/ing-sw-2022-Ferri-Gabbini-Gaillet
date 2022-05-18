package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.model.enumerations.Color;
import it.polimi.ingsw.model.enumerations.TowerColor;
import it.polimi.ingsw.view.gui.scene.GameBoardSceneController;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.transform.Rotate;

import java.util.*;

public class FXPlayerBoard {

    private TowerColor towerColor;
    private Label nickname;
    int boardNumber; //Number of the board to set the right coordinates for the pawns
    private final ArrayList<FXStudent> entrance = new ArrayList<>();
    private final Map<Color, ArrayList<FXStudent>> diningRooms = new HashMap<>();
    private final Map<Color, FXStudent> professorZone = new HashMap<>();
    private final ArrayList<Circle> towerZone = new ArrayList<>();
    private final GameBoardSceneController gameBoardSceneController;
    private final Coordinates startingEntrance = new Coordinates(68,745.5); // First pawn of the entrance (Top left one), there is no pawn there
    private final Coordinates startingDiningRoom = new Coordinates(162, 745.5); // First pawn of the Dining Room (Top left one, the first green)
    private final Coordinates startingProfessor = new Coordinates(480, 745.5); //First pawn of the professors zone (green professor)
    private final Coordinates startingTower = new Coordinates(545,789); //First pawn of the tower zone (top left one)
    private final Coordinates label = new Coordinates (280, 628); //Coordinates of the nickname label
    private final double entranceHorizontalOffset = 36;
    private final double entranceVerticalOffset = 43.5;
    private final double diningHorizontalOffset = 29;
    private final double diningVerticaloffset = 43;
    private final double professorsVerticalOffset = 43.5;
    private final double playerBoardsHorizontalOffset = 620;
    private final double towersHorizontalOffset = 35;
    private final double towersVerticalOffset = 43;
    private final double offset;
    private final double radius = 12.5;
    private final double professorsRadius = 16.5;



    public FXPlayerBoard(String nickname, int boardNumber, GameBoardSceneController gameBoardSceneController){
        this.offset = playerBoardsHorizontalOffset * (boardNumber - 1);
        this.boardNumber = boardNumber;
        this.gameBoardSceneController = gameBoardSceneController;
        this.nickname = new Label(nickname);
        this.nickname.setLayoutX(label.getX() + offset);
        this.nickname.setLayoutY(label.getY());
        this.nickname.setFont(new Font("Arial", 35));
        this.nickname.setStyle("-fx-font-weight: bold;");
        this.gameBoardSceneController.mainPane.getChildren().add(this.nickname);
        if(boardNumber == 1)
            towerColor = TowerColor.WHITE;
        else if(boardNumber == 2)
            towerColor = TowerColor.BLACK;
        else towerColor = TowerColor.GRAY;

    }

    public void createPlayerBoard(){

        createEntrance();
        createDiningRoom();
        createProfessorZone();
        createTowerZone();
    }

    private void createEntrance(){ //We create every circle and set them to not visibile, when the player adds a pawn to the entrance i simply search for the first "not visible" circle and set it visibile with the right color sprite

        double horizontal_offset = 0;
        double vertical_offset = 0;

        for(int i=0; i<2; i++, horizontal_offset+=entranceHorizontalOffset){
            vertical_offset = 0;
            for (int j=0;j<5;j++, vertical_offset += entranceVerticalOffset){
                if(i!=0 || j!= 0) //The top left entrance spot is empty
                    entrance.add(new FXStudent(startingEntrance.getX() + offset + horizontal_offset , startingEntrance.getY() + vertical_offset, radius));
            }
        }

        for (int i=0;i<9;i++) {
            gameBoardSceneController.mainPane.getChildren().add(entrance.get(i));
            entrance.get(i).setVisible(false);
        }

    }

    private void createDiningRoom(){

        double horizontal_offset = 0;
        double vertical_offset = 0;
        String imageColor;
        String imagePath;

        for(Color color : Color.values()){
            vertical_offset = getVerticalOffset(color);

            diningRooms.put(color, new ArrayList<>());
            imageColor = color.toString().toLowerCase(Locale.ROOT);
            imagePath = "images/student_" + imageColor + ".png";
            Image studentImage = new Image(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(imagePath)));
            for(int i=0; i<10; i++, horizontal_offset+=diningHorizontalOffset) {
                diningRooms.get(color).add(new FXStudent(startingDiningRoom.getX() + offset + horizontal_offset, startingDiningRoom.getY() + vertical_offset, radius));
                diningRooms.get(color).get(i).setColor(color);
                diningRooms.get(color).get(i).setFill(new ImagePattern(studentImage));
                diningRooms.get(color).get(i).setStroke(javafx.scene.paint.Color.BLACK);
                gameBoardSceneController.mainPane.getChildren().add(diningRooms.get(color).get(i));


               // diningRooms.get(color).get(i).setVisible(false);

            }
            horizontal_offset = 0;

        }


    }

    private void createProfessorZone(){

        String imageColor;
        String imagePath;
        double verticalOffset = 0;
        for(Color color : Color.values()){
            verticalOffset = getVerticalOffset(color);
            imageColor = color.toString().toLowerCase(Locale.ROOT);
            imagePath = "images/teacher_" + imageColor + ".png";
            Image professorImage = new Image(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(imagePath)));
            professorZone.put(color, new FXStudent(startingProfessor.getX() + offset, startingProfessor.getY() + verticalOffset + 0.4, professorsRadius));
            professorZone.get(color).setColor(color);
            professorZone.get(color).setFill(new ImagePattern(professorImage));
            professorZone.get(color).setRotate(90);
            gameBoardSceneController.mainPane.getChildren().add(professorZone.get(color));


            //professorZone.get(color).setVisible(false);

        }

    }

    private void createTowerZone(){

        double horizontalOffset = 0;
        double verticalOffset = 0;
        int counter = 0;
        String imageColor;
        String imagePath;
        imageColor = towerColor.toString().toLowerCase(Locale.ROOT);
        imagePath = "images/"+imageColor+"_tower.png";

        for(int i=0; i<3; i++, horizontalOffset+=towersHorizontalOffset){
            verticalOffset=0;
            for(int j=0; j<3;j++, verticalOffset += towersVerticalOffset, counter++){
                if(i!= 2 || j!= 2){
                    towerZone.add(new Circle(startingTower.getX() + offset + horizontalOffset, startingTower.getY() + verticalOffset, professorsRadius));
                    Image towerImage = new Image(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(imagePath)));
                    towerZone.get(counter).setFill(new ImagePattern(towerImage));
                    gameBoardSceneController.mainPane.getChildren().add(towerZone.get(counter));
                }
            }
        }
    }

    private double getVerticalOffset(Color color){
        double vertical_offset;
        switch(color){
            case GREEN:
                vertical_offset=0;
                break;
            case RED:
                vertical_offset=diningVerticaloffset;
                break;
            case YELLOW:
                vertical_offset=diningVerticaloffset*2;
                break;
            case PINK:
                vertical_offset=diningVerticaloffset*3;
                break;
            default: vertical_offset = diningVerticaloffset*4;
        }
        return vertical_offset;
    }

}
