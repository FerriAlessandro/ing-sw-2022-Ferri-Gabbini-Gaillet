package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.enumerations.Characters;

public class KnightController extends CharacterController{
    public KnightController(GameController gameController, Characters characterName){
        super(gameController, characterName);
    }

    @Override
    public void use(String nickName){
        if(checkCoin()){
            removeCoins();
            gameController.getCharacterByName(Characters.KNIGHT).use();
            switchPhase();
            gameController.hasPlayedCharacter = true;
            gameController.getCharacterByName(Characters.KNIGHT).setActive(true);
        }
        else chooseAnotherCard(nickName);
    }
}
