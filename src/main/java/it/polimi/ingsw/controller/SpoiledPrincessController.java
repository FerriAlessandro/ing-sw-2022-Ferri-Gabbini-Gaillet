package it.polimi.ingsw.controller;

import it.polimi.ingsw.exceptions.EmptyBagException;
import it.polimi.ingsw.exceptions.FullDestinationException;
import it.polimi.ingsw.model.DiningRoom;
import it.polimi.ingsw.model.enumerations.Characters;
import it.polimi.ingsw.model.enumerations.Color;
import it.polimi.ingsw.model.enumerations.Phase;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.RMessageMonkPrincessRogue;
import it.polimi.ingsw.network.messages.SMessageMonkPrincessRogue;

import java.util.EnumMap;

public class SpoiledPrincessController extends CharacterController{

    public SpoiledPrincessController(GameController gameController, Characters characterName){
        super(gameController, characterName);
    }

    @Override
    public void use(String nickName){
        if(checkCoin()) {
            //First we check if the player can play the card (if the card has at least 1 color that the player can choose)
            DiningRoom dining = gameController.getDiningRoom();
            boolean playable = false;
            for (Color color : gameController.getCharacterByName(Characters.SPOILED_PRINCESS).getState().keySet()) {
                if (dining.getNumStudents(color) < dining.getMaxStudents() && gameController.getCharacterByName(Characters.SPOILED_PRINCESS).getState().get(color) > 0)
                    playable = true;
            }
            if (playable) //if the player can play the card ask for the color
                gameController.getVirtualView(nickName)
                        .askCharacterMove(new SMessageMonkPrincessRogue(new EnumMap<>(gameController.getCharacterByName(Characters.SPOILED_PRINCESS).getState()), Characters.SPOILED_PRINCESS));
            else { //if the player cannot play the card, ask if he wants to play another card
                gameController.sendErrorMessage(nickName, "You cannot play this card, you already filled the Dining Rooms corresponding to this card's colors");
                gameController.getVirtualView(nickName).showCharacterChoice(gameController.createCharacterMessage());
            }
        }
        else{
            chooseAnotherCard(nickName);
        }
    }

    @Override
    public void activate(Message message){

        RMessageMonkPrincessRogue princessMessage = (RMessageMonkPrincessRogue) message;
        try{
            getGameBoard().move(princessMessage.chosenColor, gameController.getCharacterByName(Characters.SPOILED_PRINCESS), getGameBoard().getPlayerBoard(getGame().getCurrentPlayer()).getDiningRoom());
            removeCoins();
            gameController.getCharacterByName(Characters.SPOILED_PRINCESS).use();
            getGameBoard().fillCharacter(gameController.getCharacterByName(princessMessage.characterName));


        } catch(FullDestinationException e){
            gameController.sendErrorMessage(princessMessage.nickName, "Your " + princessMessage.chosenColor + "Dining Room is full, please selected another color");
            gameController.getVirtualView(princessMessage.nickName).askCharacterMove(new SMessageMonkPrincessRogue(new EnumMap<>(gameController.getCharacterByName(Characters.SPOILED_PRINCESS).getState()), Characters.SPOILED_PRINCESS));
            return;
        }
        catch(EmptyBagException ignored){} //already handled in the ChooseCloud method

        switchPhase();

    }


}
