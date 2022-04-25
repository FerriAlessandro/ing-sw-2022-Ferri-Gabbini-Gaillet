package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.enumerations.Characters;

public class CentaurController extends CharacterController{
    public CentaurController(GameController gameController, Characters characterName){
        super(gameController, characterName);
    }

    @Override
    public void use(String nickName){
        if(checkCoin()){
            removeCoins();
            gameController.getCharacterByName(Characters.CENTAUR).use();
            switchPhase();
            gameController.hasPlayedCharacter = true;
            gameController.getCharacterByName(Characters.CENTAUR).setActive(true);
        }
        else chooseAnotherCard(nickName);
    }
}
