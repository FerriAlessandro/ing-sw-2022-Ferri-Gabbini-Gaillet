package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.EmptyOriginException;
import it.polimi.ingsw.exceptions.FullDestinationException;
import it.polimi.ingsw.model.enumerations.Color;
import it.polimi.ingsw.model.enumerations.TowerColor;
import it.polimi.ingsw.model.enumerations.Wizard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Optional;

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
            CloudTile cl1 = new CloudTile(4);
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
            //TODO: please add this constructor to PlayerBoard and fix related constructor in Entrance (logic for maxNumStud in Entrance should be implemented in Entrance)
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
    void checkForArchipelagoForwardLimited(){
        int originalSize = islands.size();
        for (IslandTile island : islands) {
            island.swapTower(TowerColor.BLACK);
            island.addStudent(Color.GREEN);
            island.addNoEntry();
        }
        islands.get(islands.size() - 2).swapTower(TowerColor.WHITE);
        islands.get(islands.size() - 2).removeNoEntry();
        gb.checkForArchipelago(islands.get(2));
        assertEquals(2, islands.size());
        assertEquals(TowerColor.BLACK, islands.get(0).getTowerColor());
        assertEquals(TowerColor.WHITE, islands.get(1).getTowerColor());
        assertEquals(originalSize - 1, islands.get(0).getNumStudents(Color.GREEN));
        assertEquals(1, islands.get(1).getNumStudents(Color.GREEN));
        assertEquals(originalSize - 1, islands.get(0).getNumTowers());
        assertEquals(1, islands.get(1).getNumTowers());
        for (int i = 0; i < originalSize - 1; i++) {
            assertTrue(islands.get(0).isForbidden());
        }
        assertFalse(islands.get(0).isForbidden());
        assertFalse(islands.get(1).isForbidden());
    }

    @Test
    void checkForArchipelagoBackwardsLimited(){
        int originalSize = islands.size();
        for (IslandTile island : islands) {
            island.swapTower(TowerColor.BLACK);
            island.addStudent(Color.GREEN);
            island.addNoEntry();
        }
        islands.get(2).swapTower(TowerColor.WHITE);
        islands.get(2).removeNoEntry();
        gb.checkForArchipelago(islands.get(islands.size() - 2));
        assertEquals(2, islands.size());
        assertEquals(TowerColor.BLACK, islands.get(1).getTowerColor());
        assertEquals(TowerColor.WHITE, islands.get(0).getTowerColor());
        assertEquals(originalSize - 1, islands.get(1).getNumStudents(Color.GREEN));
        assertEquals(1, islands.get(0).getNumStudents(Color.GREEN));
        assertEquals(originalSize - 1, islands.get(1).getNumTowers());
        assertEquals(1, islands.get(0).getNumTowers());
        for (int i = 0; i < originalSize - 1; i++) {
            assertTrue(islands.get(1).isForbidden());
        }
        assertFalse(islands.get(1).isForbidden());
        assertFalse(islands.get(0).isForbidden());
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
        while (playerBoards.get(p).getTowerZone().getNumOfTowers() > 0){
            playerBoards.get(p).getTowerZone().remove();
        }
        Optional<Player> opt2 = gb.checkEmptyTowerZone();
        assertTrue(opt2.isPresent());
        assertEquals(opt2.get(), p);
    }

    @Test
    @DisplayName("Test move method general behaviour")
    void move(){
        IslandTile is1 = new IslandTile();
        int maxStud = 7;
        Entrance en1 = new Entrance(maxStud);
        //Check behaviour when given origin is empty

        assertThrows(EmptyOriginException.class, () -> gb.move(Color.GREEN, en1, is1));
        en1.addStudent(Color.GREEN);
        //Check behaviour in nominal conditions
        assertEquals(0, en1.getNumStudents(Color.GREEN));
        assertEquals(1, is1.getNumStudents(Color.GREEN));

        for (int i = 0; i < maxStud; i++) {
            is1.addStudent(Color.GREEN);
            try {
                gb.move(Color.GREEN, is1, en1);
            } catch (EmptyOriginException | FullDestinationException e) {
                e.printStackTrace();
                fail();
            }
        }
        //Check behaviour when given destination is full
        assertThrows(FullDestinationException.class, () -> gb.move(Color.GREEN, is1, en1));
    }

    @Test
    @DisplayName("Test that professors are assigned correctly in move")
    void moveCheckProf(){
        assertTrue(gb.getProfessors().keySet().isEmpty());
        Player p1 = playerBoards.keySet().iterator().next();
        Player p2 = playerBoards.keySet().iterator().next();
        playerBoards.get(p1).getDiningRoom().addStudent(Color.GREEN);
        assertEquals(p1, gb.getProfessorOwnerByColor(Color.GREEN));
        playerBoards.get(p2).getDiningRoom().addStudent(Color.GREEN);
        playerBoards.get(p2).getDiningRoom().addStudent(Color.GREEN);
        assertEquals(p2, gb.getProfessorOwnerByColor(Color.GREEN));
    }

    @Test
    void chooseCloud() {
        Player p1 = playerBoards.keySet().iterator().next();
        Entrance en1 = gb.getPlayerBoard(p1).getEntrance();
        CloudTile c1 = clouds.get(0);
        EnumMap<Color, Integer> students = new EnumMap<>(Color.class);
        for (Color color : Color.values()){
            students.put(color, students.get(color) + c1.getNumStudents(color));
        }
        try {
            gb.chooseCloud(c1,p1);
        }catch(Exception e){
            fail();
        }
        for (Color color : students.keySet()){
            assertEquals(en1.getNumStudents(color), students.get(color));
        }

        assertThrows(InvalidParameterException.class, () -> gb.chooseCloud(c1, p1));

        CloudTile c2 = clouds.get(1);
        assertThrows(FullDestinationException.class, () -> gb.chooseCloud(c2, p1));
    }

    @Test
    void fillClouds() {
        //Check normal behaviour
        for (CloudTile cloud: clouds) {
            cloud.removeAllStudents(); //TODO: please add this method to CloudTile
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
        for (TowerColor color : TowerColor.values()){
            gb.swapTowers(isl1, color);
            assertEquals(color, isl1.getTowerColor());
        }
    }
}
