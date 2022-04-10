package it.polimi.ingsw.network.messages;

/**
 * {@link java.util.Enumeration} for the types of {@link Message}.
 * @author A.G. Gaillet, AlessandroF, AlessandroG
 */
public enum MessageType {
    PING,
    DISCONNECTED,
    S_GAMESTATE,
    S_WIN,
    S_LOBBY,
    S_NUMPLAYERS,
    R_DISCONNECT,
    R_NICKNAME,
    R_GAMESETTINGS,
    S_GAMESETTINGS,
    S_ERROR,
}
