package it.polimi.ingsw.controller;

import it.polimi.ingsw.exceptions.FullDestinationException;
import it.polimi.ingsw.model.DiningRoom;
import it.polimi.ingsw.model.enumerations.Characters;
import it.polimi.ingsw.model.enumerations.Color;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.RMessageJesterBard;
import it.polimi.ingsw.network.messages.SMessageJesterBard;

import java.util.EnumMap;

/**
 * This class represents the Character Controller when the Bard Character Card is played
 * @author Alessandro F.
 * @version 1.0
 */

public class BardController extends CharacterController{

    public BardController(GameController gameController, Characters characterName){
        super(gameController, characterName);
    }

    /**
     * This method shows the player its Dining Room and its Entrance to let him choose the students to swap.
     * If he doesn't have enough coins or if he can't play the card, he's asked to choose another Card.
     * @param nickName The NickName of the Player
     */
    @Override
    public void use(String nickName){

        if(checkCoin()){

            DiningRoom diningRoom = getCurrentPlayerBoard().getDiningRoom();
            boolean playable = false;
            for(Color color : Color.values()){
                if(diningRoom.getNumStudents(color)>0)
                    playable = true;
            }
            if(playable) {  //If at least one swap is possible the card is playable
                gameController.getVirtualView(nickName)
                        .askCharacterMove(new SMessageJesterBard(
                                new EnumMap<>(getCurrentPlayerBoard().getDiningRoom().getState()),
                                new EnumMap<>(getCurrentPlayerBoard().getEntrance().getState()),
                                Characters.BARD));
            }
            else {
                gameController.sendErrorMessage(nickName, "You cannot play this card, you dont have students in your dining rooms");
                gameController.getVirtualView(nickName).showCharacterChoice(gameController.createCharacterMessage());
            }
        }
        else chooseAnotherCard(nickName);
    }

    /**
     * This method is invoked when the card's effect is activated.
     * It checks if the player sent the right parameters (if he has enough students on its dining room to fulfill the request); if the parameters are correct
     * the card is activated, the coins are removed from the Player and the card's cost is increased, otherwise the player is asked to choose other colors
     * @param message The Message containing the colors to swap chosen by the Player
     */
    @Override
    public void activate(Message message){

        RMessageJesterBard bardMessage = (RMessageJesterBard) message;
        DiningRoom diningRoom = getCurrentPlayerBoard().getDiningRoom();


        for(Color student : bardMessage.origin)
            diningRoom.removeStudent(student);

        for(Color student : bardMessage.entrance)
            getCurrentPlayerBoard().getEntrance().removeStudent(student);

        for(Color student : bardMessage.origin)
            try{
                getCurrentPlayerBoard().getEntrance().addStudent(student);
            }catch(FullDestinationException e){
                e.printStackTrace(); //only if a bug happens
            }

        for(Color student : bardMessage.entrance)
            try{
                diningRoom.addStudent(student);
                getGame().checkProfessorsOwnership();
            }catch(FullDestinationException e){
                e.printStackTrace(); //only if a bug happens
            }

        sideEffects();

    }



}

