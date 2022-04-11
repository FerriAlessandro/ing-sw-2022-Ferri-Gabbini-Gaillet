package it.polimi.ingsw.controller;

import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.enumerations.Phase;
import it.polimi.ingsw.network.messages.*;
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
    private final Map<String, VirtualView > playersView = new HashMap<>();
    private final boolean isExpert;
    private boolean isLastRound;
    private final int numOfPlayers;
    private int numOfMoves = 0;
    private final ArrayList<String> nickNames = new ArrayList<>();

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

            }

        }
        else throw new FullGameException();
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
     * @param message The message that needs to be broadcasted
     */
    private void broadcastMessage(Message message){
        for(VirtualView v : playersView.values()){
            v.showGenericMessage(message);
        }
    }


    /**
     * Maps a message to an action: plays the Assistant card that the player chose.
     * @param message The message containing the chosen Assistant card
     */
    private void elaborateAssistant(Message message){  //The message has already been filtered by the InputController (sender is the current player and
                                                       //the Game Phase is correct)

        RMessageAssistant assistantMessage = (RMessageAssistant) message;
        try {
            game.playAssistantCard(assistantMessage.getPlayedAssistant());
            //TODO CALL ON VIRTUAL VIEW TO ASK THE NEXT PLAYER TO CHOOSE A CARD?

        }catch(NoCurrentPlayerException e){  //should only be thrown if a bug happens...
            e.printStackTrace();

            game.getPlayers().get(0).setPlayerTurn(true);  //if it happens assign the turn to a random player

            broadcastMessage((new SMessageInvalid(e.getMessage() + game.getPlayers().get(0).getNickName() + "is the first player now")));

        }
        catch(CardNotAvailableException | CardNotFoundException e){
            playersView.get(assistantMessage.getNickName()).showGenericMessage(new SMessageInvalid(e.getMessage()));
        }
        catch(EmptyDeckException e){
            if(!isLastRound){
                broadcastMessage(new SMessageInvalid(e.getMessage()));
                isLastRound = true;
            }
        }
        catch(EndRoundException e){
            game.sortPlayersActionTurn();

            if(isExpert) {
                gamePhase = Phase.CHOOSE_CHARACTER_CARD_1;
                //TODO NOTIFICA LA VIEW PER I TURNI DEI GIOCATORI
                // TODO playersView.get(game.getPlayers().get(0)).CHIEDI_CARTA_PERSONAGGIO
            }

            else {
                //TODO NOTIFICA LA VIEW PER I TURNI DEI GIOCATORI

                gamePhase = Phase.MOVE_STUDENTS;
                //TODO playersView.get(game.getPlayers().get(0)).CHIEDI_MOVE
            }
        }
    }

    /**
     * Utility method to get the Entrance of the current player
     * @return The Entrance of the current player
     * @throws NoCurrentPlayerException Thrown if it's nobody's turn
     */
    private Entrance getEntrance() throws NoCurrentPlayerException {

        return game.getGameBoard().getPlayerBoard(game.getCurrentPlayer()).getEntrance();

    }

    /**
     * Utility method to get the Dining Room of the current player
     * @return The Dining Room of the current player
     * @throws NoCurrentPlayerException Thrown if it's nobody's turn
     */
    private DiningRoom getDiningRoom() throws NoCurrentPlayerException {

        return game.getGameBoard().getPlayerBoard(game.getCurrentPlayer()).getDiningRoom();

    }

    /**
     * Maps a message to an action: moves a piece from the Entrance of a player to an Island or the player's Dining Room
     * @param message The message containing the color of the piece that is being moved and the chosen destination (Island or DiningRoom)
     */

    private void elaborateMove(Message message){

        RMessageMove moveMessage = (RMessageMove) message;
        if(numOfMoves == numOfPlayers + 1){ //Already moved 3 (or 4) pieces

            numOfMoves = 0;
            if(isExpert){
                gamePhase = Phase.CHOOSE_CHARACTER_CARD_2;
                // TODO playersView.get(moveMessage.getNickName()).CHIEDI_CARTA_PERSONAGGIO
            }
            else{
                gamePhase = Phase.MOVE_MOTHERNATURE;
                //TODO CHIEDI A PLAYER MADRENATURA
            }
        }
        else { //Can move a piece

            TileWithStudents dest;
            if (moveMessage.getDestination() == 0) { //Destination is the dining room
                try {
                    dest = getDiningRoom();
                } catch (NoCurrentPlayerException e) {

                    e.printStackTrace();
                    game.getPlayers().get(0).setPlayerTurn(true);  //if it happens assign the turn to a random player

                    broadcastMessage((new SMessageInvalid(e.getMessage() + game.getPlayers().get(0).getNickName() + "is the first player now")));
                    return;

                }
            }
            else if (moveMessage.getDestination() > 0 && moveMessage.getDestination() < game.getGameBoard().getIslands().size() + 1)//Destination is an island
                dest = game.getGameBoard().getIslands().get(moveMessage.getDestination() - 1); //User counts islands from 1, not from 0

            else { //Destination not valid
                playersView.get(moveMessage.getNickName()).showGenericMessage(new SMessageInvalid("This destination does not exist!"));
                return;
            }

            try {
                game.move(moveMessage.getChosenColor(), getEntrance(), dest);
            } catch (NoCurrentPlayerException e) { //Bug if it happens

                e.printStackTrace();
                game.getPlayers().get(0).setPlayerTurn(true);  //if it happens assign the turn to a random player

                broadcastMessage((new SMessageInvalid(e.getMessage() + game.getPlayers().get(0).getNickName() + "is the first player now")));

                return;

            } catch (FullDestinationException e) { //Destination already full, need to pick another one (don't increment numOfMoves)

                playersView.get(moveMessage.getNickName()).showGenericMessage(new SMessageInvalid(e.getMessage() + "Please pick another one"));
                return;

            }

            numOfMoves += 1;
        }

    }








}
