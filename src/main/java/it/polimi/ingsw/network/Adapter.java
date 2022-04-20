package it.polimi.ingsw.network;

import it.polimi.ingsw.network.messages.*;
import it.polimi.ingsw.view.ViewInterface;

import java.io.IOException;
import java.security.InvalidParameterException;

/**
 * This class masks the network implementation to the {@link ViewInterface} on the client side.
 * @author A.G. Gaillet
 * @version 1.0
 */
public class Adapter {
    ViewInterface view;
    ClientSocket socket;
    Message previousMessage = new SMessage(MessageType.S_ERROR);
    String currentPlayer;

    /**
     * Constructor used for mock classes that extend {@link Adapter}. To be used for testing.
     */
    public Adapter(){}

    /**
     * Default constructor. Used in testing.
     * @param view the {@link ViewInterface} linked to this {@link Adapter}
     */
    public Adapter(ViewInterface view){
        this.view = view;
        socket = new ClientSocket(this);
        socket.start();
    }

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
        if(!message.getType().equals(MessageType.S_TRYAGAIN)){
            previousMessage = message;
        }
        switch (message.getType()) {
            case DISCONNECTED:
                view.showDisconnectionMessage();
                break;
            case S_GAMESTATE:
                view.showBoard((SMessageGameState) message);
                break;
            case S_WIN:
                view.showWinMessage((SMessageWin) message);
                break;
            case S_LOBBY:
                view.showLobby((SMessageLobby) message);
                break;
            case S_INVALID:
                view.showGenericMessage((SMessageInvalid) message);
                break;
            case S_ASSISTANT:
                view.showAssistantChoice();
                break;
            case S_CHARACTER:
                view.showCharacterChoice();
                break;
            case S_MOVE:
                view.askMove();
                break;
            case S_MOTHERNATURE:
                view.askMotherNatureMove();
                break;
            case S_CLOUD:
                view.askCloud();
                break;
            case S_NICKNAME:
                if(view.getNickName() == null) view.askNickName();
                else sendMessage(new RMessageNickname(view.getNickName()));
                break;
            case S_GAMESETTINGS:
                view.askGameSettings();
                break;
            case S_ERROR:
                view.showGenericMessage(new SMessageInvalid("A generic error occurred"));
                break;
            case S_TRYAGAIN:
                view.askAgain();
                elaborateMessage(previousMessage);
                break;
            case S_PLAYER:
                currentPlayer = ((SMessageCurrentPlayer) message).nickname;
                break;
            default:
                new InvalidParameterException().printStackTrace();
                break;
        }
    }

    /**
     * Send the message through the linked {@link ClientSocket}
     * @param message to be sent
     */
    public void sendMessage(Message message){
        if(!currentPlayer.equals(view.getNickName())){
            view.showGenericMessage(new SMessageInvalid("Not your turn"));
            return;
        }

        try {
            socket.sendMessage(message);
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
