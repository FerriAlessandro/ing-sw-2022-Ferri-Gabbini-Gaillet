package it.polimi.ingsw.view.gui;

/**
 * This class represents Coordinates as a couple of double values
 * @author Alessandro F.
 * @version 1.0
 */
public class Coordinates {

    private double x, y;

    public Coordinates(){

    }
    public Coordinates(double x, double y){
        this.x=x;
        this.y=y;
    }

    public double getX(){
        return this.x;
    }

    public double getY(){
        return this.y;
    }

    public void setX(double x){
        this.x=x;
    }

    public void setY(double y){
        this.y=y;
    }

    /**
     * Method used to Sum two Coordinates
     * @param c1 First Coordinate
     * @param c2 Second Coordinate
     * @return A coordinate representing the sum of the two parameters
     */
    public static Coordinates sum(Coordinates c1, Coordinates c2){
        Coordinates res = new Coordinates();
        res.x = c1.x + c2.x;
        res.y= c1.y + c2.y;

        return res;
    }

    /**
     * Method used to Subtract two Coordinates
     * @param c1 First Coordinate
     * @param c2 Second Coordinate
     * @return A coordinate representing the subtraction of the two parameters
     */
    public Coordinates subtract(Coordinates c1, Coordinates c2){
        Coordinates res = new Coordinates();
        res.x = c1.x - c2.x;
        res.y= c1.y - c2.y;

        return res;
    }



}

