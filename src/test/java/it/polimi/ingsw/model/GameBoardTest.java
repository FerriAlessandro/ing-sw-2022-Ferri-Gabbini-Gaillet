package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.enumerations.Characters;
import it.polimi.ingsw.model.enumerations.Color;
import it.polimi.ingsw.model.enumerations.TowerColor;
import it.polimi.ingsw.model.enumerations.Wizard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.security.InvalidParameterException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class GameBoardTest {

    GameBoard gb;
    MotherNature mn;
    ArrayList<IslandTile> islands = new ArrayList<>();
    ArrayList<CloudTile> clouds = new ArrayList<>();
    ArrayList<CharacterCard> characters = new ArrayList<>();
    HashMap<Player, PlayerBoard> playerBoards = new HashMap<>();
    EnumMap<Color, Player> professors = new EnumMap<>(Color.class);
    int numPlayers = 3;
    Bag bg = new Bag();

    MotherNature tmn;

    @BeforeEach
    void setUp() {
        IslandTile isl1 = new IslandTile();
        islands.add(isl1);
        mn = new MotherNature(islands);
        tmn = new MotherNature(islands);
        for (int i = 1; i < 6; i++) {
            isl1 = new IslandTile();
            islands.add(isl1);
        }

        for (int i = 0; i < numPlayers; i++) {
            CloudTile cl1 = new CloudTile(numPlayers);
            clouds.add(cl1);
        }

        ArrayList<TowerColor> t1 = new ArrayList<>();
        t1.add(TowerColor.BLACK);
        t1.add(TowerColor.WHITE);
        t1.add(TowerColor.GRAY);

        for (int i = 0; i< numPlayers; i++) {
            AssistantDeck ad = new AssistantDeck(Wizard.WIZARD_1);
            Player pl1 = new Player(1, ad, "test", false, false, t1.get(i % t1.size()));
            PlayerBoard pb1 = new PlayerBoard(numPlayers);
            playerBoards.put(pl1, pb1);
        }
        gb = new GameBoard(clouds, islands, characters, mn, playerBoards, professors, bg);
    }

    @Test
    @DisplayName("Check that MotherNature's moves are executed as per MotherNature definition")
    void moveMotherNature() {
        int pos = 25;
        tmn.move(pos);
        assertEquals(tmn.getCurrentIsland(), gb.moveMotherNature(pos));
    }

    @Test
    void checkForArchipelago(){
        int originalSize = islands.size();
        for (IslandTile island : islands) {
            island.swapTower(TowerColor.BLACK);
            try {
                island.addStudent(Color.GREEN);
            }catch (FullDestinationException e){
                e.printStackTrace();
                fail();
            }
            island.addNoEntry();
        }
        islands.get(4).swapTower(TowerColor.WHITE);
        islands.get(4).removeNoEntry();
        try {
            gb.checkForArchipelago(islands.get(1));
        } catch (NumOfIslandsException e){
            fail();
        }
        assertEquals(originalSize - 2, islands.size());
        assertEquals(TowerColor.BLACK, islands.get(1).getTowerColor());
        assertEquals(TowerColor.WHITE, islands.get(islands.size() - 2).getTowerColor());
        assertEquals(1, islands.get(1).getNumStudents(Color.GREEN));
        assertEquals(3, islands.get(0).getNumStudents(Color.GREEN));
        assertEquals(1, islands.get(1).getNumTowers());
        assertEquals(3, islands.get(0).getNumTowers());
        assertEquals(3, islands.get(0).getNumOfNoEntryTiles());
        assertTrue(islands.get(0).isForbidden());
        assertFalse(islands.get(2).isForbidden());

        assertThrows(NumOfIslandsException.class, () -> gb.checkForArchipelago(islands.get(1)));
        assertEquals(3, islands.size());
    }

    @Test
    void checkEmptyTowerZone() {
        for (Player p : playerBoards.keySet()){
            PlayerBoard pb = playerBoards.get(p);
            if(pb.getTowerZone().getNumOfTowers() == 0){
                pb.getTowerZone().add();
            }
        }
        Optional<Player> opt = gb.checkEmptyTowerZone();
        assertTrue(opt.isEmpty());
        Player p = playerBoards.keySet().iterator().next();
        while (playerBoards.get(p).getTowerZone().getNumOfTowers() > 1) {
            try {
                playerBoards.get(p).getTowerZone().remove();
            } catch (Exception e) {
                fail();
            }
        }
        assertThrows(TowerWinException.class, () -> playerBoards.get(p).getTowerZone().remove());

        Optional<Player> opt2 = gb.checkEmptyTowerZone();
        assertTrue(opt2.isPresent());
        assertEquals(opt2.get(), p);
    }

    @Test
    @DisplayName("Test move method general behaviour")
    void move(){
        IslandTile is1 = new IslandTile();
        int numPlayers = 2;
        int maxStud = 7;
        Entrance en1 = new Entrance(numPlayers);
        //Check behaviour when given origin is empty


        assertThrows(InvalidParameterException.class, () -> gb.move(Color.GREEN, en1, is1));
        try {
            en1.addStudent(Color.GREEN);
        }catch (FullDestinationException e){
            e.printStackTrace();
            fail();
        }
        //Check behaviour in nominal conditions
        try {
            gb.move(Color.GREEN, en1, is1);
        } catch (FullDestinationException e) {
            e.printStackTrace();
            fail();
        }
        assertEquals(0, en1.getNumStudents(Color.GREEN));
        assertEquals(1, is1.getNumStudents(Color.GREEN));

        for (int i = 0; i < maxStud; i++) {
            try {
                is1.addStudent(Color.GREEN);
            } catch (FullDestinationException e){
                e.printStackTrace();
                fail();
            }
            try {
                gb.move(Color.GREEN, is1, en1);
            } catch (InvalidParameterException | FullDestinationException e) {
                e.printStackTrace();
                fail();
            }
        }
        //Check behaviour when given destination is full
        try {
            ArrayList<Color> stud = new ArrayList<>(bg.getStudents(1));
            try{
                is1.addStudent(stud.get(0));
            } catch (Exception e){
                fail();
            }
            int numOrigin = is1.getNumStudents();
            int numDest = en1.getNumStudents();
            assertThrows(FullDestinationException.class, () -> gb.move(stud.get(0), is1, en1));
            assertEquals(numOrigin, is1.getNumStudents());
            assertEquals(numDest, en1.getNumStudents());
        } catch (EmptyBagException e) {
            fail();
        }
    }

    @Test
    @DisplayName("Test that professors are assigned correctly in move")
    void moveCheckProf() throws FullDestinationException{
        assertTrue(gb.getProfessors().keySet().isEmpty());
        Player p1 = playerBoards.keySet().iterator().next();
        Player p2 = playerBoards.keySet().iterator().next();
        IslandTile is1 = islands.get(0);
        is1.addStudent(Color.GREEN);
        gb.move(Color.GREEN, is1, playerBoards.get(p1).getDiningRoom());
        assertEquals(p1, gb.getProfessorOwnerByColor(Color.GREEN));
        is1.addStudent(Color.GREEN);
        is1.addStudent(Color.GREEN);
        gb.move(Color.GREEN, is1, playerBoards.get(p2).getDiningRoom());
        gb.move(Color.GREEN, is1, playerBoards.get(p2).getDiningRoom());
        assertEquals(p2, gb.getProfessorOwnerByColor(Color.GREEN));
    }

    @Test
    void chooseCloud() throws FullDestinationException {
        Player p1 = playerBoards.keySet().iterator().next();
        Entrance en1 = gb.getPlayerBoard(p1).getEntrance();
        CloudTile c1 = clouds.get(0);
        EnumMap<Color, Integer> students = new EnumMap<>(Color.class);
        c1.addStudent(Color.GREEN);
        c1.addStudent(Color.YELLOW);
        c1.addStudent(Color.BLUE);
        c1.addStudent(Color.BLUE);

        for (Color color : Color.values()){
            students.put(color, c1.getNumStudents(color));
        }
        try {
            gb.chooseCloud(c1,p1);
        }catch(CloudNotFullException | FullDestinationException e){
            fail();
        }
        for (Color color : students.keySet()){
            assertEquals(en1.getNumStudents(color), students.get(color));
        }

        assertThrows(CloudNotFullException.class, () -> gb.chooseCloud(c1, p1));

        CloudTile c2 = clouds.get(1);
        c2.addStudent(Color.GREEN);
        c2.addStudent(Color.YELLOW);
        c2.addStudent(Color.BLUE);
        c2.addStudent(Color.BLUE);
        try {
            gb.chooseCloud(c2,p1);
        }catch (Exception e){
            fail();
        }
        c2.addStudent(Color.GREEN);
        c2.addStudent(Color.YELLOW);
        c2.addStudent(Color.BLUE);
        c2.addStudent(Color.BLUE);
        assertThrows(FullDestinationException.class, () -> gb.chooseCloud(c2, p1));
    }

    @Test
    void fillClouds() throws FullDestinationException, EmptyBagException {
        //Check normal behaviour
        for (CloudTile cloud : clouds){
            for (int i = 0; i < cloud.getMaxStudents(); i++) {
                cloud.addStudent(Color.GREEN);
            }
        }
        for (CloudTile cloud: clouds) {
            try {
                cloud.removeAllStudents();
            }catch(CloudNotFullException e){
                fail();
            }
        }
        gb.fillClouds();
        for (CloudTile cloud: clouds){
            assertEquals(cloud.getNumStudents(), cloud.getMaxStudents());
        }

        //Check that non-empty clouds remain unchanged
        EnumMap<Color, Integer> studCol = new EnumMap<>(Color.class);
        CloudTile cloud = clouds.get(1);
        for (Color color : Color.values()){
            studCol.put(color, cloud.getNumStudents(color));
        }
        gb.fillClouds();
        for (Color color : Color.values()){
            assertEquals(studCol.get(color), cloud.getNumStudents(color));
        }
    }

    @Test
    void swapTowers() {
        IslandTile isl1 = islands.get(1);
        EnumMap<TowerColor, Integer> intMap = new EnumMap<>(TowerColor.class);
        ArrayList<TowerColor> colors = new ArrayList<>();
        for (Player player : playerBoards.keySet()){
            intMap.put(player.getTowerColor(), gb.getPlayerBoard(player).getTowerZone().getNumOfTowers());
            colors.add(player.getTowerColor());
        }

        EnumMap<TowerColor, TowerZone> towerMap = new EnumMap<>(TowerColor.class);
        for (Player player : playerBoards.keySet()){
            towerMap.put(player.getTowerColor(), playerBoards.get(player).getTowerZone());
        }

        for (TowerColor color : colors) {
            if (!color.equals(TowerColor.NONE)) {
                TowerColor oldColor = isl1.getTowerColor();
                try {
                    gb.swapTowers(isl1, color);
                }catch (Exception e){
                    fail();
                }
                if(!oldColor.equals(TowerColor.NONE)){
                    assertEquals(intMap.get(oldColor) + 1, towerMap.get(oldColor).getNumOfTowers());
                    intMap.put(oldColor, towerMap.get(oldColor).getNumOfTowers());
                }
                assertEquals(intMap.get(color) - 1, towerMap.get(color).getNumOfTowers());
                intMap.put(color, towerMap.get(color).getNumOfTowers());
            }
        }
    }
    @Test
    @DisplayName("Test the normal operation of CheckInfluence method in two scenarios")
    public void checkInfluenceTest() throws Exception {

        IslandTile islandToCheck = new IslandTile();
        //White player has PINK professor
        //Black player has GREEN professor
        //Gray player has YELLOW professor and the tower on the island
        for(Player p : playerBoards.keySet()) {
            if (p.getTowerColor() == TowerColor.WHITE)
                professors.put(Color.PINK, p);
            if(p.getTowerColor() == TowerColor.BLACK)
                professors.put(Color.GREEN, p);
            if(p.getTowerColor() == TowerColor.GRAY) {
                professors.put(Color.YELLOW, p);
                islandToCheck.swapTower(p.getTowerColor());
            }
        }

        //Scenario 1: one YELLOW, one PINK, GRAY must win
        islandToCheck.addStudent(Color.PINK);
        islandToCheck.addStudent(Color.YELLOW);
        assertEquals(TowerColor.GRAY, gb.checkInfluence(islandToCheck));

        //Scenario 2: (previous +) three GREEN students on the island, Black must win
        for(int i = 0; i < 3; i++)
            islandToCheck.addStudent(Color.GREEN);
        assertEquals(TowerColor.BLACK, gb.checkInfluence(islandToCheck));
    }

    @Test
    @DisplayName("Test if exception is thrown properly: scenario 1")
    public void checkInfExc1() {
        //Every player has 0 influence, method has to throw an exception because there is no winner
        IslandTile islandToCheck = new IslandTile();
        RuntimeException e = new RuntimeException();
        assertThrowsExactly(e.getClass(), () -> gb.checkInfluence(islandToCheck));
    }

    @Test
    @DisplayName("Test if exception is thrown properly: scenario 2")
    public void checkInfExc2() throws Exception {
        //Two player has the same influence, method has to throw an exception because there is no winner
        IslandTile islandToCheck = new IslandTile();
        for(Player p : playerBoards.keySet()) {
            if(p.getTowerColor() == TowerColor.BLACK)
                professors.put(Color.GREEN, p);
            if (p.getTowerColor() == TowerColor.WHITE)
                professors.put(Color.PINK, p);
        }
        islandToCheck.addStudent(Color.GREEN);
        islandToCheck.addStudent(Color.PINK);
        RuntimeException e = new RuntimeException();
        assertThrowsExactly(e.getClass(), () -> gb.checkInfluence(islandToCheck));
    }

    @Test
    @DisplayName("Test if exception is thrown properly: scenario 3")
    public void checkInfExc3() throws Exception {
        //A contender has the same influence of the actual owner, method has to throw an exception
        IslandTile islandToCheck = new IslandTile();
        for(Player p : playerBoards.keySet()) {
            if (p.getTowerColor() == TowerColor.BLACK)
                islandToCheck.swapTower(p.getTowerColor());
            if (p.getTowerColor() == TowerColor.WHITE)
                professors.put(Color.PINK, p);
        }
            islandToCheck.addStudent(Color.PINK);
            RuntimeException e = new RuntimeException();
            assertThrowsExactly(e.getClass(), () -> gb.checkInfluence(islandToCheck));
    }

    @Test
    @DisplayName("Test if method addCoin works properly")
    public void addCoinTest() throws Exception {
        DiningRoom destination = new DiningRoom();
        Entrance origin = new Entrance(3);
        Player player = null;
        for(Player p : playerBoards.keySet())
            if(p.getTowerColor() == TowerColor.BLACK) {
                destination = gb.getPlayerBoard(p).getDiningRoom();
                origin = gb.getPlayerBoard(p).getEntrance();
                player = p;
            }
        assertEquals(0, gb.getPlayerBoard(player).getCoin()); //At the beginning 0 coin

        for(int i = 0; i < 3; i++) {
            origin.addStudent(Color.RED);
            gb.move(Color.RED, origin, destination);
        }
        assertEquals(1, gb.getPlayerBoard(player).getCoin()); //1 coin with three red students

        for(int i = 0; i < 6; i++) {
            origin.addStudent(Color.RED);
            gb.move(Color.RED, origin, destination);
        }
        assertEquals(3, gb.getPlayerBoard(player).getCoin()); //3 coin with nine red students

        for(int i = 0; i < 17; i++)
            gb.getPlayerBoard(player).addCoin();
        assertEquals(20, gb.getPlayerBoard(player).getCoin()); //must have 20 coins now

        for(int i = 0; i < 3; i++) {
            origin.addStudent(Color.GREEN);
            gb.move(Color.GREEN, origin, destination);
        }
        assertEquals(20, gb.getPlayerBoard(player).getCoin()); //must have again 20 coins
    }

    @Test
    @DisplayName("Tests if the method addCharacterCard works properly")
    public void addCharacterTest() throws EmptyBagException {

        int oldSize = bg.numRemaining();

        gb.addCharacterCard(Characters.JESTER);

        assertEquals(oldSize, bg.numRemaining() + gb.getCharacters().get(0).getNumStudents()); //Old bag size == new bag size + number of students on the card

        oldSize = bg.numRemaining();
        gb.addCharacterCard(Characters.ROGUE);

        assertEquals(oldSize, bg.numRemaining()); //After adding a non-students-containing card, the size of the bag remains the same

    }

    @Test
    @DisplayName("Tests if the method fillCharacter works properly")
    public void fillCharacterTest() throws EmptyBagException{

        gb.addCharacterCard(Characters.JESTER);
        assertEquals(6, characters.get(0).getNumStudents());
        gb.fillCharacter(characters.get(0));
        assertEquals(7, characters.get(0).getNumStudents());

    }
}

