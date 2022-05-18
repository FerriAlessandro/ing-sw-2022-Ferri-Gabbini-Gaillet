package it.polimi.ingsw.view.gui;

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

    public static Coordinates sum(Coordinates c1, Coordinates c2){
        Coordinates res = new Coordinates();
        res.x = c1.x + c2.x;
        res.y= c1.y + c2.y;

        return res;
    }

    public Coordinates subtract(Coordinates c1, Coordinates c2){
        Coordinates res = new Coordinates();
        res.x = c1.x - c2.x;
        res.y= c1.y - c2.y;

        return res;
    }



}

