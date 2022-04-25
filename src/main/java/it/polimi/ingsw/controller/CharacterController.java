package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.enumerations.Characters;
import it.polimi.ingsw.model.enumerations.Phase;
import it.polimi.ingsw.network.messages.Message;

/**
 * This class represents the Character Controller, it manages Characters effects in Expert Mode
 * @author Alessandro F.
 * @version 1.0
 */
public class CharacterController {

    public GameController gameController;
    public Characters characterName;

    /**
     * Constructor
     * @param gameController The Game Controller
     * @param characterName The Name of the Character Card
     */
    public CharacterController(GameController gameController, Characters characterName){
        this.gameController = gameController;
        this.characterName = characterName;
    }

    public void use(String nickName){
        switchPhase();
    }

    public void activate(Message message){

    }


    /**
     * Utility method to check if the player using the card has the necessary coins
     * @return True if the player has enough coins, false otherwise
     */
    public boolean checkCoin(){
        CharacterCard card = gameController.getCharacterByName(characterName);
        return gameController.getGame().getGameBoard().getPlayerBoard(gameController.getGame().getCurrentPlayer()).getCoin() >= card.getCost();
    }

    /**
     * Utility method to get the current player of the round
     * @return The current Player
     */
    public Player getCurrentPlayer(){
       return gameController.getGame().getCurrentPlayer();
    }

    /**
     * Removes the coins from a player when he plays a Character Card, the coins are removed based on the card's cost
     */
    public void removeCoins(){
        gameController.getGame().getGameBoard().getPlayerBoard(gameController.getGame().getCurrentPlayer()).removeCoin(gameController.getCharacterByName(characterName).getCost());
    }

    public GameBoard getGameBoard(){
        return gameController.getGame().getGameBoard();
    }
    public Game getGame(){
        return gameController.getGame();
    }

    /**
     * Utility method to switch the Phase of the game after a Card is played
     */
    public void switchPhase(){
        gameController.switchPhase();
    }

    /**
     * Utility method to ask a player to choose another Character Card if the doesn't have enough coins to play the selected card
     * @param nickName The NickName of the Player
     */
    public void chooseAnotherCard(String nickName){
        gameController.sendErrorMessage(nickName, "Not enough coin!");
        gameController.getVirtualView(nickName).showCharacterChoice(gameController.createCharacterMessage());
    }

    /**
     * Utility method to return the PlayerBoard of the current Player
     * @return The PlayerBoard of the current Player
     */
    public PlayerBoard getCurrentPlayerBoard(){
        return getGameBoard().getPlayerBoard(getCurrentPlayer());
    }
}
