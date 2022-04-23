package it.polimi.ingsw.controller;

import it.polimi.ingsw.exceptions.EmptyBagException;
import it.polimi.ingsw.exceptions.FullDestinationException;
import it.polimi.ingsw.model.enumerations.Characters;
import it.polimi.ingsw.model.enumerations.Phase;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.RMessageMonkPrincessRogue;
import it.polimi.ingsw.network.messages.SMessageMonkPrincessRogue;

import java.util.EnumMap;

public class MonkController extends CharacterController{

    public MonkController(GameController gameController, Characters characterName){
        super(gameController, characterName);
    }

    @Override
    public void use(String nickName){
        if(checkCoin()) //If the player has enough coin
            gameController.getVirtualView(nickName)
                .askCharacterMove(new SMessageMonkPrincessRogue(new EnumMap<>(gameController.getCharacterByName(Characters.MONK).getState()), Characters.MONK));
        else {//else ask for a new character card
            chooseAnotherCard(nickName);

        }
    }

    @Override
    public void activate(Message message){
        RMessageMonkPrincessRogue monkMessage = (RMessageMonkPrincessRogue) message;

        int desiredIslandIndex = monkMessage.islandIndex-1;
        int lastIslandIndex = getGameBoard().getIslands().size()-1;
        if(lastIslandIndex < desiredIslandIndex){
            gameController.sendErrorMessage(monkMessage.nickName, "Invalid Island! Please select a number between 1 and " + (lastIslandIndex + 1));
            gameController.getVirtualView(monkMessage.nickName).askCharacterMove(new SMessageMonkPrincessRogue(new EnumMap<>(gameController.getCharacterByName(Characters.MONK).getState()), Characters.MONK)));
            return;
        }
        try {
            removeCoins();
            gameController.getCharacterByName(Characters.MONK).use();
            getGameBoard().move(monkMessage.chosenColor, gameController.getCharacterByName(Characters.MONK), getGameBoard().getIslands().get(desiredIslandIndex));
            getGameBoard().fillCharacter(gameController.getCharacterByName(Characters.MONK));
        }catch(FullDestinationException | EmptyBagException ignored){} //Islands can't be full and empty bag is handled when the clouds are chosen

        switchPhase();
    }
}
