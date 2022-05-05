package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.enumerations.AssistantCard;
import it.polimi.ingsw.model.enumerations.Characters;
import it.polimi.ingsw.model.enumerations.Color;
import it.polimi.ingsw.model.enumerations.Phase;
import it.polimi.ingsw.network.messages.*;
import it.polimi.ingsw.view.VirtualView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class InputControllerTest {
    MockInputController inputController = new MockInputController(3, true);
    Message message;

    @BeforeEach
    public void setup() {

    }

    @Test
    @DisplayName("Test moveCheck method")
    public void moveCheckTest() {
        inputController.getGameController().setGamePhase(Phase.MOVE_STUDENTS);
        message = new RMessageMove(null, 3, "testPlayer");
        inputController.elaborateMessage(message);
        assertFalse(inputController.isMessageValid());
        message = new RMessageMove(Color.RED, 13, "testPlayer");
        inputController.elaborateMessage(message);
        assertFalse(inputController.isMessageValid());
        message = new RMessageMove(Color.RED, 3, "testPlayer");
        inputController.elaborateMessage(message);
        assertTrue(inputController.isMessageValid());
    }

    @Test
    @DisplayName("Test cloudCheck method")
    public void cloudCheckTest() {
        inputController.getGameController().setGamePhase(Phase.CHOOSE_CLOUD);
        message = new RMessageCloud(0, "nickTest");
        inputController.elaborateMessage(message);
        assertFalse(inputController.isMessageValid());
        message = new RMessageCloud(1, "nickTest");
        inputController.elaborateMessage(message);
        assertTrue(inputController.isMessageValid());
    }

    @Test
    @DisplayName("Test assistantCheck method")
    public void assistantCheckTest() {
        inputController.getGameController().setGamePhase(Phase.CHOOSE_ASSISTANT_CARD);
        message = new RMessageAssistant(null, "nickTest");
        inputController.elaborateMessage(message);
        assertFalse(inputController.isMessageValid());
        message = new RMessageAssistant(AssistantCard.TURTLE, "nickTest");
        inputController.elaborateMessage(message);
        assertTrue(inputController.isMessageValid());
    }

    @Test
    @DisplayName("Test motherNatureCheck method")
    public void motherNatureCheckTest() {
        inputController.getGameController().setGamePhase(Phase.MOVE_MOTHERNATURE);
        message = new RMessageMotherNature(0, "nickTest");
        inputController.elaborateMessage(message);
        assertFalse(inputController.isMessageValid());
        message = new RMessageMotherNature(3, "nickTest");
        inputController.elaborateMessage(message);
        assertTrue(inputController.isMessageValid());
    }

    @Test
    @DisplayName("Test characterCheck method")
    public void characterCheckTest() {
        inputController.getGameController().setGamePhase(Phase.CHOOSE_CHARACTER_CARD_1);
        message = new RMessageCharacter(null, "nickTest");
        inputController.elaborateMessage(message);
        assertFalse(inputController.isMessageValid());
        message = new RMessageCharacter(Characters.CENTAUR, "nickTest");
        inputController.elaborateMessage(message);
        assertTrue(inputController.isMessageValid());
    }

    @Test
    @DisplayName("Test monkPrincessCheck method")
    public void monkPrincessCheckTest() {
        inputController.getGameController().setGamePhase(Phase.CHOOSE_CHARACTER_CARD_1);
        message = new RMessageMonkPrincess(null, "nickTest", 4, Color.RED);
        inputController.elaborateMessage(message);
        assertFalse(inputController.isMessageValid());
        message = new RMessageMonkPrincess(Characters.MONK, "nickTest", 4, null);
        inputController.elaborateMessage(message);
        assertFalse(inputController.isMessageValid());
        message = new RMessageMonkPrincess(Characters.CENTAUR, "nickTest", 4, Color.RED);
        inputController.elaborateMessage(message);
        assertFalse(inputController.isMessageValid());
        message = new RMessageMonkPrincess(Characters.MONK, "nickTest", 0, Color.RED);
        inputController.elaborateMessage(message);
        assertFalse(inputController.isMessageValid());
        message = new RMessageMonkPrincess(Characters.MONK, "nickTest", 4, Color.RED);
        inputController.elaborateMessage(message);
        assertTrue(inputController.isMessageValid());
        message = new RMessageMonkPrincess(Characters.SPOILED_PRINCESS, "nickTest", 0, Color.RED);
        inputController.elaborateMessage(message);
        assertTrue(inputController.isMessageValid());
    }

    @Test
    @DisplayName("Test jesterBardCheck method")
    public void jesterBardCheckTest() {
        ArrayList<Color> origin = new ArrayList<>();
        ArrayList<Color> entrance = new ArrayList<>();
        inputController.getGameController().setGamePhase(Phase.CHOOSE_CHARACTER_CARD_2);
        message = new RMessageJesterBard(null, "nickTest", origin, entrance);
        inputController.elaborateMessage(message);
        assertFalse(inputController.isMessageValid());
        message = new RMessageJesterBard(Characters.CENTAUR, "nickTest", origin, entrance);
        inputController.elaborateMessage(message);
        assertFalse(inputController.isMessageValid());
        message = new RMessageJesterBard(Characters.JESTER, "nickTest", origin, entrance);
        inputController.elaborateMessage(message);
        assertTrue(inputController.isMessageValid());
    }

    @Test
    @DisplayName("Test grandmaherbHeraldCheck method")
    public void grandmaherbHeraldCheckTest() {
        inputController.getGameController().setGamePhase(Phase.CHOOSE_CHARACTER_CARD_2);
        message = new RMessageGrandmaherbHerald(null, "nickTest", 3);
        inputController.elaborateMessage(message);
        assertFalse(inputController.isMessageValid());
        message = new RMessageGrandmaherbHerald(Characters.CENTAUR, "nickTest", 3);
        inputController.elaborateMessage(message);
        assertFalse(inputController.isMessageValid());
        message = new RMessageGrandmaherbHerald(Characters.HERALD, "nickTest", 15);
        inputController.elaborateMessage(message);
        assertFalse(inputController.isMessageValid());
        message = new RMessageGrandmaherbHerald(Characters.GRANDMA_HERB, "nickTest", 3);
        inputController.elaborateMessage(message);
        assertTrue(inputController.isMessageValid());
    }

    @Test
    @DisplayName("Test rogueMushroomPickerCheck method")
    public void rogueMushroomPickerCheckTest() {
        inputController.getGameController().setGamePhase(Phase.CHOOSE_CHARACTER_CARD_3);
        message = new RMessageRogueMushroomPicker(null, "nickTest", Color.RED);
        inputController.elaborateMessage(message);
        assertFalse(inputController.isMessageValid());
        message = new RMessageRogueMushroomPicker(Characters.ROGUE, "nickTest", null);
        inputController.elaborateMessage(message);
        assertFalse(inputController.isMessageValid());
        message = new RMessageRogueMushroomPicker(Characters.CENTAUR, "nickTest", Color.RED);
        inputController.elaborateMessage(message);
        assertFalse(inputController.isMessageValid());
        message = new RMessageRogueMushroomPicker(Characters.ROGUE, "nickTest", Color.RED);
        inputController.elaborateMessage(message);
        assertTrue(inputController.isMessageValid());
    }

    @Test
    @DisplayName("Test nicknameCheck method")
    public void nicknameCheckTest() {
        message = new RMessageNickname(null);
        inputController.elaborateMessage(message);
        assertFalse(inputController.isMessageValid());
        message = new RMessageNickname("nickTest");
        inputController.elaborateMessage(message);
        assertTrue(inputController.isMessageValid());
    }

    //TODO test when VirtualView is not null
    @Test
    @DisplayName("Test addPlayer method")
    public void addPlayerTest(){
        /*Socket socket = new Socket("test", 0);
        //ClientHandler clientHandler = new ClientHandler(socket, inputController);
        //VirtualView virtualView = new VirtualView(clientHandler);
        assertThrowsExactly(RuntimeException.class, () -> inputController.addPlayer(null, virtualView));*/
        assertThrowsExactly(RuntimeException.class, () -> inputController.addPlayer("nickTest",null));
        //inputController.addPlayer(null, virtualView);
    }

    @Test
    @DisplayName("Test playerDisconnected method")
    public void playerDisconnected() {
        assertThrowsExactly(RuntimeException.class, () -> inputController.playerDisconnected(null));
        inputController.playerDisconnected("nickTest");
        assertTrue(inputController.isMessageValid());
    }
}





