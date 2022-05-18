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
    public final Map<String, Map<Color, Integer>> studEntrance;
    public final Map<String, Map<Color, Integer>> studDining;
    public final Map<String, Integer> towerNumber;
    public final Map<String, TowerColor> towerColor;
    public final Map<Integer, Map<Color, Integer>> studIslands;
    public final Map<Integer, Integer> numTowersIslands;
    public final Map<Integer, TowerColor> colorTowerIslands;
    public final Map<Integer, Integer> forbiddenTokens;
    public final Map<Integer, Map<Color, Integer>> studClouds;
    public final Map<Color, String> professors;
    public final int motherNaturePosition;

    public final Map<String, Integer> coins;

    /**
     * Constructor.
     * @param studEntrance {@link Map} of nicknames and entrances (maps of Color and integer)
     * @param studDining {@link Map} of nicknames and dining rooms (maps of Color and integer)
     * @param towerNumber {@link Map} of nicknames and number of remaining towers (integer)
     * @param towerColor {@link Map} of nicknames and {@link TowerColor}
     * @param studIslands {@link Map} of island id and students on the island (maps of Color and integer)
     * @param numTowersIslands {@link Map} of island id and number of towers on the island
     * @param colorTowerIslands {@link Map} of island id and {@link TowerColor} of the tower present on the island
     * @param forbiddenTokens {@link Map} of island id and number of "forbidden-tokens" present
     * @param studClouds {@link Map} of cloud id and present students (maps of Color and integer)
     * @param professors {@link Map} of Color and nickname of owner
     * @param motherNaturePosition int representing the id of the island where mother nature is
     * @param coins {@link Map} of nicknames and number of coins owned (integer)
     */
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
