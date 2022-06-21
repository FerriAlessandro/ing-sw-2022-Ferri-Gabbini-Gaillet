package it.polimi.ingsw.view.gui;
import it.polimi.ingsw.model.enumerations.Color;
import it.polimi.ingsw.network.messages.RMessageCloud;
import it.polimi.ingsw.view.gui.scene.GameBoardSceneController;
import javafx.scene.Cursor;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.ImagePattern;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

/**
 * This class is used to store and modify the students (represented as Circles) present on a cloud tile
 * @author Alessandro F.
 * @version 1.0
 */

public class FXCloud {

    private final double cloudAbsoluteHorizontalOffset; //Horizontal offset between clouds
    private final double cloudAbsoluteVerticalOffset; //Vertical offset between clouds
    private final Coordinates firstStudent = new Coordinates(1460, 115); //first student of the first cloud
    private final GameBoardSceneController gameBoardSceneController;
    private final ArrayList<FXStudent> students= new ArrayList<> (); //We store the Circles representing the students to have a way of retrieving them from the Scene Tree
    private final int cloudIndex;
    private final int numOfPlayers;
    private final ImageView cloud;


    /**
     * Constructor
     * @param index index of the cloud that's being created (1, 2 or 3)
     * @param gameBoardSceneController The Controller that controls the scene on which the clouds appear*
     * @param numOfPlayers The number of players in the game, based on which the third cloud is instantiated or not
     */
    public FXCloud(int index, GameBoardSceneController gameBoardSceneController, int numOfPlayers, ImageView cloud){

        this.cloud = cloud;
        this.numOfPlayers = numOfPlayers;
        this.cloudIndex = index;
        this.gameBoardSceneController = gameBoardSceneController;
        if(index == 1 ) {
            cloudAbsoluteHorizontalOffset = 0;
            cloudAbsoluteVerticalOffset = 0;
        }
        else if(index == 2) {
            cloudAbsoluteHorizontalOffset = 238;
            cloudAbsoluteVerticalOffset = 0;
        }
        else{
            cloudAbsoluteHorizontalOffset =  119;
            cloudAbsoluteVerticalOffset = 205;
        }
    }

    /**
     * Method used to create an FXCloud, it creates circles positioned on a cloud (depending on the cloud's index)
     */
    public void createCloud() {

        double horizontalOffset = 0;
        double verticalOffset;
        int count = 0;

        //Offset between students on the cloud
        double cloudHorizontalOffset = 50;
        for (int i = 0; i < 2; i++, horizontalOffset += cloudHorizontalOffset) {
            verticalOffset = 0;
            //Offset between students on the cloud
            double cloudVerticalOffset = 50;
            for (int j = 0; j < 2; j++, verticalOffset += cloudVerticalOffset, count++) {
                if(i!=1 || j!=1 || numOfPlayers == 3){
                    double radius = 12.5;
                    students.add(new FXStudent(firstStudent.getX() + cloudAbsoluteHorizontalOffset + horizontalOffset, firstStudent.getY() + verticalOffset + cloudAbsoluteVerticalOffset, radius));
                    gameBoardSceneController.mainPane.getChildren().add(students.get(count));
                    students.get(count).setOpacity(0);
                }

            }
        }
    }

    /**
     * Makes a cloud selectable and, when clicked, sends a message to the server containing the index of the clicked cloud and the nickname of the player that clicked it
     */
    public void makeSelectable(){
        cloud.setOnMouseEntered(mouseEvent -> cloud.setCursor(Cursor.HAND));
        cloud.setOnMouseClicked(mouseEvent -> {
                                                gameBoardSceneController.getGui().adapter.sendMessage(new RMessageCloud(cloudIndex, gameBoardSceneController.getGui().getNickName()));
                                                gameBoardSceneController.removeSelectableCloud();});
    }

    /**
     * Makes a cloud not selectable
     */
    public void removeSelectableCloud(){
        cloud.setOnMouseEntered(mouseEvent -> cloud.setCursor(Cursor.DEFAULT));
        cloud.setOnMouseClicked(null);
    }

    /**
     * Updates the students on a cloud
     * @param students The Map containing the number of students on the cloud for each color
     */
    public void refreshClouds(Map<Color, Integer> students){

        String imageColor;
        String imagePath;
        Image image;
        int counter = 0;
        int numEmpty = 0;
        for(Color color : students.keySet()){
            if(students.get(color) == 0)
                numEmpty+=1;
            while(students.get(color) > 0){
                imageColor = color.toString().toLowerCase(Locale.ROOT);
                imagePath = "images/student_" + imageColor + ".png";
                image = new Image(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(imagePath)));
                this.students.get(counter).setFill(new ImagePattern(image));
                this.students.get(counter).setOpacity(1);
                counter+=1;
                students.put(color, students.get(color)-1);
            }
        }
        if(numEmpty == 5){
            for (FXStudent student : this.students) {
                student.setOpacity(0);
            }
        }
    }
}
