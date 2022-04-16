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
    S_INVALID,
    S_ASSISTANT,
    R_ASSISTANT,
    S_CHARACTER,
    R_CHARACTER,
    S_MOVE,
    R_MOVE,
    S_MOTHERNATURE,
    R_MOTHERNATURE,
    S_CLOUD,
    R_CLOUD,
    S_NICKNAME,
    R_NICKNAME,
    S_NUMPLAYERS,
    R_DISCONNECT,
    R_GAMESETTINGS,
    S_GAMESETTINGS,
    S_ERROR,
    R_GAMETYPE,
}
