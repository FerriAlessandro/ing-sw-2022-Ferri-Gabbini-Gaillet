package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.model.enumerations.Color;
import javafx.scene.shape.Circle;

/**
 * This class is used to represent a Student/Professor, extends Circle so it can be drawn and has an attribute Color to know which color the pawn currently is
 * @author Alessandro F.
 * @version 1.0
 */

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
