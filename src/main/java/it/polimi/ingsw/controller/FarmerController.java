package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.enumerations.Characters;

public class FarmerController extends CharacterController{
    public FarmerController(GameController gameController, Characters characterName){
        super(gameController, characterName);
    }

    @Override
    public void use(String nickName){
        if(checkCoin()){
            removeCoins();
            gameController.getCharacterByName(Characters.FARMER).use();
            switchPhase();
            gameController.hasPlayedCharacter = true;
            gameController.getCharacterByName(Characters.FARMER).setActive(true);
        }
        else chooseAnotherCard(nickName);

    }
}
