package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enumerations.Color;
import it.polimi.ingsw.model.enumerations.TowerColor;
import it.polimi.ingsw.exceptions.*;

import java.io.Serializable;
import java.security.InvalidParameterException;
import java.util.*;

/**
 * Represents the game-board.
 * @author A.G. Gaillet
 * @version 1.3
 * @serial
 * @see CloudTile
 * @see IslandTile
 * @see MotherNature
 * @see Player
 * @see PlayerBoard
 * @see Color
 * @see TowerColor
 */
public class GameBoard implements Serializable {
    private final ArrayList<CloudTile> clouds;
    private final ArrayList<IslandTile> islands;
    private final MotherNature motherNature;
    private final HashMap<Player, PlayerBoard> playerBoards;
    private final EnumMap<Color, Player> professors;
    private final Bag bag;
    private static final int maxNumIslands = 12;
    private static final long serialVersionUID = 1L;

    /**
     * Constructor of the class; requires an {@link ArrayList} containing all {@link Player} (players).
     *
     * @param players {@link ArrayList} of {@link Player} containing references to all players of the game
     */
    public GameBoard(ArrayList<Player> players) {
        bag = new Bag();
        clouds = new ArrayList<>();
        for (int i = 0; i < players.size(); i++) {
            clouds.add(new CloudTile(players.size()));
        }

        islands = new ArrayList<>();
        for (int i = 0; i < maxNumIslands; i++) {
            islands.add(new IslandTile());
        }

        motherNature = new MotherNature(islands);
        int idxStartingIsland = islands.indexOf(motherNature.getCurrentIsland());
        for (int idx = 0; idx < islands.size(); idx++) {
            if (idx != (idxStartingIsland + (maxNumIslands / 2))) {
                try{
                    Color student = bag.getStudents(1).get(0);
                    islands.get(idx).addStudent(student);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        playerBoards = new HashMap<>();
        for (Player player : players) {
            playerBoards.put(player, new PlayerBoard(players.size()));
            //Fill entrances with students
            int max = playerBoards.get(player).getEntrance().getMaxStudents();
            try {
                ArrayList<Color> students = bag.getStudents(max);
                for (int i = 0; i < max; i++) {
                    try {
                        playerBoards.get(player).getEntrance().addStudent(students.get(i));
                    } catch (FullDestinationException e){
                        try{
                            bag.addStudents(1, students.get(i));
                        } catch (InvalidParameterException ex){
                            ex.printStackTrace();
                        }
                        e.printStackTrace();
                    }
                }
            }catch (EmptyBagException e){
                e.printStackTrace();
            }
        }

        professors = new EnumMap<>(Color.class);
    }

    /**
     * Alternative Constructor, useful in testing. Allows for external initialization of all non-static attributes.
     */
    public GameBoard(ArrayList<CloudTile> clouds, ArrayList<IslandTile> islands, MotherNature motherNature, HashMap<Player, PlayerBoard> playerBoards, EnumMap<Color, Player> professors, Bag bag) {
        this.clouds = clouds;
        this.islands = islands;
        this.motherNature = motherNature;
        this.playerBoards = playerBoards;
        this.professors = professors;
        this.bag = bag;
    }

    /**
     * Returns the reference to the memorized {@link ArrayList} of {@link CloudTile}.
     *
     * @return {@link ArrayList} of memorized clouds
     */
    public ArrayList<CloudTile> getClouds() {
        return clouds;
    }

    /**
     * Returns reference to the memorized {@link ArrayList} of {@link IslandTile}.
     *
     * @return {@link ArrayList} of memorized islands
     */
    public ArrayList<IslandTile> getIslands() {
        return islands;
    }

    /**
     * Returns the reference to the memorized {@link EnumMap} linking professors as {@link Color} to their owner as {@link Player}.
     *
     * @return {@link EnumMap} mapping professors to {@link Player} owner
     */
    public EnumMap<Color, Player> getProfessors() {
        return professors;
    }

    /**
     * Returns the {@link Player} owner of the professor of the given {@link Color}.
     *
     * @param color given {@link Color} of the professor
     * @return owner
     */
    public Player getProfessorOwnerByColor(Color color) {
        return professors.get(color);
    }

    /**
     * Returns the reference to the {@link PlayerBoard} linked to the given {@link Player}.
     *
     * @param p {@link Player} of whom I want to get the {@link PlayerBoard}
     * @return the {@link PlayerBoard} linked to the given {@link Player}
     */
    public PlayerBoard getPlayerBoard(Player p) {
        return playerBoards.get(p);
    }

    /**
     * Returns the reference to the memorized {@link MotherNature}.
     *
     * @return {@link MotherNature} object linked to this instance
     */
    public MotherNature getMotherNature() {
        return motherNature;
    }

    /**
     * Moves {@link MotherNature} by a given number of positions and returns final {@link IslandTile}. Merges islands if necessary.
     *
     * @param pos number of positions
     * @return final position as {@link IslandTile}
     */
    public IslandTile moveMotherNature(int pos) {
        motherNature.move(pos);
        return motherNature.getCurrentIsland();
    }

    /**
     * Checks if any {@link Player} has an empty {@link TowerZone}.
     *
     * @return the {@link Player} that does have an empty {@link TowerZone} if it is present
     */
    public Optional<Player> checkEmptyTowerZone() {
        ArrayList<Player> players = new ArrayList<>(playerBoards.keySet());
        if (players.size() != 0) {
            for (Player player : players) {
                if (playerBoards.get(player).getTowerZone().getNumOfTowers() == 0)
                    return Optional.of(player);
            }
        }
        return Optional.empty();
    }

    /**
     * Checks if the {@link Bag} is empty.
     *
     * @return true if the {@link Bag} is empty, false otherwise
     */
    public boolean checkEmptyBag() {
        return bag.numRemaining() == 0;
    }

    /**
     * Moves a student of a given {@link Color} from a given origin to a given destination. Reassigns professors if needed.
     *
     * @param color       {@link Color} of the student to be moved
     * @param origin      {@link TileWithStudents} of origin for the movement
     * @param destination {@link TileWithStudents} of destination for the movement
     * @throws InvalidParameterException when given {@link Color} is not available in given origin
     * @throws FullDestinationException  when given destination is full
     */
    public void move(Color color, TileWithStudents origin, TileWithStudents destination) throws InvalidParameterException, FullDestinationException {
        try {
            origin.removeStudent(color);
        } catch (RuntimeException e) {
            throw new InvalidParameterException();
        }

        try {
            destination.addStudent(color);
        }
        catch (FullDestinationException ex) {
            try {
                bag.addStudents(1, color);
            } catch (InvalidParameterException exc){
                exc.printStackTrace();
            }
            throw new FullDestinationException();
        }
        catch (RuntimeException ex) {
            for(Player p : playerBoards.keySet())
                if(getPlayerBoard(p).getDiningRoom() == destination)
                    getPlayerBoard(p).addCoin();
        }

        if (destination.getClass().equals(DiningRoom.class)) {
            checkProfessorOwnership();
        }
    }

    /**
     * Moves all students from the selected {@link CloudTile} to the {@link Entrance} of the given {@link Player}.
     *
     * @param cloud  chosen {@link CloudTile}
     * @param player who initiated the action
     * @throws FullDestinationException when given {@link Entrance} becomes full before the move is finished (In this case all available spaces are filled and remaining students are returned to the {@link Bag})
     * @throws CloudNotFullException    when given {@link CloudTile} is not full
     */
    public void chooseCloud(CloudTile cloud, Player player) throws FullDestinationException, CloudNotFullException {
        Entrance entrance = playerBoards.get(player).getEntrance();
        ArrayList<Color> students = cloud.removeAllStudents();
        boolean fullDestFlag = false;
        for (Color student : students) {
            if (!fullDestFlag) {
                try {
                    entrance.addStudent(student);
                } catch (FullDestinationException ex) {
                    try {
                        bag.addStudents(1, student);
                    } catch (InvalidParameterException e) {
                        //Ignore this exception, it is due to the structure of the test
                    }
                    fullDestFlag = true;
                }
            } else {
                try {
                    bag.addStudents(1, student);
                } catch (InvalidParameterException e) {
                    //Ignore this exception, it is due to the structure of the test
                }
            }
        }
        if (fullDestFlag) throw new FullDestinationException();
    }

    /**
     * Refills all {@link CloudTile} using students from the {@link Bag}.
     */
    public void fillClouds() throws EmptyBagException {
        for (CloudTile cloud : clouds) {
            if (cloud.getNumStudents() == 0) {
                ArrayList<Color> students = bag.getStudents(cloud.getMaxStudents());
                for (Color student : students) {
                    try {
                        cloud.addStudent(student);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * Changes the {@link TowerColor} of all towers on the given {@link IslandTile}.
     *
     * @param island   target {@link IslandTile}
     * @param newColor new {@link TowerColor} of the tower(s)
     * @throws IllegalArgumentException when given {@link TowerColor} is {@link TowerColor#NONE}
     * @throws TowerWinException when a {@link TowerZone} becomes empty, in this case the message contains the nickname of the {@link Player} that won
     */
    public void swapTowers(IslandTile island, TowerColor newColor) throws IllegalArgumentException, TowerWinException{
        TowerColor oldColor = island.getTowerColor();
        island.swapTower(newColor);

        for (int i = 0; i < island.getNumTowers(); i++){
            if (!oldColor.equals(TowerColor.NONE))
                try { getTowerZone(oldColor).add(); } catch (InvalidParameterException e) { e.printStackTrace(); }

            try { getTowerZone(newColor).remove(); } catch (RuntimeException e) { e.printStackTrace(); }
            catch (TowerWinException ex) {
                Optional<Player> p = getPlayerByTowerColor(newColor);
                if(p.isPresent())
                    throw new TowerWinException(p.get().getNickName() + "has won !");
            }
        }
    }

    /**
     * Creates an archipelago from the current {@link IslandTile} and adjacent tiles when merging conditions are met.
     *
     * @param currentIsland {@link IslandTile} where {@link MotherNature} is currently standing
     * @throws NumOfIslandsException when the minimum number of remaining islands is reached
     */
    public void checkForArchipelago(IslandTile currentIsland) throws NumOfIslandsException{
        int currentIdx = islands.indexOf(currentIsland);
        int nextIdx;
        if (currentIdx == islands.size() - 1) nextIdx = 0;
        else nextIdx = currentIdx + 1;
        checkForArchipelagoStep(currentIdx, nextIdx);
        //currentIdx = islands.indexOf(currentIsland);

        int prevIdx;
        if (currentIdx == 0) prevIdx = islands.size() - 1;
        else prevIdx = currentIdx - 1;
        checkForArchipelagoStep(prevIdx, currentIdx);
    }

    /**
     * Called by the {@link GameBoard#checkForArchipelago(IslandTile) checkForArchipelago} method for checking that merge conditions are met and for the merging of the islands.
     *
     * @param firstIslandIdx  index of first Island
     * @param secondIslandIdx index of the second island
     * @throws NumOfIslandsException when the minimum number of remaining islands is reached
     */
    private void checkForArchipelagoStep(int firstIslandIdx, int secondIslandIdx) throws NumOfIslandsException{
        IslandTile isl1 = islands.get(firstIslandIdx);
        IslandTile isl2 = islands.get(secondIslandIdx);
        if (isl1.getTowerColor().equals(isl2.getTowerColor())) {
            for (Color color : Color.values()) {
                int numStud = isl2.getNumStudents(color);
                for (int i = 0; i < numStud; i++) {
                    try {
                        isl1.addStudent(color);
                    } catch (FullDestinationException e) {
                        e.printStackTrace();
                    }
                }
            }
            for (int i = 0; i < isl2.getNumTowers(); i++) {
                isl1.mergeTower();
            }
            for (int i = 0; i < isl2.getNumOfNoEntryTiles(); i++) {
                isl1.addNoEntry();
            }
            islands.remove(isl2);

            if (islands.size() == 3){
                throw new NumOfIslandsException();
            }
        }
    }

    /**
     * Returns reference to the {@link TowerZone} with towers of the given {@link TowerColor}.
     *
     * @param color target {@link TowerColor}
     * @return {@link TowerZone} corresponding to the given {@link TowerColor}
     * @throws InvalidParameterException when the given {@link TowerColor} is not found
     */
    private TowerZone getTowerZone(TowerColor color) throws InvalidParameterException {
        ArrayList<Player> players = new ArrayList<>(playerBoards.keySet());
        for (Player player : players) {
            if (player.getTowerColor().equals(color)) {
                return playerBoards.get(player).getTowerZone();
            }
        }
        throw new InvalidParameterException();
    }

    /**
     * Reassigns all professors to their rightful owner.
     */
    private void checkProfessorOwnership() {
        for (Color color : Color.values()) {
            if (professors.containsKey(color)) {
                Player owner = professors.get(color);
                for (Player player : playerBoards.keySet()) {
                    if (playerBoards.get(player).getDiningRoom().getNumStudents(color) > playerBoards.get(owner).getDiningRoom().getNumStudents(color))
                        owner = player;
                }
                professors.put(color, owner);
            } else {
                for (Player player : playerBoards.keySet()) {
                    if (playerBoards.get(player).getDiningRoom().getNumStudents(color) > 0) {
                        professors.put(color, player);
                        checkProfessorOwnership();
                    }
                }
            }
        }
    }

    /**
     * Utility method to compute the part of the total influence gained by the presence of the students
     * @param color the color of the students on the island we are assigning the influence
     * @param islandToCheck the island where it's been checking the influence of each player
     * @param playersInfluence maps each player with his current influence on the island
     */
    protected void computeInfByColor(Color color, IslandTile islandToCheck, HashMap<Player, Integer> playersInfluence) {
        if(getProfessorOwnerByColor(color) != null) {
            Player ownerOfProf = getProfessorOwnerByColor(color);
            playersInfluence.put(ownerOfProf, playersInfluence.get(ownerOfProf) + islandToCheck.getNumStudents(color));
        }
    }

    /**
     * Utility method to compute the part of the total influence gained by the presence of the tower(s)
     * @param islandToCheck the island where it's been checking the influence of each player
     * @param playersInfluence maps each player with his current influence on the island.
     *                         Only one (or none) of the all influences will be modified due to the presence of the tower(s).
     */
    protected void computeInfByTower(IslandTile islandToCheck, HashMap<Player, Integer> playersInfluence) {
        getPlayerByTowerColor(islandToCheck.getTowerColor()).ifPresent(player -> playersInfluence.put(player, playersInfluence.get(player) + islandToCheck.getNumTowers()));
    }

    /**
     * Utility method to decide who is going to be the new owner of the island (could be the old one),
     * by checking the influences values computed previously.
     * It's pretty long because there are many "particular" scenarios to be handled.
     * @param playersInfluence maps each player with his current influence on the island.
     * @return the TowerColor linked to who won the contention.
     * @throws RuntimeException when nobody won.
     */
    protected TowerColor computeMaxInfluence(HashMap<Player, Integer> playersInfluence) {
        Player mostInfPlayer = playersInfluence.keySet().stream().reduce((a,b) -> playersInfluence.get(a) > playersInfluence.get(b) ? a : b).orElse(null);
        if (mostInfPlayer!=null && playersInfluence.keySet().stream().filter(x -> playersInfluence.get(x).intValue() == playersInfluence.get(mostInfPlayer).intValue()).count() == 1)
            return mostInfPlayer.getTowerColor();
        else
            throw new RuntimeException();
    }

    /**
     * Function called everytime MotherNature moves on an island.
     * Determines which player has the right to conquer the island.
     * Three distinct parts: the first one computes the influence due to the presence of the students.
     *                       the second one adds the influence due to the presence of tower(s).
     *                       the third one determines the return of the all checkInfluence method.
     * @param islandToCheck is the island where it's been checking the influence.
     * @return the TowerColor of the owner of the island (can be the previous owner!)
     * @throws RuntimeException when there isn't a player with more influence than any other player.
     */
    protected TowerColor checkInfluence(IslandTile islandToCheck) throws RuntimeException {
        //playersInfluence is a map that matches every player with his actual influence on the islandToCheck
        HashMap<Player, Integer> playersInfluence = new HashMap<>();
        for (Player p : playerBoards.keySet())
            playersInfluence.put(p, 0); // set each player's influence to 0

        for (Color color : Color.values())
            computeInfByColor(color, islandToCheck, playersInfluence); //compute the first part of the influence

        computeInfByTower(islandToCheck, playersInfluence); //compute the second part of the influence

        return computeMaxInfluence(playersInfluence);
    }

    /**
     * Returns {@link Player} linked to the given {@link TowerColor}
     * @param color of the tower
     * @return {@link Player} which owns towers of the given color
     */
    private Optional<Player> getPlayerByTowerColor(TowerColor color){
        return playerBoards.keySet().stream().filter(player -> player.getTowerColor().equals(color)).findFirst();
    }
}
