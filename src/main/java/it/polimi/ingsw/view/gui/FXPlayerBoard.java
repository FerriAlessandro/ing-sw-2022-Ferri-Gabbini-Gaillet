package it.polimi.ingsw.view.gui;
import it.polimi.ingsw.model.enumerations.Color;
import it.polimi.ingsw.model.enumerations.TowerColor;
import it.polimi.ingsw.network.messages.RMessageMove;
import it.polimi.ingsw.view.gui.scene.GameBoardSceneController;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;

import java.util.*;

/**
 * This class is used to store and modify the students, professors and towers on a player's playerboard
 * @author Alessandro F.
 * @version 1.0
 */

public class FXPlayerBoard {

    private TowerColor towerColor;
    public String nickname;
    private final int boardNumber; //Number of the board to set the right coordinates for the pawns
    private final int numOfPlayers;

    private final ArrayList<FXStudent> entrance = new ArrayList<>();
    private final HashMap<Color, ArrayList<FXStudent>> diningRooms = new HashMap<>();
    private final HashMap<Color, FXStudent> professorZone = new HashMap<>();
    private final ArrayList<Circle> towerZone = new ArrayList<>();
    private Circle coins;
    private Label coinsNum;
    private final GameBoardSceneController gameBoardSceneController;
    private final Coordinates startingEntrance = new Coordinates(68,745.5); // First pawn of the entrance (Top left one), there is no pawn there
    private final Coordinates startingDiningRoom = new Coordinates(162, 745.5); // First pawn of the Dining Room (Top left one, the first green)
    private final Coordinates startingProfessor = new Coordinates(480, 745.5); //First pawn of the professors zone (green professor)
    private final Coordinates startingTower = new Coordinates(545,789); //First pawn of the tower zone (top left one)
    private final Coordinates label = new Coordinates (280, 628); //Coordinates of the nickname label
    private final double entranceHorizontalOffset = 36; //Offset between pawns in the entrance
    private final double entranceVerticalOffset = 43.5;//Offset between pawns in the entrance
    private final double diningHorizontalOffset = 29;//Offset between pawns in the Dining Room
    private final double diningVerticaloffset = 43;//Offset between pawns in the Dining Room
    private final double professorsVerticalOffset = 43.5; //Offset between pawns in the Professors' area
    private final double playerBoardsHorizontalOffset = 620; //Offset between playerboards
    private final double towersHorizontalOffset = 35; //Offset between pawns in the Tower Zone
    private final double towersVerticalOffset = 43; //Offset between pawns in the Tower Zone
    private final double offset; //Offset of a given playerboard from the first one
    private final double radius = 12.5; //Radius of the circles that represent the students
    private final double professorsRadius = 16.5; //Radius of the circles that represent professors and towers


    /**
     * Constructor
     * @param boardNumber Number of the playerBoard (1,2 or 3)
     * @param gameBoardSceneController Controller that manages the GameBoard scene
     * @param numOfPlayers Number of players in the game
     */
    public FXPlayerBoard(int boardNumber, GameBoardSceneController gameBoardSceneController, int numOfPlayers, String nickname){
        this.nickname = nickname;
        this.numOfPlayers = numOfPlayers;
        this.offset = playerBoardsHorizontalOffset * (boardNumber - 1);
        this.boardNumber = boardNumber;
        this.gameBoardSceneController = gameBoardSceneController;
        if(boardNumber == 1)
            towerColor = TowerColor.WHITE;
        else if(boardNumber == 2)
            towerColor = TowerColor.BLACK;
        else towerColor = TowerColor.GRAY;

    }

    /**
     * Method used to create a full PlayerBoard
     */
    public void createPlayerBoard(){

        createEntrance();
        createDiningRoom();
        createProfessorZone();
        createTowerZone();
    }

