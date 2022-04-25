package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.enumerations.Characters;

/**
 * This class represents the Character Controller when one of the following Character Card is played : Centaur, Farmer, Knight, MagicMailman
 * @author Alessandro F.
 * @version 1.0
 */

public class InfluenceController extends CharacterController{

    public InfluenceController(GameController gameController, Characters characterName){
        super(gameController, characterName);
    }

    /**
     * This method checks if the player has enough coins to play the selected character; if he does, the card is activated, otherwise he is asked to choose another Character
     * @param nickName The NickName of the player
     */
    @Override
    public void use(String nickName){
        if(checkCoin()){

            sideEffects();
        }
        else chooseAnotherCard(nickName);
    }

}
