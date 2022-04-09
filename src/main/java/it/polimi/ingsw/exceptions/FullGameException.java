package it.polimi.ingsw.exceptions;

public class FullGameException extends Exception{

    public FullGameException(){
        super("This Match is already full!");
    }
}
