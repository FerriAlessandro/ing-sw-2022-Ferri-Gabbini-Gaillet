package it.polimi.ingsw.view.gui;

/**
 * This class represents Coordinates as a couple of double values
 * @author Alessandro F.
 * @version 1.0
 */
public class Coordinates {

    private double x, y;

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

}