/**
 * Utility class for testing. Allows to set GameController's gamePhase.
 */
class MockGameController extends GameController {
    //phase is used to set a specific phase, indispensable for testing
    private Phase phase;
    //messageValidated used to see if elaborateMessage is called properly
    private boolean messageValidated = false;

    public MockGameController(int numOfPlayers, boolean isExpert) {
        super(numOfPlayers, isExpert);

    }
    public Phase getGamePhase() {
        return phase;
    }
    public void setGamePhase(Phase phase) {
        this.phase = phase;
    }
    public boolean isMessageValidated() {
        return this.messageValidated;
    }
    public void elaborateMessage(Message mess) {
        messageValidated = true;
    }

    public void askAgain() {
        messageValidated = false;
    }

    public void addPlayer(String nickname, VirtualView virtualView) {
        messageValidated = true;
    }

    public void playerDisconnected(String nickname) {
        messageValidated = true;
    }
}

/**
 * Utility class for testing. It works with the help of the other MockClass MockGameController.
 */
class MockInputController extends InputController {
    private final MockGameController gameController;
    public MockInputController(int numOfPlayers, boolean isExpert) {
        super(numOfPlayers, isExpert);
        gameController = new MockGameController(numOfPlayers, isExpert);
    }

    public MockGameController getGameController() {
        return gameController;
    }

    public boolean isMessageValid() {
        return gameController.isMessageValidated();
    }


}

