/*package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.character_controllers.CharacterController;
import it.polimi.ingsw.exceptions.FullDestinationException;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.enumerations.Characters;
import it.polimi.ingsw.model.enumerations.Color;
import it.polimi.ingsw.model.enumerations.Phase;
import it.polimi.ingsw.view.VirtualView;

import java.util.*;

public class MockGameController extends GameController{

    private Game game;
    public Phase gamePhase;
    private CharacterController characterController;
    private final Map<String, VirtualView> playersView = new HashMap<>();
    private boolean isLastRound;
    private int numOfMoves = 0;
    public boolean hasPlayedCharacter;
    private final ArrayList<String> nickNames = new ArrayList<>();

    public MockGameController() {
        super(3, true);
        nickNames.add("Ale");
        nickNames.add("Gabbo");
        nickNames.add("Angelo");
        game = new Game(nickNames);
        gamePhase = Phase.CHOOSE_CHARACTER_CARD_1;
        isLastRound = false;
        hasPlayedCharacter = false;
        for(int i=0;i<5;i++) {
            game.getGameBoard().getPlayerBoard(game.getPlayers().get(0)).addCoin();
            try {
                for(Player p : game.getPlayers()) {
                    game.getGameBoard().getPlayerBoard(p).getDiningRoom().addStudent(Color.RED);
                    game.getGameBoard().getPlayerBoard(p).getDiningRoom().addStudent(Color.GREEN);
                    game.getGameBoard().getPlayerBoard(p).getDiningRoom().addStudent(Color.YELLOW);
                    game.getGameBoard().getPlayerBoard(p).getDiningRoom().addStudent(Color.BLUE);
                    game.getGameBoard().getPlayerBoard(p).getDiningRoom().addStudent(Color.PINK);
                }
            }catch(FullDestinationException ignored){}
        }

    }

    @Override
    public VirtualView getVirtualView(String nickname){
       return null;
    }

    @Override
    public void sendErrorMessage(String nickname, String msg){

    }
}
*/