package it.polimi.ingsw.controller;
import it.polimi.ingsw.model.enumerations.Characters;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.RMessageGrandmaherbHerald;
import it.polimi.ingsw.network.messages.SMessageGrandmaherbHerald;

/**
 * This class represents the Character Controller when the Grandma Herb Character Card is played
 * @author Alessandro F.
 * @version 1.0
 */


public class GrandmaHerbController extends CharacterController{

    public GrandmaHerbController(GameController gameController, Characters characterName){
        super(gameController, characterName);
    }

    /**
     * This method asks the player the index of the Island on which to place the no entry tile.
     * If the player doesn't have enough coins or if the Grandma Herb card doesn't have any no entry tile left, the player is asked to choose another card.
     * @param nickName NickName of the Player
     */
    @Override
    public void use (String nickName){
        if(checkCoin()){
            if(gameController.getCharacterByName(Characters.GRANDMA_HERB).getNoEntryTiles() > 0)
                gameController.getVirtualView(nickName).askCharacterMove(new SMessageGrandmaherbHerald(Characters.GRANDMA_HERB));
            else {
                gameController.sendErrorMessage(nickName, "There are 0 No Entry Tiles available, please select another Character Card");
                gameController.getVirtualView(nickName).showCharacterChoice(gameController.createCharacterMessage());
            }
        }
        else chooseAnotherCard(nickName);
    }

    /**
     * This method is invoked when the Grandma Herb card's effect is activated. If the index chosen by the player is not valid a message is send to the player asking
     * for a new index, otherwise the no entry tile is placed to the chosen Island, the player's coins are removed and the card's cost is incremented
     * @param message The message containing the Island's index chosen by the player
     */
    @Override
    public void activate(Message message){
        RMessageGrandmaherbHerald grandmaHerbMessage = (RMessageGrandmaherbHerald) message;

        int desiredIsland = grandmaHerbMessage.islandIndex - 1;
        int lastIslandIndex = getGameBoard().getIslands().size() - 1;
        if(lastIslandIndex < desiredIsland){
            gameController.sendErrorMessage(grandmaHerbMessage.nickName, "Invalid Island! Please select a number between 1 and " + (lastIslandIndex + 1));
            gameController.getVirtualView(grandmaHerbMessage.nickName).askCharacterMove(new SMessageGrandmaherbHerald(Characters.GRANDMA_HERB));
            return;
        }
        getGameBoard().getIslands().get(desiredIsland).addNoEntry();
        gameController.getCharacterByName(Characters.GRANDMA_HERB).removeNoEntryTile();
        sideEffects();
    }
}
