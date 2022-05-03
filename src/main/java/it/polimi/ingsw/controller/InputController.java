package it.polimi.ingsw.controller;

import java.util.ArrayList;
import it.polimi.ingsw.exceptions.FullGameException;
import it.polimi.ingsw.model.enumerations.Phase;
import it.polimi.ingsw.view.VirtualView;
import it.polimi.ingsw.network.messages.*;

/**
 * InputController's task is ensuring that every message from users is correct, both syntactically and
 * in the correct game phase. It has to come also from the correct player.
 * @author AlessandroG
 * @version 1.0
 */
public class InputController {

    private final GameController gameController;
    private Phase gamePhase;

    /**
     * Constructor instants gameController, passing the parameters received by the server.
     * @param numOfPlayers number of players
     * @param isExpert indicates if the game is normal or expert version
     */
    public InputController(int numOfPlayers, boolean isExpert){
        gameController = new GameController(numOfPlayers, isExpert);
    }

    public void elaborateMessage(Message mess){
        verifyMessage(mess);
    }

    public void addPlayer(String nick, VirtualView vv) throws FullGameException {}

    public void playerDisconnected(String nick){}

    public ArrayList<String> getNicknames(){return new ArrayList<>();}

    public GameController getGameController() { return gameController; }

    /**
     * The method calls the proper messageTypeCheck.
     * @param message is the message that InputController has to check.
     */
    private void verifyMessage(Message message) {
        //TODO synchronization on Message (?)
        gamePhase = gameController.getGamePhase();

        switch(message.getType()) {

            case R_MOVE:
                moveCheck(message);
                break;

            case R_CLOUD:
                cloudCheck(message);
                break;

            case R_ASSISTANT:
                assistantCheck(message);
                break;

            case R_MOTHERNATURE:
                motherNatureCheck(message);
                break;

            case R_CHARACTER:
                characterCheck(message);
                break;

            case R_NICKNAME:
                nicknameCheck(message);
                break;

            default:
                new RuntimeException().printStackTrace();
                break;
        }
    }


    /**
     * Utility method just to avoid coding repetitions
     * @param message will be elaborated or asked again
     * @param isValid indicates if the message is valid
     */
    private void validateMessage(boolean isValid, Message message) {
        if(isValid)
            gameController.elaborateMessage(message);
        else
            gameController.askAgain(message);
    }

    private void moveCheck(Message message) {
        int destination = ((RMessageMove)message).getDestination();
        boolean isValid = true;

        if(gamePhase != Phase.MOVE_STUDENTS)
            new RuntimeException().printStackTrace();

        if(((RMessageMove)message).getChosenColor() == null)
            isValid = false;

        if(destination < 0 || destination > 12)
            isValid = false;

        validateMessage(isValid, message);
    }

    private void cloudCheck(Message message) {
        boolean isValid = true;
        int cloud = ((RMessageCloud)message).getCloudIndex();

        if(gamePhase != Phase.CHOOSE_CLOUD)
            new RuntimeException().printStackTrace();

        if(cloud > 3 || cloud < 1)
            isValid = false;

        validateMessage(isValid, message);
    }

    private void assistantCheck(Message message) {
        boolean isValid = true;

        if(gamePhase != Phase.CHOOSE_ASSISTANT_CARD)
            new RuntimeException().printStackTrace();

        if(((RMessageAssistant)message).getPlayedAssistant() == null)
            isValid = false;

        validateMessage(isValid, message);
    }

    private void motherNatureCheck(Message message) {
        int islandIdx = ((RMessageMotherNature)message).getIslandIndex();
        boolean isValid = true;

        if(gamePhase != Phase.MOVE_MOTHERNATURE)
            new RuntimeException().printStackTrace();

        if(islandIdx < 1 || islandIdx > 12)
            isValid = false;

        validateMessage(isValid, message);
    }

    private void characterCheck(Message message) {
        boolean isValid = ((RMessageCharacter)message).getCharacter() != null;

        if(gamePhase != Phase.CHOOSE_CHARACTER_CARD)
            new RuntimeException().printStackTrace();

        validateMessage(isValid, message);
    }

    private void nicknameCheck(Message message) {
        RMessageNickname mess = (RMessageNickname) message;
        String nickname = mess.getNickname();
        boolean isValid = ((RMessageNickname)message).getNickname() == null;

        validateMessage(isValid, message);
    }

}
