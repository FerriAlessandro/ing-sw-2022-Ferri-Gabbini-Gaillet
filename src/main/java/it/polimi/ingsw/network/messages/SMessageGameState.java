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
    public Map<String, Integer> towerNumber;
    public Map<String, TowerColor> towerColor;
    public Map<Integer, Map<Color, Integer>> studIslands;
    public Map<Integer, Integer> numTowersIslands;
    public Map<Integer, TowerColor> colorTowerIslands;
    public Map<Integer, Integer> forbiddenTokens;
    public Map<Integer, Map<Color, Integer>> studClouds;
    public Map<Color, String> professors;
    public int motherNaturePosition;

    public Map<String, Integer> coins;

    public SMessageGameState(Map<String, Map<Color, Integer>> studEntrance, Map<String, Map<Color, Integer>> studDining,
                             Map<String, Integer> towerNumber, Map<String, TowerColor> towerColor,
                             Map<Integer, Map<Color, Integer>> studIslands, Map<Integer, Integer> numTowersIslands,
                             Map<Integer, TowerColor> colorTowerIslands, Map<Integer, Integer> forbiddenTokens,
                             Map<Integer, Map<Color, Integer>> studClouds, Map<Color, String> professors,
                             int motherNaturePosition, Map<String, Integer> coins){

        super(MessageType.S_GAMESTATE);

        this.studEntrance = studEntrance;
        this.studDining = studDining;
        this.towerNumber = towerNumber;
        this.towerColor = towerColor;
        this.studIslands = studIslands;
        this.numTowersIslands = numTowersIslands;
        this.colorTowerIslands = colorTowerIslands;
        this.forbiddenTokens = forbiddenTokens;
        this.motherNaturePosition = motherNaturePosition;
        this.studClouds = studClouds;
        this.professors = professors;
        this.coins = coins;
    }

}
