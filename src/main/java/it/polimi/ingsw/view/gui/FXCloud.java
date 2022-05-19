package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.view.gui.scene.GameBoardSceneController;

import java.util.ArrayList;

public class FXCloud {

    private double cloudHorizontalOffset = 50 ; //Offset between students on the cloud
    private double cloudVerticalOffset = 50;//Offset between students on the cloud
    private double cloudAbsoluteHorizontalOffset; //Horizontal offset between clouds
    private double cloudAbsoluteVerticalOffset; //Vertical offset between clouds
    private Coordinates firstStudent = new Coordinates(1460, 115); //first student of the first cloud
    private GameBoardSceneController gameBoardSceneController;
    private ArrayList<FXStudent> students= new ArrayList<> ();
    private int cloudIndex;
    private int numOfPlayers;
    private double radius = 12.5;



    public FXCloud(int index, GameBoardSceneController gameBoardSceneController, int numOfPlayers){

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

    public void createCloud() {

        double horizontalOffset = 0;
        double verticalOffset = 0;
        int count = 0;

        for (int i = 0; i < 2; i++, horizontalOffset += cloudHorizontalOffset) {
            verticalOffset = 0;
            for (int j = 0; j < 2; j++, verticalOffset += cloudVerticalOffset, count++) {
                if(i!=1 || j!=1 || numOfPlayers == 3){
                    students.add(new FXStudent(firstStudent.getX() + cloudAbsoluteHorizontalOffset + horizontalOffset, firstStudent.getY() + verticalOffset + cloudAbsoluteVerticalOffset, radius));
                    gameBoardSceneController.mainPane.getChildren().add(students.get(count));
                    students.get(count).setVisible(false);
                }

            }
        }
    }

}
