package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.CloudNotFullException;
import it.polimi.ingsw.exceptions.FullDestinationException;
import it.polimi.ingsw.exceptions.TowerWinException;
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
    HashMap<Player, PlayerBoard> playerBoards = new HashMap<>();
    EnumMap<Color, Player> professors = new EnumMap<>(Color.class);
    int numPlayers = 3;

    MotherNature tmn;

    @BeforeEach
    void setUp() {
        IslandTile isl1 = new IslandTile();
        islands.add(isl1);
        mn = new MotherNature(islands);
        tmn = new MotherNature(islands);
        for (int i = 1; i < 12; i++) {
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
            Player pl1 = new Player(ad, "test", false, false, t1.get(i % t1.size()));
            PlayerBoard pb1 = new PlayerBoard(numPlayers);
            playerBoards.put(pl1, pb1);
        }

        gb = new GameBoard(clouds, islands, mn, playerBoards, professors);
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
        islands.get(islands.size() - 2).swapTower(TowerColor.WHITE);
        islands.get(islands.size() - 2).removeNoEntry();
        gb.checkForArchipelago(islands.get(2));
        assertEquals(originalSize - 2, islands.size());
        assertEquals(TowerColor.BLACK, islands.get(1).getTowerColor());
        assertEquals(TowerColor.WHITE, islands.get(islands.size() - 2).getTowerColor());
        assertEquals(3, islands.get(1).getNumStudents(Color.GREEN));
        assertEquals(1, islands.get(0).getNumStudents(Color.GREEN));
        assertEquals(3, islands.get(1).getNumTowers());
        assertEquals(1, islands.get(0).getNumTowers());
        assertEquals(3, islands.get(1).getNumOfNoEntryTiles());
        assertTrue(islands.get(0).isForbidden());
        assertTrue(islands.get(2).isForbidden());
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
        assertThrows(FullDestinationException.class, () -> gb.move(Color.GREEN, is1, en1));
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
    void fillClouds() throws FullDestinationException {
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
            towerMap.put(player.getTowerColor(), playerBoards.get(player).towerZone);
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
        Player black = null;
        Player gray = null;
        //White player has PINK professor
        //Black player has GREEN professor
        //Gray player has YELLOW professor and the tower on the island
        for(Player p : playerBoards.keySet()) {
            if (p.getTowerColor() == TowerColor.WHITE)
                professors.put(Color.PINK, p);
            if(p.getTowerColor() == TowerColor.BLACK) {
                professors.put(Color.GREEN, p);
                black = p;
            }
            if(p.getTowerColor() == TowerColor.GRAY) {
                professors.put(Color.YELLOW, p);
                islandToCheck.swapTower(p.getTowerColor());
                gray = p;
            }
        }

        //Scenario 1: one YELLOW, one PINK, GRAY must win
        islandToCheck.addStudent(Color.PINK);
        islandToCheck.addStudent(Color.YELLOW);
        assertEquals(gray.getTowerColor(), gb.checkInfluence(islandToCheck));

        //Scenario 2: (previous +) three GREEN students on the island, Black must win
        for(int i = 0; i < 3; i++)
            islandToCheck.addStudent(Color.GREEN);
        assertEquals(black.getTowerColor(), gb.checkInfluence(islandToCheck));
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

}

