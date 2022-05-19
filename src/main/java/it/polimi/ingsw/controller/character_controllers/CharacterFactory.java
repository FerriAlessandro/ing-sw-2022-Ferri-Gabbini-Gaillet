package it.polimi.ingsw.controller.character_controllers;
import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.model.enumerations.Characters;

/**
 * Factory Class to delegate the CharacterControllers' creation.
 * @author Alessandro F.
 * @version 1.0
 * @see Characters
 * @see CharacterController
 */
public class CharacterFactory {

    /**
     * Factory method.
     * @param gameController to be linked to the created {@link GameController}
     * @param characterName of the character to be implemented
     * @return the corresponding {@link CharacterController}
     */
    public static CharacterController create(GameController gameController, Characters characterName){

        CharacterController characterController;
        switch (characterName) {
            case MONK -> characterController = new MonkController(gameController, Characters.MONK);
            case FARMER -> characterController = new InfluenceController(gameController, Characters.FARMER);
            case HERALD -> characterController = new HeraldController(gameController, Characters.HERALD);
            case MAGIC_MAILMAN -> characterController = new InfluenceController(gameController, Characters.MAGIC_MAILMAN);
            case GRANDMA_HERB -> characterController = new GrandmaHerbController(gameController, Characters.GRANDMA_HERB);
            case CENTAUR -> characterController = new InfluenceController(gameController, Characters.CENTAUR);
            case JESTER -> characterController = new JesterController(gameController, Characters.JESTER);
            case BARD -> characterController = new BardController(gameController, Characters.BARD);
            case KNIGHT -> characterController = new InfluenceController(gameController, Characters.KNIGHT);
            case SPOILED_PRINCESS -> characterController = new SpoiledPrincessController(gameController, Characters.SPOILED_PRINCESS);
            case MUSHROOM_PICKER -> characterController = new MushroomPickerController(gameController, Characters.MUSHROOM_PICKER);
            case ROGUE -> characterController = new RogueController(gameController, Characters.ROGUE);
            case NONE -> characterController = new CharacterController(gameController, Characters.NONE);
            default -> throw new RuntimeException("No corresponding character name");
        }
        return characterController;
    }
}
