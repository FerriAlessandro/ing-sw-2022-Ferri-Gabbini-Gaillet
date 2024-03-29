package it.polimi.ingsw.network;

import it.polimi.ingsw.network.messages.*;
import it.polimi.ingsw.view.ViewInterface;

import java.io.IOException;

/**
 * This class masks the network implementation to the {@link ViewInterface} on the client side.
 * @author A.G. Gaillet
 * @version 1.0
 */
public class Adapter {
    private final ViewInterface view;
    private final ClientSocket socket;
    private Message previousMessage = new SMessageInvalid("No previous command");
    private boolean gameEnded = false;

    /**
     * Constructor that allows for overriding of default ip and port values.
     * @param view the {@link ViewInterface} linked to this {@link Adapter}
     * @param ip address of the server you wish to connect to
     * @param port of the server you wish to connect to
     */
    public Adapter(ViewInterface view, String ip, int port){
        this.view = view;
        this.socket = new ClientSocket(ip, port, this);
        socket.start();
    }

    /**
     * Elaborates messages coming from the {@link ClientSocket} and invokes the appropriate method of the linked {@link ViewInterface}
     * @param message received by the {@link ClientSocket}
     */
    public void elaborateMessage(Message message){
        if(!message.getType().equals(MessageType.S_TRYAGAIN) && !message.getType().equals(MessageType.S_INVALID) && !message.getType().equals(MessageType.S_GAMESTATE)){
            previousMessage = message;
        }
        switch (message.getType()) {
            case DISCONNECTED:
                if(!gameEnded)
                    view.showDisconnectionMessage();
                break;

            case S_GAMESTATE:
                view.showBoard((SMessageGameState) message);
                break;

            case S_WIN:
                view.showWinMessage((SMessageWin) message);
                gameEnded = true;
                try{
                    socket.sendMessage(new RMessageDisconnect());
                }catch (IOException ignored){}
                break;

            case S_LOBBY:
                view.showLobby((SMessageLobby) message);
                break;

            case S_INVALID:
                view.showGenericMessage((SMessageInvalid) message);
                break;

            case S_ASSISTANT:
                view.showAssistantChoice((SMessageShowDeck) message);
                break;

            case S_CHARACTER:
                view.showCharacterChoice((SMessageCharacter) message);
                break;

            case S_MOVE:
                view.askMove();
                break;

            case S_MOTHERNATURE:
                view.askMotherNatureMove((SMessageMotherNature) message);
                break;

            case S_CLOUD:
                view.askCloud();
                break;

            case S_NICKNAME:
                view.askNickName();
                break;

            case S_GAMESETTINGS:
                view.askGameSettings();
                break;

            case S_TRYAGAIN:
                view.askAgain();
                elaborateMessage(previousMessage);
                break;

            case S_PLAYER:
                SMessageCurrentPlayer messageCurrentPlayer = (SMessageCurrentPlayer) message;
                view.showCurrentPlayer(messageCurrentPlayer);
                break;

            case S_MONKPRINCESS:
                view.monkPrincessScene((SMessageMonkPrincess) message);
                break;

            case S_ROGUEMUSHROOMPICKER:
                view.rogueMushroomPickerScene((SMessageRogueMushroomPicker) message);
                break;

            case S_GRANDMAHERBHERALD:
                view.grandmaHerbHeraldScene((SMessageGrandmaherbHerald) message);
                break;

            case S_JESTERBARD:
                view.jesterBardScene((SMessageJesterBard) message);
                break;

            case S_LOADGAME:
                view.askUseSavedGame((SMessageLoadGame) message);
                break;

            case S_DISCONNECT:
                view.showDisconnectionMessage();
                break;

            case S_EXPERT:
                view.setExpert((SMessageExpert) message);
                break;

            case S_ASSISTANTSTATUS:
                view.showAssistantStatus((SMessageAssistantStatus) message);
                break;

            default:
                new UnsupportedOperationException().printStackTrace();
                break;
        }
    }

    /**
     * Send the message through the linked {@link ClientSocket}
     * @param message to be sent
     */
    public void sendMessage(Message message){
        //System.out.println("--SENDING MESSAGE TO SERVER--");

        try {
            socket.sendMessage(message);
        } catch (IOException e){
            System.out.println("--UNABLE TO SEND MESSAGE--");
            view.showDisconnectionMessage();
        }
        //System.out.println("--MESSAGE SENT--");
    }

}
