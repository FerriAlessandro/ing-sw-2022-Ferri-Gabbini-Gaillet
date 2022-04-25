package it.polimi.ingsw.model.enumerations;

public enum Characters {

    HERALD("Choose an Island and resolve the Island as if Mother Nature had ended her movement there." +
            "Mother Nature will still move and the Island where she ends her movement will also be resolved"),
    KNIGHT("During the influence calculation this turn, you count as having 2 more influence"),
    CENTAUR("When resolving a Conquering on an Island, Towers do not count towards influence"),
    FARMER("During this turn, you take control of any number of Professors even if you have the same " +
            "number of Students as the player who currently controls them"),
    MUSHROOM_PICKER("Choose a color of Student: during the influence calculation this turn, that color adds no influence"),
    JESTER("You may take up to 3 Students from this card and replace them with the same number of Students" +
            "from your Entrance"),
    ROGUE("Choose a type of Student: every player (including yourself) must return 3 Students of that type" +
            "from their Dining Room to the bag. If any player has fewer than 3 Students of that type, return as " +
            "many Students as they have"),
    BARD("You may exchange up to 2 Students between your Entrance and your Dining Room"),
    MONK("Take 1 Student form this card and place it on an Island of your choice. Then, draw a new Student" +
            "from the Bag and place it on this card"),
    GRANDMA_HERB("Place a No Entry tile on an Island of your choice. The first time Mother Nature ends her movement there, " +
            "put the No Entry tile back onto this card and do not calculate influence on that Island, or place any Towers."),
    MAGIC_MAILMAN("You may move Mother Nature up to 2 additional Islands than is indicated by the Assistant card you've played"),
    SPOILED_PRINCESS("Take 1 Student form this card and place it in your Dining Room. Then, draw a new Student from the" +
            "Bag and place it on this card"),

    NONE("");

    private final String effect;

    Characters(String effect){
        this.effect = effect;
    }

    public String getEffect(){
        return effect;
    }

}
