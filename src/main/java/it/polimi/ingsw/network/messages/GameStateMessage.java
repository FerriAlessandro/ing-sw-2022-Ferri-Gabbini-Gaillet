package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.model.enumerations.Color;

import java.util.ArrayList;
import java.util.Map;

public class GameStateMessage extends Message {
    public Map<String, Map<Color, Integer>> studEntrance;
    public Map<String, Map<Color, Integer>> studDining;
    public Map<Integer, Map<Color, Integer>> studIslands;
    public Map<Integer, Integer> towerIslands;
    public Map<Integer, Integer> forbiddenTokens;
    public Map<Integer, Map<Color, Integer>> studClouds;
    public int motherNaturePosition;

    public GameStateMessage(Map<String, Map<Color, Integer>> studEntrance, Map<String, Map<Color, Integer>> studDining,
                            Map<Integer, Map<Color, Integer>> studIslands, Map<Integer, Integer> towerIslands,
                            Map<Integer, Integer> forbiddenTokens, Map<Integer, Map<Color, Integer>> studClouds, int motherNaturePosition){

        this.studEntrance = studEntrance;
        this.studDining = studDining;
        this.studIslands = studIslands;
        this.towerIslands = towerIslands;
        this.forbiddenTokens = forbiddenTokens;
        this.motherNaturePosition = motherNaturePosition;
        this.studClouds = studClouds;
        this.type = MessageType.S_GAMESTATE;
    }

}
