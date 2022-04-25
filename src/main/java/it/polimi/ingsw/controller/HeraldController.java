package it.polimi.ingsw.controller;
import it.polimi.ingsw.exceptions.NumOfIslandsException;
import it.polimi.ingsw.exceptions.TowerWinException;
import it.polimi.ingsw.model.IslandTile;
import it.polimi.ingsw.model.enumerations.Characters;
import it.polimi.ingsw.model.enumerations.Phase;
import it.polimi.ingsw.model.enumerations.TowerColor;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.RMessageGrandmaherbHerald;
import it.polimi.ingsw.network.messages.SMessageGrandmaherbHerald;
import it.polimi.ingsw.network.messages.SMessageWin;

public class HeraldController extends CharacterController{

    public HeraldController(GameController gameController, Characters characterName){
        super(gameController, characterName);
    }


    @Override
    public void use(String nickName){

        if(checkCoin()){
            gameController.getVirtualView(nickName).askCharacterMove(new SMessageGrandmaherbHerald(Characters.HERALD));
        }
        else chooseAnotherCard(nickName);
    }

    @Override
    public void activate (Message message){

        RMessageGrandmaherbHerald heraldMessage = (RMessageGrandmaherbHerald) message;
        TowerColor influenceWinner;
        IslandTile islandToCheck;
        int desiredIsland = heraldMessage.islandIndex - 1;
        int lastIslandIndex = getGameBoard().getIslands().size() - 1;
        if(lastIslandIndex < desiredIsland){
            gameController.sendErrorMessage(heraldMessage.nickName, "Invalid Island! Please select a number between 1 and " + (lastIslandIndex + 1));
            gameController.getVirtualView(heraldMessage.nickName).askCharacterMove(new SMessageGrandmaherbHerald(Characters.HERALD));
            return;
        }
        islandToCheck = getGameBoard().getIslands().get(desiredIsland);
        if(islandToCheck.isForbidden()){
            gameController.sendErrorMessage(heraldMessage.nickName, "You can't calculate influence on a forbidden Island! Please select another Island");
            gameController.getVirtualView(heraldMessage.nickName).askCharacterMove(new SMessageGrandmaherbHerald(Characters.HERALD));
            return;
        }

        try {
            getGame().checkInfluence(islandToCheck);
            getGame().checkForArchipelago(islandToCheck);
        }catch (TowerWinException e){
            for (String nickName : gameController.getNickNames())
                gameController.getVirtualView(nickName).showWinMessage(new SMessageWin(e.getMessage()));
            gameController.gamePhase = Phase.WINNER;
            return;

        }
        catch(NumOfIslandsException e){
            gameController.checkWin();
            gameController.gamePhase = Phase.WINNER;
            return;

        }

        removeCoins();
        gameController.getCharacterByName(Characters.HERALD).use();
        switchPhase();
        gameController.hasPlayedCharacter = true;
        gameController.getCharacterByName(Characters.HERALD).setActive(true);
    }
}

