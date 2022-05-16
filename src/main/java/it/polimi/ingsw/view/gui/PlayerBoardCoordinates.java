package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.model.enumerations.Color;

import java.sql.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Class used to get the coordinates of each space that can contain a student/professor/tower on a playerboard
 * @author Alessandro F.
 * @version 1.0
 */

/**
 * Constructor, the attributes playerBoard_NUMBER_Offset hold the coordinates of the top-left corner of each playerboard png from the top-left corner of the Pane that contains them
 */
public class PlayerBoardCoordinates {

    //PlayerBoards offsets from the top left corner of the root Pane in the Scene
    private static final Coordinates playerBoardOneOffset = new Coordinates(10, 93);
    private static final Coordinates playerBoardTwoOffset = new Coordinates(970, 93);
    private static final Coordinates playerBoardThreeOffset = new Coordinates(10, 520);

    //Helper nested class to represent coordinates
    public static class Coordinates{
        public double x, y;
        Coordinates(double x, double y){
            this.x=x;
            this.y=y;
        }
    }

    /**
     * Helper method used to create the Map<Color, ArrayList<Coordinates>> used to represent a playerboard diningRoom
     * @param diningRooms the HashMap to fill
     * @param starting_space The coordinates of the first Green Student spaces on a diningRoom based on the playerBoard (playerBoard 1 will have different coordinates for the first green
     *                       student than playerBoard 2)
     */
    private static void createDiningShort(HashMap<Color, ArrayList<Coordinates>> diningRooms, Coordinates starting_space){

        for(int j=0, vertical_offset=0 ; j<5 ; j++, vertical_offset+=67)
            for(int i=0, horizontal_offset=0; i<10; i++, horizontal_offset+=45){
                if(j==0)
                    diningRooms.get(Color.GREEN).add(new Coordinates(starting_space.x + horizontal_offset, starting_space.y + vertical_offset));
                else if(j==1)
                    diningRooms.get(Color.RED).add(new Coordinates(starting_space.x + horizontal_offset, starting_space.y + vertical_offset));
                else if(j==2)
                    diningRooms.get(Color.YELLOW).add(new Coordinates(starting_space.x + horizontal_offset, starting_space.y + vertical_offset));
                else if(j==3)
                    diningRooms.get(Color.PINK).add(new Coordinates(starting_space.x + horizontal_offset, starting_space.y + vertical_offset));
                else
                    diningRooms.get(Color.BLUE).add(new Coordinates(starting_space.x + horizontal_offset, starting_space.y + vertical_offset));
            }
    }

    /**
     * Helper method used to fill an Arraylist of Coordinates that represents the Student spaces coordinates of an Entrance for a specific playerBoard
     * @param entrance The ArrayList to fill
     * @param starting_space The coordinates of the first Student space on an Entrance (Top left one) based on the playerBoard
     */
    private static void createEntranceShort(ArrayList<Coordinates> entrance, Coordinates starting_space){

        for(int i=0, horizontal_offset = 0; i<2; i++, horizontal_offset+=56)
            for (int j = 0, vertical_offset = 0; j < 4; j++, vertical_offset+=67) //vertical offset between entrance spaces is 67, 4 is the number of spaces in the first column of the entrance
                entrance.add(new Coordinates(starting_space.x + horizontal_offset, starting_space.y + vertical_offset));
    }

    /**
     * Helper method used to fill an ArrayList of Coordinates that represents the Professors spaces coordinates for a specific playerboard
     * @param professors The ArrayList to fill
     * @param starting_space The coordinates of the first Professor space on a specific playerboard (the first one from above, which is the green one)
     */
    public static void createProfessorsShort(ArrayList<Coordinates> professors, Coordinates starting_space){

        for(int j=0, vertical_offset = 0; j<5; j++, vertical_offset+=67)
            professors.add(new Coordinates(starting_space.x, starting_space.y + vertical_offset ));
    }

    /**
     * Helper method used to fill an ArrayList of Coordinates that represents the Towers spaces coordinates for a specific playerboard
     * @param towers The ArrayList to fill
     * @param starting_space The coordinates of the first tower space on a specific playerboard (the top left one)
     */
    public static void createTowerZonesShort(ArrayList<Coordinates> towers, Coordinates starting_space){

        for (int i=0, horizontal_offset=0; i<3; i++, horizontal_offset+=50){
            for(int j=0, vertical_offset=0; j<3; j++, vertical_offset+=67){
                if(i!=2 || j!=2) // towers are 8 in total, 2 columns with 3 towers each and 1 column with 2 towers in it
                    towers.add(new Coordinates(starting_space.x + horizontal_offset, starting_space.y + vertical_offset));
            }
        }
    }

