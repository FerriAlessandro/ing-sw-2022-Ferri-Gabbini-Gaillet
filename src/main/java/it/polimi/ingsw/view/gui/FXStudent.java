package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.model.enumerations.Color;
import javafx.scene.shape.Circle;


public class FXStudent extends Circle {

    private Color color;

    public FXStudent(double x, double y, double radius){
        super(x, y, radius);
    }

    public Color getColor(){
        return this.color;
    }

    public void setColor(Color color){
        this.color = color;
    }

}
