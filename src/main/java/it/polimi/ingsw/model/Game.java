package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.enumerations.*;
import it.polimi.ingsw.observers.Observable;

import java.util.*;
import java.util.stream.Collectors;


/**
 * This class represents the Game entity.
 * @author Alessandro F.
 * @version 1.0
 */

public class Game extends Observable {

    private ArrayList<Player> players; //sorted based on the assistant cards played (lowest to highest)
    private Phase gamePhase;
    private final GameBoard gameBoard;
    private final int numOfPlayers;


    /**
     * Game constructor.
     */
    public Game(ArrayList<String> playersNames) {

        this.players = new ArrayList<>();
        this.numOfPlayers = playersNames.size();
        this.gamePhase = Phase.CHOOSE_ASSISTANT_CARD;
        this.players.add(new Player(1,new AssistantDeck(Wizard.WIZARD_1), playersNames.get(0), true, true, TowerColor.WHITE));
        this.players.add(new Player(2, new AssistantDeck(Wizard.WIZARD_2), playersNames.get(1), false, false, TowerColor.BLACK));
        if(playersNames.size()==3)
            this.players.add(new Player(3, new AssistantDeck(Wizard.WIZARD_3), playersNames.get(2), false, false, TowerColor.GRAY));

        this.gameBoard = new GameBoard(players);

    }

    /**
     * @return The List of Players
     */
    public List<Player> getPlayers() {
        return players;
    }

    /**
     * @return The GameBoard
     */
    public GameBoard getGameBoard() {
        return gameBoard;
    }

    /**
     * @return The number of Players in the game
     */
    public int getNumOfPlayers() {
        return numOfPlayers;
    }

    /**
     * @return The current Phase of the game
     */
    public Phase getGamePhase() {
        return gamePhase;
    }

    /**
     * This method sets the Game Phase.
     * @param gamePhase The new Game Phase
     */
    public void setGamePhase(Phase gamePhase) {
        this.gamePhase = gamePhase;
    }

    /**
     * This method is called when a player moves Mother Nature.
     * @param num Num of steps that Mother Nature has to perform
     * @throws TowerWinException Thrown if a player placed all of his towers
     * @throws NumOfIslandsException Thrown if there are only 3 islands left
     */

    public void moveMotherNature(int num) throws TowerWinException, NumOfIslandsException {
        IslandTile oldIsland = gameBoard.getMotherNature().getCurrentIsland();

        IslandTile islandToCheck;
        TowerColor influenceWinner;
        islandToCheck = gameBoard.moveMotherNature(num);
        influenceWinner = gameBoard.checkInfluence(islandToCheck);
        gameBoard.swapTowers(islandToCheck, influenceWinner);
        gameBoard.checkForArchipelago(islandToCheck);

        if (oldIsland != gameBoard.getMotherNature().getCurrentIsland()){
            notifyObservers();
        }
    }

    /**
     * @return The player that can initiate actions in the game
     * @throws NoCurrentPlayerException Thrown if there isn't a current player
     */
    public Player getCurrentPlayer() throws NoCurrentPlayerException{

        for(Player p : players) {
            if (p.isPlayerTurn())
                return p;
        }
        throw new NoCurrentPlayerException(); //if it's nobody's turn (Should only be possible if a bug happens)
    }


    /**
     * This method ends a player's turn and checks if another player has to play after him or if a new round has to start.
     * @param currentPlayer The player that's ending its turn
     * @throws EndRoundException Thrown if the round is over
     */
    private void endPlayerTurn(Player currentPlayer) throws EndRoundException {

        if(players.indexOf(currentPlayer) == players.size()-1)
            throw new EndRoundException();
        else {
            currentPlayer.setPlayerTurn(false);
            players.get(players.indexOf(currentPlayer)+1).setPlayerTurn(true); // the players are sorted based on their played assistant cards
        }

    }



    /**
     * This method plays the assistant card selected by the player.
     * @param playedAssistant The assistant card chosen by the player
     * @throws NoCurrentPlayerException Thrown if there isn't a current player
     * @throws CardNotAvailableException Thrown if the selected card was already played in this turn by another player
     * @throws CardNotFoundException Thrown if the current player doesn't have the selected card
     * @throws EmptyDeckException Thrown if a player runs out of cards
     */
    public void playAssistantCard(AssistantCard playedAssistant) throws NoCurrentPlayerException,
            CardNotAvailableException, CardNotFoundException, EndRoundException, EmptyDeckException {

        try {
            getCurrentPlayer().playAssistantCard(playedAssistant);
            endPlayerTurn(getCurrentPlayer());

        } catch(EmptyDeckException e){

            endPlayerTurn(getCurrentPlayer());
            throw e;
        }

    }

    /**
     * This method moves Pawns on the board based on color, origin and destination.
     * @param color The color of the pawn to move
     * @param origin The starting point of the pawn
     * @param destination The destination of the pawn
     * @throws FullDestinationException Thrown if the selected destination is already full
     */
    public void move (Color color, TileWithStudents origin, TileWithStudents destination) throws FullDestinationException {
        gameBoard.move(color, origin, destination);

        notifyObservers();
    }

    /**
     * This method adds the students on the selected cloud tile to the player's entrance.
     * @param cloud The chosen cloud
     * @throws NoCurrentPlayerException Thrown if there isn't a current player
     * @throws CloudNotFullException Thrown if the selected cloud doesn't have students
     * @throws FullDestinationException Thrown if the entrance of the player is already full
     * @throws EndRoundException Thrown if the Round is over
     */
    public void chooseCloud(CloudTile cloud) throws NoCurrentPlayerException, CloudNotFullException,
                                                    FullDestinationException, EndRoundException {

        gameBoard.chooseCloud(cloud, getCurrentPlayer());
        endPlayerTurn(getCurrentPlayer());

    }

    /**
     * This method sorts the Players based on the value of their played Assistant Card (lowest to highest)
     * and sets the new First Player of the Round
     */
    // The method should keep the players with the same card value in the arrival order
    // (if p1 plays cheetah and p2 plays cheetah,
    // p1 should play before p2, the test works but keep an eye on that).
    public void sortPlayersActionTurn() {

        players.get(numOfPlayers-1).setPlayerTurn(false);
        players = players.stream()
                .sorted(Comparator.comparingInt((p) -> p.getPlayedCard().getCardValue()))
                .collect(Collectors.toCollection(ArrayList :: new));

        Objects.requireNonNull(getFirstPlayer()).setFirst(false);
        players.get(0).setFirst(true);
        players.get(0).setPlayerTurn(true);

    }

    /**
     * @return The first player of the Round
     */
    private Player getFirstPlayer(){
        for(Player p : players){
            if(p.isFirst())
                return p;
        }
        return null;
    }

    /**
     * Sorts the players in a clockwise order starting from the player who plays first
     */
    public void sortPlayersAssistantTurn() {

        players.get(numOfPlayers - 1).setPlayerTurn(false);
        ArrayList<Player> p = new ArrayList<>();
        p.add(getFirstPlayer());
        players.remove(getFirstPlayer());
        if (players.size() == 1)
            p.add(players.get(0));
        else {
            if (players.get(0).getID() == p.get(0).getID() % 3 + 1) {
                p.add(players.get(0));
                p.add(players.get(1));
            } else {
                p.add(players.get(1));
                p.add(players.get(0));
            }
        }
        players = p;
        getFirstPlayer().setPlayerTurn(true);
    }

}
