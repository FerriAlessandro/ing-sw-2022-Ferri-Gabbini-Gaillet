package it.polimi.ingsw.model.enumerations;

public enum Characters {

    HERALD("Choose an Island and resolve the Island as if Mother Nature had ended her movement there." +
            "Mother Nature will still move and the Island where she ends her movement will also be resolved", 3),
    KNIGHT("During the influence calculation this turn, you count as having 2 more influence", 2),
    CENTAUR("When resolving a Conquering on an Island, Towers do not count towards influence", 3),
    FARMER("During this turn, you take control of any number of Professors even if you have the same " +
            "number of Students as the player who currently controls them",2),
    MUSHROOM_PICKER("Choose a color of Student: during the influence calculation this turn, that color adds no influence", 3),
    JESTER("You may take up to 3 Students from this card and replace them with the same number of Students" +
            "from your Entrance", 1),
    ROGUE("Choose a type of Student: every player (including yourself) must return 3 Students of that type" +
            "from their Dining Room to the bag. If any player has fewer than 3 Students of that type, return as " +
            "many Students as they have", 3),
    BARD("You may exchange up to 2 Students between your Entrance and your Dining Room", 1),
    MONK("Take 1 Student form this card and place it on an Island of your choice. Then, draw a new Student" +
            "from the Bag and place it on this card", 1),
    GRANDMA_HERB("Place a No Entry tile on an Island of your choice. The first time Mother Nature ends her movement there, " +
            "put the No Entry tile back onto this card and do not calculate influence on that Island, or place any Towers.", 2),
    MAGIC_MAILMAN("You may move Mother Nature up to 2 additional Islands than is indicated by the Assistant card you've played", 1),
    SPOILED_PRINCESS("Take 1 Student form this card and place it in your Dining Room. Then, draw a new Student from the" +
            "Bag and place it on this card", 2),

    NONE("", 0);

    private final String effect;
    private final int cost;

    Characters(String effect, int cost){
        this.effect = effect;
        this.cost=cost;
    }

    public String getEffect(){
        return effect;
    }
    public int getCost(){return cost;}

}
