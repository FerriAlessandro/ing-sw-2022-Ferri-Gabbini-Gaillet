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
 * @version 1.0
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
    private final Bag bag = new Bag();
    private static final int maxNumIslands = 12;
    private static final long serialVersionUID = 1L;

    /**
     * Constructor of the class; requires an {@link ArrayList} containing all {@link Player} (players).
     *
     * @param players {@link ArrayList} of {@link Player} containing references to all players of the game
     */
    public GameBoard(ArrayList<Player> players) {
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
                Color student = bag.getStudents(1).get(0);
                try {
                    islands.get(idx).addStudent(student);
                } catch (FullDestinationException e) {
                    e.printStackTrace();
                }
            }
        }

        playerBoards = new HashMap<>();
        ArrayList<TowerColor> t_col = new ArrayList<>();
        t_col.add(TowerColor.BLACK);
        t_col.add(TowerColor.WHITE);
        if (players.size() == 3) {
            t_col.add(TowerColor.GRAY);
        }
        for (Player player : players) {
            playerBoards.put(player, new PlayerBoard(t_col.get(players.indexOf(player) % 2), players.size()));
        }

        professors = new EnumMap<>(Color.class);
    }

    /**
     * Alternative Constructor, useful in testing. Allows for external initialization of all non-static attributes.
     */
    public GameBoard(ArrayList<CloudTile> clouds, ArrayList<IslandTile> islands, MotherNature motherNature, HashMap<Player, PlayerBoard> playerBoards, EnumMap<Color, Player> professors) {
        this.clouds = clouds;
        this.islands = islands;
        this.motherNature = motherNature;
        this.playerBoards = playerBoards;
        this.professors = professors;
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
        checkForArchipelago(motherNature.getCurrentIsland());
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
        } catch (FullDestinationException ex) {
            throw new FullDestinationException();
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
                        e.printStackTrace();
                    }
                    fullDestFlag = true;
                }
            } else {
                try {
                    bag.addStudents(1, student);
                } catch (InvalidParameterException e) {
                    e.printStackTrace();
                }
            }
        }
        if (fullDestFlag) throw new FullDestinationException();
    }

    /**
     * Refills all {@link CloudTile} using students from the {@link Bag}.
     */
    public void fillClouds() {
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
     */
    public void swapTowers(IslandTile island, TowerColor newColor) {
        TowerColor oldColor = island.getTowerColor();
        try {
            island.swapTower(newColor);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        try {
            if (!oldColor.equals(TowerColor.NONE))
                getTowerZone(oldColor).add();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            getTowerZone(newColor).remove();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates an archipelago from the current {@link IslandTile} and adjacent tiles when merging conditions are met.
     *
     * @param currentIsland {@link IslandTile} where {@link MotherNature} is currently standing
     */
    public void checkForArchipelago(IslandTile currentIsland) {
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
     */
    private void checkForArchipelagoStep(int firstIslandIdx, int secondIslandIdx) {
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
            //checkForArchipelago(islands.get(firstIslandIdx)); //recursive step
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
        int valueToAdd = islandToCheck.getNumStudents(color);
        Player ownerOfProf;
        if(getProfessorOwnerByColor(color) != null) {
            ownerOfProf = getProfessorOwnerByColor(color);
            for(Player p : playersInfluence.keySet())
                if(p.equals(ownerOfProf)) {
                    int previousInfluence = playersInfluence.get(p); //the influence computed since now
                    playersInfluence.put(p, previousInfluence + valueToAdd); //the new total influence
                }
        }
    }

    /**
     * Utility method to compute the part of the total influence gained by the presence of the tower(s)
     * @param islandToCheck the island where it's been checking the influence of each player
     * @param playersInfluence maps each player with his current influence on the island.
     *                         Only one (or none) of the all influences will be modified due to the presence of the tower(s).
     */
    protected void computeInfByTower(IslandTile islandToCheck, HashMap<Player, Integer> playersInfluence) {
        int valueToAdd = islandToCheck.getNumTowers();
        TowerColor colorOfTheOwner = islandToCheck.getTowerColor(); //it's the current possessor's island tower's color
        for(Player p : playerBoards.keySet())
            if (colorOfTheOwner.equals(p.getTowerColor())) {
                int previousInfluence = playersInfluence.get(p); //the influence computed since now
                playersInfluence.put(p, previousInfluence + valueToAdd); //p is the current owner of the tower
            }
    }

    /**
     * Utility method to decide who is going to be the new owner of the island (could be the old one),
     * by checking the influences values computed previously.
     * It's pretty long because there are many "particular" scenarios to be handled.
     * @param islandToCheck the island where it's been checking the influence of each player
     * @param playersInfluence maps each player with his current influence on the island.
     *                         At this point the influences values won't change.
     * @return the TowerColor of the (new) owner of the island.
     * @throws RuntimeException when every player gets 0 influence, so nobody won.
     */
    protected TowerColor computeMaxInfluence(IslandTile islandToCheck, HashMap<Player, Integer> playersInfluence) {
        int infMax = 0; //the current value of the max influence
        Player newOwnerOfTheIsland = null; //actually it may happen that nobody gets the island
        TowerColor actualColor = islandToCheck.getTowerColor(); //just to avoid a too long if
        for(Player p: playerBoards.keySet())
            //the following if statement can be validated in two scenarios:
            //1- there is a player with more influence than any other players (more common scenario)
            //2- the currentOwner ties with another player, hence he keeps the island
            if((playersInfluence.get(p) > infMax) || (playersInfluence.get(p) == infMax && p.getTowerColor() == actualColor)) {
                infMax = playersInfluence.get(p);
                newOwnerOfTheIsland = p;
            }

        //handles the very corner case of 3 players-game:
        //when two players tie and their influence is higher than the oldOwner, the island must remain of the oldOwner
        Player oldOwnerOfTheIsland = null;
        for(Player p : playerBoards.keySet())
            if (p.getTowerColor().equals(actualColor))
                oldOwnerOfTheIsland = p; //find the actualOwner
        if(playersInfluence.get(oldOwnerOfTheIsland) != infMax) {
            int counterOfMaxInfluences = 0;
            for(Player p : playerBoards.keySet())
                if(playersInfluence.get(p) == infMax)
                    counterOfMaxInfluences ++;
            if(counterOfMaxInfluences == 2) //if this happens then the island must not change owner
                newOwnerOfTheIsland = oldOwnerOfTheIsland;
        }

        //handles the case with no winner (can happen in the early rounds)
        if(newOwnerOfTheIsland == null)
            throw new RuntimeException("Every player with 0 influence: no need to swap Tower!");

        //normal return of the function
        return newOwnerOfTheIsland.getTowerColor();
    }

    /**
     * Function called everytime MotherNature moves on an island.
     * Determines which player has the right to conquer the island.
     * Three distinct parts: the first one computes the influence due to the presence of the students.
     *                       the second one adds the influence due to the presence of tower(s).
     *                       the third one determines the return of the all checkInfluence method.
     * @param islandToCheck is the island where it's been checking the influence.
     * @return the TowerColor of the owner of the island (can be the previous owner!)
     * @throws RuntimeException when every player gets 0 influence, so nobody won.
     */
    protected TowerColor checkInfluence(IslandTile islandToCheck) throws RuntimeException {
        //playersInfluence is a map that matches every player with his actual influence on the islandToCheck
        HashMap<Player, Integer> playersInfluence = new HashMap<>();
        for (Player p : playerBoards.keySet())
            playersInfluence.put(p, 0); // set each player's influence to 0

        for (Color color : Color.values())
            computeInfByColor(color, islandToCheck, playersInfluence); //compute the first part of the influence

        computeInfByTower(islandToCheck, playersInfluence); //compute the second part of the influence

        return computeMaxInfluence(islandToCheck, playersInfluence);

    }

}
