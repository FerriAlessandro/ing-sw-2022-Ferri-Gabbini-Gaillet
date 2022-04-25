package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.DiningRoom;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.enumerations.Characters;
import it.polimi.ingsw.network.messages.*;

import java.util.EnumMap;

/**
 * This class represents the Character Controller when the Rogue Character Card is played
 * @author Alessandro F.
 * @version 1.0
 */

public class RogueController extends CharacterController{

    private final int rogueRemovals;


    public RogueController(GameController gameController, Characters characterName){
        super(gameController, characterName);
        this.rogueRemovals = 3;
    }

    /**
     * This Method shows the player the Colors and lets him choose one, if the player doesn't have enough coins he's asked to choose another card
     * @param nickName The NickName of the Player
     */
    @Override
    public void use(String nickName){

        if(checkCoin()){
            gameController.getVirtualView(nickName)
                    .askCharacterMove(new SMessageRogueMushroomPicker(Characters.ROGUE));
        }
        else{
            chooseAnotherCard(nickName);
        }

    }

    /**
     * This method is invoked when the card's effect is activated (after receiving the right parameters).
     * 3 Students of the decided Color are removed from each Player's Dining Room.
     * The Phase of the Game is changed at the end of the method's call
     * @param message
     */
    @Override
    public void activate(Message message){

        RMessageRogueMushroomPicker rogueMessage = (RMessageRogueMushroomPicker) message;
        DiningRoom diningRoom;
        int numOfRemovals = 0;
        for(Player p : getGame().getPlayers()){
            diningRoom = getGameBoard().getPlayerBoard(p).getDiningRoom();
            boolean emptyDining = false;

            for(int i = 0; i<rogueRemovals && !emptyDining; i++){
                try {
                    diningRoom.removeStudent(rogueMessage.chosenColor);
                    numOfRemovals++;
                }catch(RuntimeException exception){
                    emptyDining = true;
                }
            }
        }
        getGameBoard().getBag().addStudents(numOfRemovals, rogueMessage.chosenColor);
        gameController.broadcastMessage("ROGUE Card has been played, 3 "+rogueMessage.chosenColor+" Students were removed from your Dining Room", MessageType.S_INVALID);
        removeCoins();
        gameController.getCharacterByName(Characters.ROGUE).use();
        switchPhase();
        gameController.hasPlayedCharacter = true;
        gameController.getCharacterByName(Characters.ROGUE).setActive(true);
    }



}
