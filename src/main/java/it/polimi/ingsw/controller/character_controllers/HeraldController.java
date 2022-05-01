package it.polimi.ingsw.controller.character_controllers;
import it.polimi.ingsw.controller.GameController;
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
/**
 * This class represents the Character Controller when the Herald Character Card is played
 * @author Alessandro F.
 * @version 1.0
 */

public class HeraldController extends CharacterController{

    public HeraldController(GameController gameController, Characters characterName){
        super(gameController, characterName);
    }

    /**
     * This method checks if the user has enough coin to play the Herald; if he does, a message requesting for and island index is sent
     * @param nickName The NickName of the player
     */
    @Override
    public void use(String nickName){

        if(checkCoin()){
            gameController.getVirtualView(nickName).askCharacterMove(new SMessageGrandmaherbHerald(Characters.HERALD));
        }
        else chooseAnotherCard(nickName);
    }

    /**
     * This method is invoked when the effect is activated. The influence is calculated on the island selected by the player; if the index sent is incorrect, a message asking
     * for a new index is sent to the Player
     * @param message The Message containing the index of the island on which to calculate the influence
     */
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

        sideEffects();
    }
}

