package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.enumerations.*;
import it.polimi.ingsw.observers.Observable;

import java.io.Serial;
import java.io.Serializable;
import java.security.InvalidParameterException;
import java.util.*;
import java.util.stream.Collectors;


/**
 * This class represents the Game entity.
 * @author Alessandro F.
 * @version 1.0
 */

public class Game extends Observable implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
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
        IslandTile islandToCheck;
        TowerColor influenceWinner;
        islandToCheck = gameBoard.moveMotherNature(num);
        if(!islandToCheck.isForbidden()) {
            try{
                influenceWinner = gameBoard.checkInfluence(islandToCheck);
            }catch(RuntimeException e){
                notifyObservers();
                return;
            }

            try {
                gameBoard.swapTowers(islandToCheck, influenceWinner);
            } catch(TowerWinException e){
                notifyObservers();
                throw e;
            }
            try {
                gameBoard.checkForArchipelago(islandToCheck);
            }catch(NumOfIslandsException e){
                notifyObservers();
                throw e;
            }
            catch(RuntimeException e){
                notifyObservers();
            }
        }
        else {
            getCharacterByName(Characters.GRANDMA_HERB).addNoEntryTile();
            islandToCheck.removeNoEntry();
        }

        notifyObservers();
        System.out.println("END OF MOVEMN in GAME");
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
    public void endPlayerTurn(Player currentPlayer) throws EndRoundException {

        if(players.indexOf(currentPlayer) == players.size()-1)
            throw new EndRoundException();
        else {
            currentPlayer.setPlayerTurn(false);
            Player nextPlayer = players.get(players.indexOf(currentPlayer) + 1);
            nextPlayer.setPlayerTurn(true); // the players are sorted based on their played assistant cards
            if(!nextPlayer.isConnected()){
                endPlayerTurn(nextPlayer);
            }
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
    public void move (Color color, TileWithStudents origin, TileWithStudents destination) throws FullDestinationException, InvalidParameterException {
        try {
            gameBoard.move(color, origin, destination);
        }catch (Exception e){
            notifyObservers();
            throw e;
        }
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

        notifyObservers();
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

        ArrayList<Player> nonConnected = players.stream().filter(x -> !x.isConnected()).collect(Collectors.toCollection(ArrayList :: new));

        players = players.stream()
                .filter(Player::isConnected)
                .sorted(Comparator.comparingInt((p) -> p.getPlayedCard().getCardValue()))
                .collect(Collectors.toCollection(ArrayList :: new));

        players.addAll(nonConnected);

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
        throw new RuntimeException("First player not found"); //If a bug happens
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
        if (getFirstPlayer().isConnected()) {
            getFirstPlayer().setPlayerTurn(true);
        }else{
            try {
                endPlayerTurn(getFirstPlayer());
            }catch (EndRoundException e){
                e.printStackTrace();
            }
        }

    }

    /**
     * Finds the winner at the end of the game by checking the number of towers of each player and, in case of a tie, checks the number of Professors owned
     * @return the nickname of the winner
     */
    public String checkWinner (){

        int minTower;
        Optional<Integer> maxProfessors;
        ArrayList<Player> playersCopy = new ArrayList<>(players);
        HashMap<Player, Integer> sameNumOfTowers = new HashMap<>();
        Player winner;

        minTower = gameBoard.getPlayerBoard(playersCopy.get(0)).getTowerZone().getNumOfTowers();
        winner = playersCopy.get(0);


        playersCopy.remove(0);

        for(Player p : playersCopy){
            if(gameBoard.getPlayerBoard(p).getTowerZone().getNumOfTowers() < minTower){
                minTower = gameBoard.getPlayerBoard(p).getTowerZone().getNumOfTowers();
                winner = p;
            }
            else if(gameBoard.getPlayerBoard(p).getTowerZone().getNumOfTowers() == minTower) {
                sameNumOfTowers.put(p, 0);
                if(!sameNumOfTowers.containsKey(winner))
                    sameNumOfTowers.put(winner, 0);
            }

        }

        if(!sameNumOfTowers.containsKey(winner))
            return winner.getNickName();

        else {
            for(Color c : Color.values()){ //if a professor is owned by a player and the owner is in the "Tie" Map
                if(gameBoard.getProfessors().containsKey(c) && sameNumOfTowers.containsKey(gameBoard.getProfessorOwnerByColor(c)))
                    sameNumOfTowers.put(gameBoard.getProfessorOwnerByColor(c), sameNumOfTowers.get(gameBoard.getProfessorOwnerByColor(c)) + 1);
            }

            maxProfessors = sameNumOfTowers.values().stream().max((p1, p2) -> p1 > p2 ? 1 : p1.equals(p2) ? 0 : -1); //get the max number of prof owned by a player

            if(sameNumOfTowers.values().stream().filter(p -> p.equals(maxProfessors.orElse(-1))).count() > 1) //Checks if someone else has Max professor, if he does it's a tie (2 prof p1, 2 prof p2)
                return("Tie");



            return Objects.requireNonNull(sameNumOfTowers.entrySet()
                    .stream()
                    .max((p1, p2) -> p1.getValue() > p2.getValue() ? 1 : p1.getValue().equals(p2.getValue()) ? 0 : -1)
                    .orElse(null))
                    .getKey().getNickName();
        }


    }

    /**
     * @return The available cards of a player
     */
    public ArrayList<AssistantCard> getPlayerDeck(){
        return new ArrayList<>(getCurrentPlayer().getDeck().getCards());
    }

    /**
     * Fills the Clouds after they are chosen
     * @throws EmptyBagException Thrown if the bag is empty
     */
    public void fillClouds() throws EmptyBagException {
        gameBoard.fillClouds();
        notifyObservers();
    }

    /**
     * @param characterName Name of the character needed
     * @return The CharacterCard with the specified name
     */
    public CharacterCard getCharacterByName(Characters characterName){
        for(CharacterCard character : getGameBoard().getCharacters()){
            if(character.getName().equals(characterName))
                return character;
        }
        throw new RuntimeException("Character not found");
    }

    /**
     * Checks the FULL influence (All students' colors + Towers) on the desired Island and swaps the Towers accordingly to the winner
     * @param islandToCheck The Island on which to calculate the influence
     */
    public void checkInfluence(IslandTile islandToCheck) throws TowerWinException {

        TowerColor winner = gameBoard.checkInfluence(islandToCheck);
        try {
            gameBoard.swapTowers(islandToCheck, winner);

        }catch(TowerWinException e){
            notifyObservers();
            throw e;
        }
        notifyObservers();
    }

    /**
     * Checks if an Archipelago has to be created between adjacent Islands
     * @param currentIsland The island on which the method starts checking
     */
    public void checkForArchipelago (IslandTile currentIsland) throws NumOfIslandsException {
        try {
            gameBoard.checkForArchipelago(currentIsland);
        } catch (NumOfIslandsException e) {
            notifyObservers();
            throw e;
        }
        notifyObservers();
    }

    /**
     * Checks the Ownership of the Professors after a student is moved
     */
    public void checkProfessorsOwnership(){
        gameBoard.checkProfessorOwnership();
        notifyObservers();
    }


}
