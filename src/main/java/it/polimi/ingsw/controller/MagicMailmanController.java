package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.enumerations.Characters;

public class MagicMailmanController extends CharacterController{
    public MagicMailmanController(GameController gameController, Characters characterName){
        super(gameController, characterName);
    }

    @Override
    public void use(String nickName){
        if(checkCoin()){
            removeCoins();
            gameController.getCharacterByName(Characters.MAGIC_MAILMAN).use();
            switchPhase();
            gameController.hasPlayedCharacter = true;
            gameController.getCharacterByName(Characters.MAGIC_MAILMAN).setActive(true);
        }
        else chooseAnotherCard(nickName);
    }
}
