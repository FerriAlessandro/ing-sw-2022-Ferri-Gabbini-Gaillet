package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.enumerations.Color;
import it.polimi.ingsw.model.enumerations.Phase;
import it.polimi.ingsw.network.messages.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InputControllerTest {
    MockInputController inputController;
    MockGameController gameController; //just to avoid code repetitions.
    RMessage message;

    @BeforeEach
    public void setup() {
        inputController = new MockInputController(3, true);
        gameController = inputController.getGameController();
    }

    @Test
    @DisplayName("Test moveCheck method")
    public void test1() {
        message = new RMessageMove(Color.RED, 3, "testPlayer");
        assertDoesNotThrow(() -> inputController.elaborateMessage(message)); //it's correct because the stackTrace is properly printed. Method doesn't throw the Exception.
        gameController.setGamePhase(Phase.MOVE_STUDENTS);

    }
}

/**
 * Utility class for testing. Allows to set GameController's gamePhase.
 */
class MockGameController extends GameController {
    Phase phase;
    public MockGameController(int numOfPlayers, boolean isExpert) {
        super(numOfPlayers, isExpert);
    }
    public Phase getGamePhase() {
        return phase;
    }

    public void setGamePhase(Phase phase) {
        this.phase = phase;
    }
}

/**
 * Utility class for testing. It works with the help of the other MockClass.
 */
class MockInputController extends InputController {
    private final MockGameController gameController;
    public MockInputController(int numOfPlayers, boolean isExpert) {
        super(numOfPlayers, isExpert);
        gameController = new MockGameController(numOfPlayers, isExpert);
    }
    public MockGameController getGameController() { return gameController; }
}