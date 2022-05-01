package it.polimi.ingsw.controller.character_controllers;
import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.model.enumerations.Characters;

/**
 * Factory Class to delegate the CharacterControllers' creation
 * @author Alessandro F.
 * @version 1.0
 */

public class CharacterFactory {

    public static CharacterController create(GameController gameController, Characters characterName){

        CharacterController characterController;
        switch (characterName){

            case MONK: {
                characterController = new MonkController(gameController, Characters.MONK);
                break;
            }
            case FARMER: {
                characterController = new InfluenceController(gameController, Characters.FARMER);
                break;
            }
            case HERALD: {
                characterController = new HeraldController(gameController, Characters.HERALD);
                break;
            }
            case MAGIC_MAILMAN: {
                characterController = new InfluenceController(gameController, Characters.MAGIC_MAILMAN);
                break;
            }
            case GRANDMA_HERB: {
                characterController = new GrandmaHerbController(gameController, Characters.GRANDMA_HERB);
                break;
            }
            case CENTAUR: {
                characterController = new InfluenceController(gameController, Characters.CENTAUR);
                break;
            }
            case JESTER: {
                characterController = new JesterController(gameController, Characters.JESTER);
                break;
            }
            case BARD: {
                characterController = new BardController(gameController, Characters.BARD);
                break;
            }
            case KNIGHT: {
                characterController = new InfluenceController(gameController, Characters.KNIGHT);
                break;
            }
            case SPOILED_PRINCESS: {
                characterController = new SpoiledPrincessController(gameController, Characters.SPOILED_PRINCESS);
                break;
            }
            case MUSHROOM_PICKER: {
                characterController = new MushroomPickerController(gameController, Characters.MUSHROOM_PICKER);
                break;
            }
            case ROGUE: {
                characterController = new RogueController(gameController, Characters.ROGUE);
                break;
            }
            case NONE:{
                characterController = new CharacterController(gameController, Characters.NONE);
                break;
            }

            default: throw new RuntimeException("No corresponding character name");

        }
        return characterController;
    }
}