    /**
     * Helper method used to create an Entrance, it creates the circles representing the students and adds them in an ArrayList (to be able to retrieve them when needed), then
     * it adds them to the Scene Tree
     */
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
            entrance.get(i).setOpacity(0); //Same as setVisible(false), but with opacity mouse events can be triggered
            entrance.get(i).setStroke(javafx.scene.paint.Color.BLACK);

        }

    }

    /**
     * Helper method used to create an Dining Room, it creates the circles representing the students and adds them in an ArrayList and stores the
     * ArrayList in a Map. The Circles are already filled with the correct sprites and are hidden, when a student needs to appear to the board it will be set as Visible.
     * The students are added to the Scene Tree
     */
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


                diningRooms.get(color).get(i).setOpacity(0.0); //We can't use set visible here, i need them to be clickable even if they're not visibile... SetVisibile(false) disables mouse events

            }
            horizontal_offset = 0;

        }


    }

    /**
     * Helper method used to create the professor zone, it creates the circles representing the professors and adds them in a Map, then it adds them to the Scene Tree.
     * The sprites are already set and the pawns are hidden until they need to be shown
     */
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


            professorZone.get(color).setOpacity(0);

        }

    }
    /**
     * Helper method used to create the Tower Zone, it creates the circles representing the towers and adds them in an ArrayList, then it
     * adds them to the Scene Tree
     */
    private void createTowerZone(){

        double horizontalOffset = 0;
        double verticalOffset = 0;
        int counter = 0;
        int numOfRows;
        String imageColor;
        String imagePath;
        imageColor = towerColor.toString().toLowerCase(Locale.ROOT);
        imagePath = "images/"+imageColor+"_tower.png";
        if(numOfPlayers == 2)
            numOfRows = 3;
        else numOfRows = 2;

        for(int i=0; i<3; i++, horizontalOffset+=towersHorizontalOffset){
            verticalOffset=0;
            for(int j=0; j<numOfRows;j++, verticalOffset += towersVerticalOffset, counter++){
                if(i!= 2 || j!= 2){
                    towerZone.add(new Circle(startingTower.getX() + offset + horizontalOffset, startingTower.getY() + verticalOffset, professorsRadius));
                    Image towerImage = new Image(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(imagePath)));
                    towerZone.get(counter).setFill(new ImagePattern(towerImage));
                    gameBoardSceneController.mainPane.getChildren().add(towerZone.get(counter));
                    towerZone.get(counter).setOpacity(1);
                }
            }
        }
    }

    /**
     * Method that draws on the screen the coins of the player
     */
    public void createCoins(){

        double radius = 40;
        Coordinates starting_coordinates = new Coordinates(547.5, 641.5);
        Circle circle = new Circle(starting_coordinates.getX() + offset, starting_coordinates.getY(), radius);
        Image coin = new Image(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("images/Moneta_base.png")));
        circle.setFill(new ImagePattern(coin));
        circle.setVisible(true);
        Label label = new Label();
        label.setLayoutX(540 + offset);
        label.setLayoutY(675);
        label.setText("x0");
        label.setFont(new Font("Arial", 15));
        label.setStyle("-fx-font-weight: bold;");
        label.setVisible(true);
        gameBoardSceneController.mainPane.getChildren().addAll(circle, label);
        this.coins = circle;
        this.coinsNum = label;

        }

    /**
     * Helper method used to get the vertical offset of the pawns in the Dining Room/Professor Zone based on the Color
     * @param color The color of the pawns that need to be created
     * @return The correct vertical offset
     */
    private double getVerticalOffset(Color color){
        return switch (color) {
            case GREEN -> 0;
            case RED -> diningVerticaloffset;
            case YELLOW -> diningVerticaloffset * 2;
            case PINK -> diningVerticaloffset * 3;
            default -> diningVerticaloffset * 4;
        };
    }

    /**
     * Makes students on the Entrance selectable and, when clicked, makes islands and dining room selectable and removes the selectable property from the Entrance
     */
    public void makeEntranceSelectable(){
        for(FXStudent student : entrance){
            if(student.getOpacity() == 1) {
                student.setOnMouseEntered(mouseEvent -> student.setCursor(Cursor.HAND));
                student.setOnMouseClicked(mouseEvent -> {
                    gameBoardSceneController.getIslandChoice(student);
                    makeDiningRoomSelectable(student);
                    removeSelectableEntrance();
                });
            }
        }
    }

    /**
     * Makes the first empty space on the Dining Room of the given color selectable, when clicked removes the selectable property from the Dining Room and the Islands and sends
     * a message to the server containing the student's destination
     * @param entranceStudent The Students that can be placed in the Dining Room
     */
    public void makeDiningRoomSelectable(FXStudent entranceStudent){
        for(FXStudent student : diningRooms.get(entranceStudent.getColor())){
            if(student.getOpacity() == 0.0) { //if the student is not visible
                student.setOnMouseEntered(mouseEvent -> student.setCursor(Cursor.HAND));
                student.setOnMouseClicked(mouseEvent -> {
                    gameBoardSceneController.removeSelectableIslands();
                    removeSelectableDiningRoom(student.getColor());
                    gameBoardSceneController.getGui().adapter.sendMessage(new RMessageMove(student.getColor(), 0, nickname));
                });
                break;
            }
        }
    }

    /**
     * Makes the Dining Room not selectable
     * @param color The color of the Dining Room to make non-selectable
     */
    public void removeSelectableDiningRoom(Color color){
        for(FXStudent student : diningRooms.get(color)){
            student.setOnMouseEntered(mouseEvent -> student.setCursor(Cursor.DEFAULT));
            student.setOnMouseClicked(null);
        }
    }

    /**
     * Makes the Entrance not selectable
     */
    public void removeSelectableEntrance(){
        for(FXStudent student : entrance){
            student.setOnMouseEntered(mouseEvent -> student.setCursor(Cursor.DEFAULT));
            student.setOnMouseClicked(null);
        }
    }

    public String getNickname(){
        return this.nickname;
    }

    public ArrayList<FXStudent> getEntrance(){
        return this.entrance;
    }

    public ArrayList<Circle> getTowerZone(){
        return this.towerZone;
    }

    public HashMap<Color, ArrayList<FXStudent>> getDiningRoom(){
        return this.diningRooms;
    }

    public HashMap<Color, FXStudent> getProfessorZone(){
        return this.professorZone;
    }

    /**
     * Updates the entrance with the informations passed
     * @param students A Map containing the number of students for each color
     */
    public void refreshEntrance(Map<Color, Integer> students){
        String imageColor;
        String imagePath;
        Image image;
        int counter = 0;
        for(Color color : students.keySet()){
            while(students.get(color) > 0){
                if(entrance.get(counter).getColor() == null || !entrance.get(counter).getColor().equals(color)) {
                    imageColor = color.toString().toLowerCase(Locale.ROOT);
                    imagePath = "images/student_" + imageColor + ".png";
                    image = new Image(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(imagePath)));
                    entrance.get(counter).setFill(new ImagePattern(image));
                    entrance.get(counter).setOpacity(1);
                    entrance.get(counter).setColor(color);
                    counter += 1;
                    students.put(color, students.get(color) - 1);
                }
                else if(entrance.get(counter).getOpacity() == 0){
                    entrance.get(counter).setOpacity(1);
                    counter+=1;
                    students.put(color, students.get(color) - 1);
                }
                else{
                    counter+=1;
                    students.put(color, students.get(color) - 1);
                }
            }
        }
        for(int i= counter; i<entrance.size(); i++)
            entrance.get(i).setOpacity(0);


    }

    /**
     * Updates the dining rooms with the informations passed
     * @param diningRooms A Map containing the number of students for each color
     */
    public void refreshDiningRooms(Map<Color, Integer> diningRooms){

        int counter = 0;

        for(Color color : diningRooms.keySet()){

            while(diningRooms.get(color) > 0){
                if(this.diningRooms.get(color).get(counter).getOpacity() == 0){
                    this.diningRooms.get(color).get(counter).setOpacity(1);

                }
                counter+=1;
                diningRooms.put(color, diningRooms.get(color)-1);

            }
            for(int i=counter; i<10;i++)
                this.diningRooms.get(color).get(i).setOpacity(0);

            counter=0;
        }

    }

    /**
     * Updates the professors zone
     * @param professors A Map containing the possessor of each professor
     */
    public void refreshProfessors(Map<Color, String> professors){
        for(Color color : professors.keySet()){
            if(!professors.get(color).equals(nickname))
                this.professorZone.get(color).setOpacity(0);
            else this.professorZone.get(color).setOpacity(1);
        }
    }

    /**
     * Updates the Tower Zone
     * @param towers The number of towers in the tower zone
     */
    public void refreshTowerZones(Integer towers) {

        int counter = 0;
        for (int i=0;i<towers;i++){
            towerZone.get(i).setOpacity(1);
            counter+=1;
        }
        for(int i=counter; i<towerZone.size();i++)
            towerZone.get(i).setOpacity(0);
    }

    /**
     * Updates the number of coins
     * @param coins The number of coins
     */
    public void refreshCoins(Integer coins){
        coinsNum.setText("x"+coins.toString());
    }



}