    /**
     * Method used to create an ArrayList of coordinates that represents the Student spaces of an Entrance for the specified PlayerBoard
     * @param playerBoardNumber Number of the PlayerBoard, the Entrance will have different coordinates on different PlayerBoards
     * @return The ArrayList containing the Coordinates of the Entrance for the specified PlayeBoard
     */
    public static ArrayList<Coordinates> createEntrance(int playerBoardNumber){

        Coordinates first_relative_coordinates = new Coordinates(52,138); //coordinates of the first entrance space (first column) relative to the top left corner of a PlayerBoard image
        Coordinates top_right_coordinates = new Coordinates(108, 71); //These are the coordinates of the top right space of the entrance, it's the only one not aligned with the others, so we hardcode it in

        ArrayList<Coordinates> entrance = new ArrayList<>(); //Result

        if(playerBoardNumber == 1){ //The playerboard of the first player, the top left one

            Coordinates starting_space = new Coordinates(playerBoardOneOffset.x + first_relative_coordinates.x, playerBoardOneOffset.y + first_relative_coordinates.y); //top left space, we move with relative coordinates from here
            entrance.add(new Coordinates(playerBoardOneOffset.x + top_right_coordinates.x, playerBoardThreeOffset.y + top_right_coordinates.y)); //We add the top right entrance space which is not aligned with the others

            createEntranceShort(entrance, starting_space);
        }

        else if(playerBoardNumber == 2){ //The playerboard of the second player, the top right one

            Coordinates starting_space = new Coordinates(playerBoardTwoOffset.x + first_relative_coordinates.x, playerBoardTwoOffset.y + first_relative_coordinates.y);
            entrance.add(new Coordinates(playerBoardTwoOffset.x + top_right_coordinates.x, playerBoardTwoOffset.y + top_right_coordinates.y));

            createEntranceShort(entrance, starting_space);
        }

        else if(playerBoardNumber == 3){ //The playerboard of the third player, the bottom left one

            Coordinates starting_space = new Coordinates(playerBoardThreeOffset.x + first_relative_coordinates.x, playerBoardThreeOffset.y + first_relative_coordinates.y);
            entrance.add(new Coordinates(playerBoardThreeOffset.x + top_right_coordinates.x, playerBoardThreeOffset.y + top_right_coordinates.y));

            createEntranceShort(entrance, starting_space);

        }

        return entrance;

    }

    /**
     * Method used to create a Map between a Color and an ArrayList of Coordinates that represents the Dining Room Coordinates for the specified PlayerBoard
     * @param playerBoardNumber Number of the PlayerBoard, the DiningRoom will have different coordinates on different PlayerBoards
     * @return The Map containing the Dining Room Coordinates for the specified PlayerBoard
     */
    public static Map<Color, ArrayList<Coordinates>> createDiningRooms(int playerBoardNumber){

        Coordinates first_relative_coordinates = new Coordinates(196,71); //Coordinates of the first cell of the Green DiningRoom (the first one from above) relative to the top left corner of the playerboard
        HashMap<Color, ArrayList<Coordinates>> diningRooms= new HashMap<>();
        for(Color color : Color.values())
            diningRooms.put(color, new ArrayList<>());

        if(playerBoardNumber == 1){
            Coordinates starting_space = new Coordinates(playerBoardOneOffset.x + first_relative_coordinates.x, playerBoardOneOffset.y + first_relative_coordinates.y);
            createDiningShort(diningRooms, starting_space);
        }

        else if(playerBoardNumber == 2){
            Coordinates starting_space = new Coordinates(playerBoardTwoOffset.x + first_relative_coordinates.x, playerBoardTwoOffset.y + first_relative_coordinates.y);
            createDiningShort(diningRooms, starting_space);

        }

        else if(playerBoardNumber == 3){
            Coordinates starting_space = new Coordinates(playerBoardThreeOffset.x + first_relative_coordinates.x, playerBoardThreeOffset.y + first_relative_coordinates.y);
            createDiningShort(diningRooms, starting_space);
        }

        return diningRooms;

    }

    /**
     * Method used to create an ArrayList of coordinates that represents the coordinates of the professors spaces on a given playerboard
     * @param playerBoardNumber The number  of the playerboard
     * @return The ArrayList containing the Coordinates of the professors spaces for a given playerboard
     */
    public static ArrayList<Coordinates> createProfessors(int playerBoardNumber){

        Coordinates first_relative_coordinates = new Coordinates(686,71); //Position of the Green professor relative to the top left corner of a playerboard image
        ArrayList<Coordinates> professors = new ArrayList<>();

        if(playerBoardNumber == 1){
            Coordinates starting_space = new Coordinates(playerBoardOneOffset.x + first_relative_coordinates.x, playerBoardOneOffset.y + first_relative_coordinates.y);
            createProfessorsShort(professors, starting_space);
        }

        else if(playerBoardNumber == 2){
            Coordinates starting_space = new Coordinates(playerBoardTwoOffset.x + first_relative_coordinates.x, playerBoardTwoOffset.y + first_relative_coordinates.y);
            createProfessorsShort(professors, starting_space);
        }

        else if(playerBoardNumber == 3){
            Coordinates starting_space = new Coordinates(playerBoardThreeOffset.x + first_relative_coordinates.x, playerBoardThreeOffset.y + first_relative_coordinates.y);
            createProfessorsShort(professors, starting_space);
        }

        return professors;
    }


    /**
     * Method used to create an ArrayList of coordinates that represents the coordinates of the towers spaces on a given playerboard
     * @param playerBoardNumber The number  of the playerboard
     * @return The ArrayList containing the Coordinates of the towers spaces for a given playerboard
     */
    public static ArrayList<Coordinates> createTowerZones (int playerBoardNumber){ //We create 8 slots of towerzones (as if the game is always a 2 players game)

        Coordinates first_relative_coordinates = new Coordinates(787,138);
        ArrayList<Coordinates> towerZones = new ArrayList<>();
        if(playerBoardNumber == 1){

            Coordinates starting_space = new Coordinates(playerBoardOneOffset.x + first_relative_coordinates.x, playerBoardOneOffset.y + first_relative_coordinates.y);
            createTowerZonesShort(towerZones, starting_space);
        }

        else if(playerBoardNumber == 2){

            Coordinates starting_space = new Coordinates(playerBoardTwoOffset.x + first_relative_coordinates.x, playerBoardTwoOffset.y + first_relative_coordinates.y);
            createTowerZonesShort(towerZones, starting_space);
        }

        else if(playerBoardNumber == 3){

            Coordinates starting_space = new Coordinates(playerBoardThreeOffset.x + first_relative_coordinates.x, playerBoardThreeOffset.y + first_relative_coordinates.y);
            createTowerZonesShort(towerZones, starting_space);
        }

        return towerZones;
    }



}
