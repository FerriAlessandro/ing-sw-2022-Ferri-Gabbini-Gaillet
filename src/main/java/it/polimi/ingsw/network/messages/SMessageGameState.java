package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.model.enumerations.Color;
import it.polimi.ingsw.model.enumerations.TowerColor;

import java.util.Map;

/**
 * {@link Message} used by the server to notify a change in the state of the model.
 * @author A.G. Gaillet
 * @version 1.0
 */
public class SMessageGameState extends SMessage {
    public Map<String, Map<Color, Integer>> studEntrance;
    public Map<String, Map<Color, Integer>> studDining;
    public Map<Integer, Map<Color, Integer>> studIslands;
    public Map<Integer, Integer> numTowersIslands;
    public Map<Integer, TowerColor> colorTowerIslands;
    public Map<Integer, Integer> forbiddenTokens;
    public Map<Integer, Map<Color, Integer>> studClouds;
    public Map<Color, String> professors;
    public int motherNaturePosition;
    public SMessageGameState(Map<String, Map<Color, Integer>> studEntrance, Map<String, Map<Color, Integer>> studDining,
                             Map<Integer, Map<Color, Integer>> studIslands, Map<Integer, Integer> numTowersIslands,
                             Map<Integer, TowerColor> colorTowerIslands, Map<Integer, Integer> forbiddenTokens,
                             Map<Integer, Map<Color, Integer>> studClouds, Map<Color, String> professors,
                             int motherNaturePosition){

        super(MessageType.S_GAMESTATE);

        this.studEntrance = studEntrance;
        this.studDining = studDining;
        this.studIslands = studIslands;
        this.numTowersIslands = numTowersIslands;
        this.colorTowerIslands = colorTowerIslands;
        this.forbiddenTokens = forbiddenTokens;
        this.motherNaturePosition = motherNaturePosition;
        this.studClouds = studClouds;
        this.professors = professors;
    }

}
