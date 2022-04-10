package it.polimi.ingsw.controller;

import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.enumerations.Phase;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.RMessageAssistant;
import it.polimi.ingsw.network.messages.SMessage;
import it.polimi.ingsw.network.messages.SMessageInvalid;
import it.polimi.ingsw.view.VirtualView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * This class represents the Game Controller, which is the one that transforms a user message into an action in the Game
 * @author Alessandro F.
 * @version 1.0
 */

public class GameController {

    private Game game;
    private Phase gamePhase;
    //TODO Add Character cards status
    private Map<String, VirtualView > playersView = new HashMap<>();
    private boolean isExpert;
    private boolean isLastRound;
    private int numOfPlayers;
    private ArrayList<String> nickNames = new ArrayList<>();

    /**
     * Game Controller constructor
     * @param numOfPlayers Num of players in the game
     * @param isExpert Flag to know if it's an expert game
     */
    public GameController(int numOfPlayers, boolean isExpert){

        this.isLastRound = false;
        this.numOfPlayers = numOfPlayers;
        this.isExpert = isExpert;

    }

    /**
     * This method adds players in the game and starts it if the chosen number of players is reached
     * @param nickName Nickname of the player to add
     * @param playerView Virtual View for the player
     * @throws FullGameException Thrown if there's an attempt to add players in an already full game
     */
    public void addPlayer(String nickName, VirtualView playerView) throws FullGameException {

        if (playersView.size() < numOfPlayers) {

            nickNames.add(nickName);  // in order of arrival

            playersView.put(nickName, playerView);

            if (playersView.size() == numOfPlayers) {
                game = new Game(nickNames);
                gamePhase = Phase.CHOOSE_ASSISTANT_CARD;

            } else throw new FullGameException();

        }
    }

    /**
     * This method elaborates messages based on their type
     * @param m Received message
     */
    public void elaborateMessage(Message m){

        switch(m.getType()){

            case R_ASSISTANT:
                elaborateAssistant(m);
                break;

            case R_CHARACTER:
                elaborateCharacter(m);
                break;

            case R_MOVE:
                elaborateMove(m);
                break;

            case R_MOTHERNATURE:
                elaborateMotherNature(m);
                break;

            case R_CLOUD:
                elaborateCloud(m);
                break;

            default:
                throw new UnsupportedOperationException();

        }
    }

    /**
     * Utility method to send the same message to each player's virtual view
     * @param m The message that needs to be broadcasted
     */
    private void broadcastMessage(Message m){
        for(VirtualView v : playersView.values()){
            v.showGenericMessage(m);
        }
    }


    /**
     * Maps a message to an action: plays the Assistant card that the player chose.
     * @param m The message containing the chosen Assistant card
     */
    private void elaborateAssistant(Message m){  //The message has already been filtered by the InputController (sender is the current player and
                                                 //the Game Phase is correct

        RMessageAssistant message = (RMessageAssistant) m;
        try {
            game.playAssistantCard(message.getPlayedAssistant());

        }catch(NoCurrentPlayerException e){  //should only be thrown if a bug happens...
            e.printStackTrace();
            game.getPlayers().get(0).setPlayerTurn(true);  //if it happens assign the turn to a random player
        }
        catch(CardNotAvailableException | CardNotFoundException e){
            playersView.get(message.getNickName()).showGenericMessage(new SMessageInvalid(e.getMessage()));
        }
        catch(EmptyDeckException e){
            if(!isLastRound){
                broadcastMessage(new SMessageInvalid(e.getMessage()));
                isLastRound = true;
            }
        }
        catch(EndRoundException e){

            game.sortPlayersActionTurn();

            if(isExpert)
                gamePhase = Phase.CHOOSE_CHARACTER_CARD;

            else gamePhase = Phase.MOVE_STUDENTS;
        }
    }








}
