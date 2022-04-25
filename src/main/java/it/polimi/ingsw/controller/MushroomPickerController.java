package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.enumerations.Characters;
import it.polimi.ingsw.model.enumerations.Color;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.RMessageRogueMushroomPicker;
import it.polimi.ingsw.network.messages.SMessageRogueMushroomPicker;

public class MushroomPickerController extends CharacterController{
    public MushroomPickerController(GameController gameController, Characters characterName){
        super(gameController, characterName);
    }

    @Override
    public void use(String nickName){
        if(checkCoin()){
            gameController.getVirtualView(nickName).askCharacterMove(new SMessageRogueMushroomPicker(Characters.MUSHROOM_PICKER));
        }
        else chooseAnotherCard(nickName);
    }

    @Override
    public void activate(Message message){
        RMessageRogueMushroomPicker mushroomPickerMessage = (RMessageRogueMushroomPicker) message;
        Color chosenColor = mushroomPickerMessage.chosenColor;
        gameController.getCharacterByName(Characters.MUSHROOM_PICKER).setForbiddenColor(chosenColor);
        removeCoins();
        gameController.getCharacterByName(Characters.MUSHROOM_PICKER).use();
        switchPhase();
        gameController.hasPlayedCharacter = true;
        gameController.getCharacterByName(Characters.MUSHROOM_PICKER).setActive(true);
    }
}
