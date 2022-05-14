package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.network.Adapter;
import it.polimi.ingsw.network.messages.*;
import it.polimi.ingsw.view.ViewInterface;
import javafx.scene.Scene;

import java.util.HashMap;

/**
 * This class is the real Graphical User Interface.
 * @author AlessandroG
 * @version 1.0
 */
public class Gui implements ViewInterface {

    private Adapter adapter;
    private HashMap<String, Scene> sceneMap = new HashMap<>();
    private Scene currentScene;
    private Game game;

    public void Gui() {
        //String ip = "localhost";
        //int port = 2351;
        //adapter = new Adapter(this, ip, port);
    }

    public void changeScene(Scene newScene) {
        currentScene = newScene;
        //stage.setScene(currentScene);
       // stage.show();
    }

    @Override
    public void askNickName() {

    }

    @Override
    public void askGameSettings() {

    }

    @Override
    public void askMotherNatureMove() {

    }

    @Override
    public void showLobby(SMessageLobby message) {

    }

    @Override
    public void showDisconnectionMessage() {

    }

    @Override
    public void showBoard(SMessageGameState gameState) {

    }

    @Override
    public void showWinMessage(SMessageWin message) {

    }

    @Override
    public void showGenericMessage(SMessageInvalid message) {

    }

    @Override
    public void showAssistantChoice(SMessageShowDeck message) {

    }

    @Override
    public void showCharacterChoice(SMessageCharacter messageCharacter) {

    }

    @Override
    public void askMove() {

    }

    @Override
    public void askCloud() {

    }

    @Override
    public void askAgain() {

    }

    @Override
    public void showCurrentPlayer(SMessageCurrentPlayer messageCurrentPlayer) {

    }

    @Override
    public String getNickName() {
        return null;
    }

    @Override
    public void askCharacterMove(SMessage message) {

    }

    @Override
    public void grandmaHerbHeraldScene(SMessageGrandmaherbHerald message) {

    }

    @Override
    public void monkPrincessScene(SMessageMonkPrincess message) {

    }

    @Override
    public void rogueMushroomPickerScene(SMessageRogueMushroomPicker message) {

    }

    @Override
    public void jesterBardScene(SMessageJesterBard message) {

    }

    @Override
    public void askUseSavedGame() {

    }
}
