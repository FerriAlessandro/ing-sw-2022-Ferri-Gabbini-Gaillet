package it.polimi.ingsw.controller;

import it.polimi.ingsw.exceptions.FullDestinationException;
import it.polimi.ingsw.model.enumerations.Characters;
import it.polimi.ingsw.model.enumerations.Color;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.RMessageJesterBard;
import it.polimi.ingsw.network.messages.SMessageJesterBard;

import javax.management.remote.JMXServerErrorException;
import java.util.ArrayList;
import java.util.EnumMap;

/**
 * This class represents the Character Controller when the Jester Character Card is played
 * @author Alessandro F.
 * @version 1.0
 */

public class JesterController extends CharacterController{


    public JesterController(GameController gameController, Characters characterName){
        super(gameController, characterName);
    }

    /**
     * This method shows the player the card's students and his Entrance's students, it lets him decide which students to swap.
     * if the player doesn't have enough coins he's asked to choose another card
     * @param nickName The NickName of the Player
     */
    @Override
    public void use(String nickName){
        if(checkCoin()){
            gameController.getVirtualView(nickName)
                    .askCharacterMove(new SMessageJesterBard(
                            new EnumMap<>(gameController.getCharacterByName(Characters.JESTER).getState()),
                            new EnumMap<>(getCurrentPlayerBoard().getEntrance().getState()),
                            Characters.JESTER));

        }
        else chooseAnotherCard(nickName);
    }

    /**
     * This method is invoked when the card's effect is activated (after receiving the right parameters).
     * The method swaps the selected students between the player's Entrance and the Jester card, removes the coin from the player and increments the card's cost
     * @param message The Message containing the colors to swap chosen by the Player
     */
    @Override
    public void activate(Message message){
        RMessageJesterBard jesterMessage = (RMessageJesterBard) message;

        //First we remove the students from the destinations, then we swap them
        for(Color student : jesterMessage.entrance)
            getCurrentPlayerBoard().getEntrance().removeStudent(student);
        for(Color student : jesterMessage.origin)
            gameController.getCharacterByName(Characters.JESTER).removeStudent(student);

        for(Color student : jesterMessage.entrance) {
            try {
                gameController.getCharacterByName(Characters.JESTER).addStudent(student);
            } catch (FullDestinationException e) {
                e.printStackTrace(); // Only if a bug happens
            }
        }

        for(Color student : jesterMessage.origin){
            try{
                getCurrentPlayerBoard().getEntrance().addStudent(student);
            }catch(FullDestinationException e){
                e.printStackTrace(); //Only if a bug happens
            }
        }

        removeCoins();
        gameController.getCharacterByName(Characters.JESTER).use();
        switchPhase();
        gameController.hasPlayedCharacter = true;

    }


}
