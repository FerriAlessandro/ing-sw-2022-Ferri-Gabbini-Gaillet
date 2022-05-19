package it.polimi.ingsw.controller.character_controllers;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.model.enumerations.Characters;
import it.polimi.ingsw.model.enumerations.Color;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.RMessageRogueMushroomPicker;
import it.polimi.ingsw.network.messages.SMessageRogueMushroomPicker;

/**
 * This class represents the Character Controller when the Mushroom Picker Character Card is played
 * @author Alessandro F.
 * @version 1.0
 */
public class MushroomPickerController extends CharacterController{

    /**
     * Constructor
     * @param gameController The Game Controller
     * @param characterName The Name of the Character Card
     */
    public MushroomPickerController(GameController gameController, Characters characterName){
        super(gameController, characterName);
    }

    /**
     * This method checks if the user has enough coins; if he does, a message requesting for the color needed for the activation of the card is sent
     * @param nickName The NickName of the player
     */
    @Override
    public void use(String nickName){
        if(checkCoin()){
            gameController.getVirtualView(nickName).rogueMushroomPickerScene(new SMessageRogueMushroomPicker(Characters.MUSHROOM_PICKER));
        }
        else chooseAnotherCard(nickName);
    }

    /**
     * This method is invoked when the card's effect is activated, the chosen color is set as Forbidden and will not be counted in the Influence check
     * @param message The message containing the forbidden color
     */
    @Override
    public void activate(Message message){
        RMessageRogueMushroomPicker mushroomPickerMessage = (RMessageRogueMushroomPicker) message;
        Color chosenColor = mushroomPickerMessage.chosenColor;
        gameController.getCharacterByName(Characters.MUSHROOM_PICKER).setForbiddenColor(chosenColor);
        sideEffects();
    }
}
