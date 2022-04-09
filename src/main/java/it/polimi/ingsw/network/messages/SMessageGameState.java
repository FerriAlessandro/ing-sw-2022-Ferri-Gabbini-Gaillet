package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.model.enumerations.Color;

import java.util.Map;

/**
 * {@link Message} used by the server to notify a change in the state of the model.
 * @author A.G. Gaillet
 * @version 1.0
 */
public class SMessageGameState extends Message {
    public Map<String, Map<Color, Integer>> studEntrance;
    public Map<String, Map<Color, Integer>> studDining;
    public Map<Integer, Map<Color, Integer>> studIslands;
    public Map<Integer, Integer> towerIslands;
    public Map<Integer, Integer> forbiddenTokens;
    public Map<Integer, Map<Color, Integer>> studClouds;
    public int motherNaturePosition;

    public SMessageGameState(Map<String, Map<Color, Integer>> studEntrance, Map<String, Map<Color, Integer>> studDining,
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
