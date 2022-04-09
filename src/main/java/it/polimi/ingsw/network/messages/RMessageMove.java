package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.model.enumerations.Color;

/**
 * Message received by the server with the color of the Student to move and the corresponding destination
 */

public class RMessageMove extends RMessage{

    Color chosenColor;
    int destination;      // 0 for Dining Room, 1 to 12 for the islands

    public RMessageMove(Color color, int destination, String nickName){

        this.type = MessageType.R_MOVE;
        this.chosenColor = color;
        this.destination = destination;
        this.nickName = nickName;
    }

    public Color getChosenColor() {
        return chosenColor;
    }

    public int getDestination() {
        return destination;
    }

}
