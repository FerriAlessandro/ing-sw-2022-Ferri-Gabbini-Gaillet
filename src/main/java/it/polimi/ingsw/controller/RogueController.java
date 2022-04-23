package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.DiningRoom;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.enumerations.Characters;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.MessageType;
import it.polimi.ingsw.network.messages.RMessageMonkPrincessRogue;
import it.polimi.ingsw.network.messages.SMessageMonkPrincessRogue;

import java.util.EnumMap;

public class RogueController extends CharacterController{

    private final int rogueRemovals = 3;

    public RogueController(GameController gameController, Characters characterName){
        super(gameController, characterName);
    }

    @Override
    public void use(String nickName){

        if(checkCoin()){
            gameController.getVirtualView(nickName)
                    .askCharacterMove(new SMessageMonkPrincessRogue(new EnumMap<>(gameController.getCharacterByName(Characters.ROGUE).getState()), Characters.ROGUE));
        }
        else{
            chooseAnotherCard(nickName);
        }

    }


    @Override
    public void activate(Message message){

        RMessageMonkPrincessRogue rogueMessage = (RMessageMonkPrincessRogue) message;
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
    }



}
