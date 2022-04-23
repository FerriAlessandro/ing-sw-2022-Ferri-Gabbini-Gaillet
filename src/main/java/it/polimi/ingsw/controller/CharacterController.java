package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.CharacterCard;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.GameBoard;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.enumerations.Characters;
import it.polimi.ingsw.model.enumerations.Phase;
import it.polimi.ingsw.network.messages.Message;

public class CharacterController {

    public GameController gameController;
    public Characters characterName;

    public CharacterController(GameController gameController, Characters characterName){
        this.gameController = gameController;
        this.characterName = characterName;
    }

    public void use(String nickName){

    }

    public void activate(Message message){

    }

    public void activate(){

    }

    public boolean checkCoin(){
        CharacterCard card = gameController.getCharacterByName(characterName);
        return gameController.getGame().getGameBoard().getPlayerBoard(gameController.getGame().getCurrentPlayer()).getCoin() >= card.getCost();
    }

    public Player getCurrentPlayer(){
       return gameController.getGame().getCurrentPlayer();
    }

    public void removeCoins(){
        gameController.getGame().getGameBoard().getPlayerBoard(gameController.getGame().getCurrentPlayer()).removeCoin(gameController.getCharacterByName(characterName).getCost());
    }

    public GameBoard getGameBoard(){
        return gameController.getGame().getGameBoard();
    }
    public Game getGame(){
        return gameController.getGame();
    }

    public void switchPhase(){
        gameController.switchPhase();
    }

    public void chooseAnotherCard(String nickName){
        gameController.sendErrorMessage(nickName, "Not enough coin!");
        gameController.getVirtualView(nickName).showCharacterChoice(gameController.createCharacterMessage());
    }
}
