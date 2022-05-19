package it.polimi.ingsw.controller.character_controllers;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.exceptions.EmptyBagException;
import it.polimi.ingsw.exceptions.FullDestinationException;
import it.polimi.ingsw.model.DiningRoom;
import it.polimi.ingsw.model.enumerations.Characters;
import it.polimi.ingsw.model.enumerations.Color;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.RMessageMonkPrincess;
import it.polimi.ingsw.network.messages.SMessageMonkPrincess;

import java.util.EnumMap;
/**
 * This class represents the Character Controller when the Spoiled Princess Character Card is played
 * @author Alessandro F.
 * @version 1.0
 */
public class SpoiledPrincessController extends CharacterController{

    /**
     * Constructor
     * @param gameController The Game Controller
     * @param characterName The Name of the Character Card
     */
    public SpoiledPrincessController(GameController gameController, Characters characterName){
        super(gameController, characterName);
    }

    /**
     * This Method sends the player the Students present on the Card for him to choose, if the player doesn't have enough coins he's asked to choose another card.
     * If the player cannot play this card (if for each Color present on the card the corresponding player's Dining Room are full) he's asked to choose another card.
     * @param nickName The NickName of the Player
     */
    @Override
    public void use(String nickName){
        if(checkCoin()) {
            //First we check if the player can play the card (if the card has at least 1 color that the player can choose)
            DiningRoom dining = getCurrentPlayerBoard().getDiningRoom();
            boolean playable = false;
            for (Color color : gameController.getCharacterByName(Characters.SPOILED_PRINCESS).getState().keySet()) {
                if (dining.getNumStudents(color) < dining.getMaxStudents() && gameController.getCharacterByName(Characters.SPOILED_PRINCESS).getState().get(color) > 0)
                    playable = true;
            }
            if (playable) //if the player can play the card ask for the color
                gameController.getVirtualView(nickName)
                        .monkPrincessScene(new SMessageMonkPrincess(new EnumMap<>(gameController.getCharacterByName(Characters.SPOILED_PRINCESS).getState()), Characters.SPOILED_PRINCESS));
            else { //if the player cannot play the card, ask if he wants to play another card
                gameController.sendErrorMessage(nickName, "You cannot play this card, you already filled the Dining Rooms corresponding to this card's colors");
                gameController.getVirtualView(nickName).showCharacterChoice(gameController.createCharacterMessage());
            }
        }
        else{
            chooseAnotherCard(nickName);
        }
    }

    /**
     * This method is invoked when the card's effect is activated (after receiving the right parameters).
     * If the player chose a color and its corresponding Dining Room is already full, he's asked to choose another color.
     * If the chosen color is valid, the card is correctly played, the coins are removed from the Player and the card's cost is incremented.
     * The Phase of the Game is changed at the end of the method's call
     * @param message The Message containing the parameters to activate the card
     */
    @Override
    public void activate(Message message){

        RMessageMonkPrincess princessMessage = (RMessageMonkPrincess) message;
        try{
            getGameBoard().move(princessMessage.chosenColor, gameController.getCharacterByName(Characters.SPOILED_PRINCESS), getGameBoard().getPlayerBoard(getGame().getCurrentPlayer()).getDiningRoom());
            getGame().checkProfessorsOwnership();
            getGameBoard().fillCharacter(gameController.getCharacterByName(Characters.SPOILED_PRINCESS));

        } catch(FullDestinationException e){
            gameController.sendErrorMessage(princessMessage.nickname, "Your " + princessMessage.chosenColor + "Dining Room is full, please selected another color");
            gameController.getVirtualView(princessMessage.nickname).monkPrincessScene(new SMessageMonkPrincess(new EnumMap<>(gameController.getCharacterByName(Characters.SPOILED_PRINCESS).getState()), Characters.SPOILED_PRINCESS));
            return;
        }
        catch(EmptyBagException ignored){} //already handled in the ChooseCloud method

        sideEffects();

    }


}
