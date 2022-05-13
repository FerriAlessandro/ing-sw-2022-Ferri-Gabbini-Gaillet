package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.model.enumerations.Color;

/**
 * Message received by the server with the color of the Student to move and the corresponding destination
 * @author Alessandro F.
 * @version 1.0
 */

public class RMessageMove extends RMessage{

    public Color chosenColor;
    public int destination;      // 0 for Dining Room, 1 to 12 for the islands

    /**
     * Constructor
     * @param color of student
     * @param destination 1-12 islands, 0 dining room
     * @param nickName of the player
     */
    public RMessageMove(Color color, int destination, String nickName){

        this.type = MessageType.R_MOVE;
        this.chosenColor = color;
        this.destination = destination;
        this.nickName = nickName;
    }

}
