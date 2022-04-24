package it.polimi.ingsw.controller;

import it.polimi.ingsw.exceptions.EmptyBagException;
import it.polimi.ingsw.exceptions.FullDestinationException;
import it.polimi.ingsw.model.enumerations.Characters;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.RMessageMonkPrincessRogue;
import it.polimi.ingsw.network.messages.SMessageMonkPrincessRogue;

import java.util.EnumMap;

/**
 * This class represents the Character Controller when the Monk Character Card is played
 * @author Alessandro F.
 * @version 1.0
 */
public class MonkController extends CharacterController{
    /**
     * Constructor
     * @param gameController The Game Controller
     * @param characterName The name of the Character Card played
     */
    public MonkController(GameController gameController, Characters characterName){
        super(gameController, characterName);
    }

    /**
     * This Method sends the player the Students present on the Card for him to choose, if the player doesn't have enough coins he's asked to choose another card
     * @param nickName The NickName of the Player
     */
    @Override
    public void use(String nickName){
        if(checkCoin()) //If the player has enough coin
            gameController.getVirtualView(nickName)
                .askCharacterMove(new SMessageMonkPrincessRogue(new EnumMap<>(gameController.getCharacterByName(Characters.MONK).getState()), Characters.MONK));
        else {//else ask for a new character card
            chooseAnotherCard(nickName);

        }
    }

    /**
     * This method is invoked when the card's effect is activated (after receiving the right parameters).
     * If the player selected an invalid island Index, he's re-asked to choose one, if the index is correct the card's effect is activated, the coin removed from the user
     * and the card's cost incremented.
     * The Phase of the Game is changed at the end of the method's call
     * @param message The message containing the parameters to activate che card
     */
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
