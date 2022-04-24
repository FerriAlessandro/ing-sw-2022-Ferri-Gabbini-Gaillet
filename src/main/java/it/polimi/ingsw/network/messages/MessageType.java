package it.polimi.ingsw.network.messages;

/**
 * {@link java.util.Enumeration} for the types of {@link Message}.
 * @author A.G. Gaillet, Alessandro F., AlessandroG
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
    R_GAMETYPE,
    R_DISCONNECT,
    R_GAMESETTINGS,
    S_GAMESETTINGS,
    S_ERROR,
    S_TRYAGAIN,
    S_PLAYER,
    S_MONKPRINCESSROGUE,
    R_MONKPRINCESSROGUE,
    S_JESTERBARD,
    R_JESTERBARD,
    S_GRANDMAHERB,
    R_GRANDMAHERB
}
