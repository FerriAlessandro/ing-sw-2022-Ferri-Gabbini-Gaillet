package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.model.enumerations.AssistantCard;
import it.polimi.ingsw.model.enumerations.Characters;
import it.polimi.ingsw.model.enumerations.Color;
import it.polimi.ingsw.model.enumerations.TowerColor;
import it.polimi.ingsw.network.Adapter;
import it.polimi.ingsw.network.messages.*;
import it.polimi.ingsw.view.ViewInterface;

import java.security.InvalidParameterException;
import java.util.*;

/**
 * This class implements a very basic Command Line Interface.
 * @author A.G. Gaillet
 * @version 1.2
 */
public class Cli implements ViewInterface {
    private final Scanner in = new Scanner(System.in);
    private Adapter adapter;
    private String nickname;

    /** True if the current game follows expert rules, false otherwise */
    protected boolean expert;

    /** Coins owned by each player */
    protected final Map<String, Integer> coins;

    /**
     * Constructor.
     */
    public Cli(){
        coins =  new HashMap<>();
    }

    /**
     * Main function for the Command Line Interface
     */
    public void startGame(){
        System.out.println("""


                   ▄████████    ▄████████ ▄██   ▄      ▄████████ ███▄▄▄▄       ███      ▄█     ▄████████\s
                  ███    ███   ███    ███ ███   ██▄   ███    ███ ███▀▀▀██▄ ▀█████████▄ ███    ███    ███\s
                  ███    █▀    ███    ███ ███▄▄▄███   ███    ███ ███   ███    ▀███▀▀██ ███▌   ███    █▀ \s
                 ▄███▄▄▄      ▄███▄▄▄▄██▀ ▀▀▀▀▀▀███   ███    ███ ███   ███     ███   ▀ ███▌   ███       \s
                ▀▀███▀▀▀     ▀▀███▀▀▀▀▀   ▄██   ███ ▀███████████ ███   ███     ███     ███▌ ▀███████████\s
                  ███    █▄  ▀███████████ ███   ███   ███    ███ ███   ███     ███     ███           ███\s
                  ███    ███   ███    ███ ███   ███   ███    ███ ███   ███     ███     ███     ▄█    ███\s
                  ██████████   ███    ███  ▀█████▀    ███    █▀   ▀█   █▀     ▄████▀   █▀    ▄████████▀ \s
                               ███    ███                                                               \s


                Welcome to the Eryantis board game"""
        );
        String ip = "localhost";
        int port = 2351;
        System.out.println("Specify the ip address of the server (press Enter for default)");
        String ipTemp = in.nextLine();
        if(!ipTemp.isEmpty() && !ipTemp.isBlank()){
            ip = ipTemp;
        }
        System.out.println("Specify the port number of the server (press Enter for defaults)");
        String portTemp = in.nextLine();
        if(!portTemp.isEmpty() && !portTemp.isBlank()){
            port = Integer.parseInt(portTemp);
        }
        adapter = new Adapter(this, ip, port);
        System.out.print("\n");
    }

    /**
     * Asks the player to pick a nickname.
     */
    @Override
    public void askNickName() {
            System.out.print("Enter a nickname: ");
            nickname = in.nextLine();
            while (nickname.isEmpty() || nickname.isBlank()) {
                System.out.print("Please enter a valid nickname: ");
                nickname = in.nextLine();
            }
        adapter.sendMessage(new RMessageNickname(nickname));
        System.out.print("\n");
    }

    /**
     * Asks the player to specify the game settings.
     */
    @Override
    public void askGameSettings() {
        System.out.print("Enter the desired number of players: ");
        int num = nextInt();
        while(num != 2 && num != 3){
            System.out.print("Please enter valid a number: ");
            num = nextInt();
        }
        System.out.print("Do you want to play an expert game or not (y/n): ");
        String choice = in.nextLine();
        while(!choice.equals("y") && !choice.equals("n")){
            System.out.print("Please enter valid a answer (y/n): ");
            choice = in.nextLine();
        }
        if(choice.equals("y")){
            System.out.println("You chose to play an expert game");
            adapter.sendMessage(new RMessageGameSettings(num, true));
        }else{
            adapter.sendMessage(new RMessageGameSettings(num, false));
        }
        System.out.print("\n");
    }

    /**
     * Ask the player to move mother nature.
     */
    @Override
    public void askMotherNatureMove(SMessageMotherNature messageMotherNature) {
        int islandIdx;
        System.out.println("It's your turn to move mother nature. You can move her up to " + messageMotherNature.maxNumTiles + " tiles.");
        do {
            System.out.print("Please choose the island of destination by providing its id number: ");
            islandIdx =  nextInt();
        }while (islandIdx < 1 || islandIdx >12);
        adapter.sendMessage(new RMessageMotherNature(islandIdx, nickname));
        System.out.print("\n");
    }

    /**
     * Ask the player to pick an assistant card from provided available cards.
     *
     * @param message containing available assistants
     */
    @Override
    public void showAssistantChoice(SMessageShowDeck message) {
        int choice;
        int index;
        do {
            System.out.println("\nPlease pick an assistant card by providing its id: ");
            for(AssistantCard assistantCard : message.cards){
                index = message.cards.indexOf(assistantCard) + 1;
                String spacing1 = "";
                String spacing2 = "";
                for(int i = 0; i < 10 - assistantCard.name().length() - (assistantCard.ordinal() + 1)/10; i++){spacing1 = spacing1.concat(" ");}
                if(assistantCard.getCardValue() < 10){spacing2 = spacing2.concat(" ");}
                System.out.println(index + " - " + assistantCard + spacing1 + "(value: " + assistantCard.getCardValue() + "," + spacing2 +" moves: " + assistantCard.getMotherNatureMovement() + ")");
            }
            choice = nextInt();
        }while (choice < 1 || choice > message.cards.size());
        System.out.println("Chosen assistant : " + message.cards.get(choice - 1));
        adapter.sendMessage(new RMessageAssistant(message.cards.get(choice - 1), nickname));
        System.out.print("\n");
    }

    /**
     * Ask the player to pick a character card from the provided available cards (or no card).
     *
     * @param messageCharacter message containing available characters
     */
    @Override
    public void showCharacterChoice(SMessageCharacter messageCharacter){
        int choice = 0;
        int index;
        System.out.println("You have " + coins.get(nickname) + " coins.");

        if (messageCharacter.effects.stream().anyMatch(x -> x.getCost() <= coins.get(nickname))) {
            System.out.println("These are the available character cards: ");
            for (Characters character : messageCharacter.effects) {
                index = messageCharacter.effects.indexOf(character) + 1;
                System.out.print(index);
                System.out.print(" " + character + "\n");
                System.out.println(character.getEffect());
                System.out.println("\t This card costs " + messageCharacter.cardCost.get(character) + " coins");
                if (messageCharacter.students.get(character) != null && !messageCharacter.students.get(character).values().stream().allMatch(x -> x == 0)) {
                    System.out.println("\t Students on the card: ");
                    printColorMap(messageCharacter.students.get(character));
                } else if (character.equals(Characters.GRANDMA_HERB)) {
                    System.out.println("\t " + messageCharacter.noEntryTiles + " no entry tiles");
                }
                System.out.print("\n");
            }

            do {
                System.out.println("Please pick a character card by providing its id (use 0 to discard the choice)");
                choice = nextInt();
            } while (choice < 0 || choice > messageCharacter.effects.size());
        } else {
            System.out.println("You don't have enough coins to choose any character card.");
        }

        Characters chosenCharacter;
        if (choice != 0) {
            chosenCharacter = messageCharacter.effects.get(choice - 1);
        } else {
            chosenCharacter = Characters.NONE;
        }
        adapter.sendMessage(new RMessageCharacter(chosenCharacter, this.nickname));
        System.out.print("\n");
    }

    /**
     * Display lobby.
     *
     * @param message containing information on connected and desired players.
     */
    @Override
    public void showLobby(SMessageLobby message) {
        System.out.println("Waiting for " + message.numPlayersTotal + " players to be connected...");
        System.out.print("Current players: ");
        List<String> players = message.currentPlayers;
        for (String nickname : players){
            System.out.print(nickname + ", ");
        }
        System.out.print("\n");
    }

    /**
     * Display a disconnection message.
     */
    @Override
    public void showDisconnectionMessage() {
        System.out.println("Either you or all other remaining players lost connection - ending game");
        try {
            System.in.close();
        } catch (Exception ignored){}
    }

    /**
     * Shows the game status displaying the board.
     * @param gameState message containing game status.
     */
    @Override
    public void showBoard(SMessageGameState gameState) {
        //System.out.println("\033[H\033[2J"); //clears console
        System.out.println("THIS IS THE CURRENT SITUATION OF THE TABLE: ");
        System.out.println("Professor ownership: ");
        for (Color color: gameState.professors.keySet()){
            System.out.println("\t" + color + ": " + gameState.professors.get(color));
        }

        System.out.println("\nPlayer-boards: ");
        for (String player: gameState.studEntrance.keySet()){
            System.out.println("Player: " + player.toUpperCase());

            if(expert) {
                System.out.print("\t" + gameState.coins.get(player) + "coins\n");
                coins.put(player,gameState.coins.get(player));
            }

            System.out.print("\tTower color: " + gameState.towerColor.get(player));
            System.out.print("\t" + gameState.towerNumber.get(player) + " remaining");

            System.out.print("\n\tEntrance: ");
            printColorMap(gameState.studEntrance.get(player));

            System.out.print("\n\tDining room: ");
            printColorMap(gameState.studDining.get(player));
            System.out.print("\n");
        }

        System.out.println("\nClouds: ");
        for (int cloud: gameState.studClouds.keySet()){
            int cloudIdx = cloud + 1;
            System.out.print("\tCloud " + cloudIdx + ": ");
            printColorMap(gameState.studClouds.get(cloud));
            System.out.print("\n");
        }

        System.out.print("\nIslands: ");
        for (int island: gameState.studIslands.keySet()){
            int islandIdx = island + 1;
            String spacing = "";
            if(islandIdx < 10){ spacing = spacing.concat(" ");}
            System.out.print("\n\tIsland " + islandIdx + ": " + spacing);
            printColorMap(gameState.studIslands.get(island));
            if(!gameState.colorTowerIslands.get(island).equals(TowerColor.NONE) && gameState.numTowersIslands.get(island)!=0){
                System.out.print("\t" + gameState.numTowersIslands.get(island) + gameState.colorTowerIslands.get(island) + " towers");
            }
            if(gameState.motherNaturePosition == island){
                System.out.print("\t MOTHER NATURE");
            }
            if(gameState.forbiddenTokens.get(island)!=0){
                System.out.print("\t" + gameState.forbiddenTokens.get(island) + "forbidden tokens - Mother Nature CANNOT move here");
            }
        }
        System.out.print("\n");
    }

    /**
     * Display a "someone has won" message.
     * @param message containing information on who won.
     */
    @Override
    public void showWinMessage(SMessageWin message) {
        if(message.getType().equals(MessageType.S_WIN)){
            if(message.tie){
                System.out.println("It's a tie");
            }else {
                System.out.println(message.nickname + " has won !!!");
            }
        }else{
            new InvalidParameterException().printStackTrace();
        }
        System.out.print("\n");
    }

    /**
     * Display a generic text message.
     * @param message containing the {@link String} to be displayed.
     */
    @Override
    public void showGenericMessage(SMessageInvalid message) {
        System.out.println("\n" + message.error.toUpperCase());
        System.out.print("\n");
    }

    /**
     * Ask the player to pick a cloud.
     */
    @Override
    public void askCloud() {
        System.out.println("It's your turn to choose a cloud");
        int cloudIdx;
        do {
            System.out.print("Please choose the cloud you wish to pick by providing its id number: ");
            cloudIdx =  nextInt();
        }while (cloudIdx < 1 || cloudIdx >4);
        adapter.sendMessage(new RMessageCloud(cloudIdx, nickname));
        System.out.print("\n");
    }

    /**
     * Ask the player to move a student.
     */
    @Override
    public void askMove(){
        System.out.println("It's your turn to move the students - please pick your next move");
        String chosenColor;
        int destination;

        chosenColor = colorChoice();

        do{
            System.out.print("Please choose the destination by providing its id (1-12 islands), (0 dining room): ");
            destination = nextInt();
        }while (destination < 0 || destination > 12);

        adapter.sendMessage(new RMessageMove(Color.valueOf(chosenColor), destination, nickname));
        System.out.print("\n");
    }

    /**
     * Prompts the user to try again. Should be followed by specific request.
     */
    @Override
    public void askAgain() {
        System.out.println("Invalid request. Please try again...");
        System.out.print("\n");
    }

    /**
     * Update current player.
     *
     * @param messageCurrentPlayer
     */
    @Override
    public void showCurrentPlayer(SMessageCurrentPlayer messageCurrentPlayer) {

        System.out.println("\n\nIt's now " + messageCurrentPlayer.nickname + "'s turn");
        System.out.print("\n");
    }

    /**
     * Getter method for the nickname of the associated player
     *
     * @return {@link String} nickname of the associated player
     */
    @Override
    public String getNickName() {
        return nickname;
    }

    /**
     * Asks additional information on chosen character effect of
     * {@link it.polimi.ingsw.model.enumerations.Characters#GRANDMA_HERB} or
     * {@link it.polimi.ingsw.model.enumerations.Characters#HERALD}.
     * @param messageGrandmaHerbHerald request message
     */
    public void grandmaHerbHeraldScene(SMessageGrandmaherbHerald messageGrandmaHerbHerald){
        System.out.println("You chose the: " + messageGrandmaHerbHerald.characterName);
        System.out.println("Effect: " + messageGrandmaHerbHerald.characterName.getEffect());
        int islandIdx;
        do {
            System.out.print("Please choose the island where the effect will be applied by providing its id number: ");
            islandIdx =  nextInt();
        }while (islandIdx < 1 || islandIdx >12);

        adapter.sendMessage(new RMessageGrandmaherbHerald(messageGrandmaHerbHerald.characterName, nickname, islandIdx));
    }

    /**
     * Asks additional information on chosen character effect of
     * {@link it.polimi.ingsw.model.enumerations.Characters#MONK} or
     * {@link it.polimi.ingsw.model.enumerations.Characters#SPOILED_PRINCESS}.
     * @param messageMonkPrincess request message
     */
    public void monkPrincessScene(SMessageMonkPrincess messageMonkPrincess){
        System.out.println("You chose the: " + messageMonkPrincess.characterName);
        System.out.println("Effect: " + messageMonkPrincess.characterName.getEffect());

        System.out.println("Please choose a student among the following: ");
        printColorMap(messageMonkPrincess.colors);

        String choice = colorChoice();
        int islandIdx = -1;
        if(messageMonkPrincess.characterName.equals(Characters.MONK)){
            do {
                System.out.print("Please choose the island where the effect will be applied by providing its id number: ");
                islandIdx =  nextInt();
            }while (islandIdx < 1 || islandIdx >12);
        }

        adapter.sendMessage(new RMessageMonkPrincess(messageMonkPrincess.characterName, nickname, islandIdx, Color.valueOf(choice)));
    }

    /**
     * Asks additional information on chosen character effect of
     * {@link it.polimi.ingsw.model.enumerations.Characters#ROGUE} or
     * {@link it.polimi.ingsw.model.enumerations.Characters#MUSHROOM_PICKER}.
     * @param messageRogueMushroomPicker request message
     */
    public void rogueMushroomPickerScene(SMessageRogueMushroomPicker messageRogueMushroomPicker){
        System.out.println("You chose the: " + Characters.MUSHROOM_PICKER);
        System.out.println("Effect: " + Characters.MUSHROOM_PICKER.getEffect());
        String choice = colorChoice();

        adapter.sendMessage(new RMessageRogueMushroomPicker(messageRogueMushroomPicker.characterName, nickname, Color.valueOf(choice)));
    }

    /**
     * Asks additional information on chosen character effect of
     * {@link it.polimi.ingsw.model.enumerations.Characters#JESTER} or
     * {@link it.polimi.ingsw.model.enumerations.Characters#BARD}.
     * @param messageJesterBard request message
     */
    public void jesterBardScene(SMessageJesterBard messageJesterBard){
        ArrayList<Color> origin = new ArrayList<>();
        ArrayList<Color> entrance = new ArrayList<>();

        System.out.println("You chose the: " + messageJesterBard.characterName);
        System.out.println("Effect: " + messageJesterBard.characterName.getEffect());
        System.out.print("These are the students available ");
        switch (messageJesterBard.characterName) {
            case JESTER -> System.out.print("on the card: ");
            case BARD -> System.out.print("in the dining room: ");
            default -> new InvalidParameterException().printStackTrace();
        }
        printColorMap(messageJesterBard.origin);

        System.out.print("\nThese are the students in your entrance: ");
        printColorMap(messageJesterBard.entrance);

        System.out.print("\nPlease choose the students to be taken from the ");
        switch (messageJesterBard.characterName) {
            case JESTER -> System.out.print("card\n");
            case BARD -> System.out.print("dining room\n");
            default -> new InvalidParameterException().printStackTrace();
        }

        System.out.print("You need to choose up to " + messageJesterBard.maxStudents + "students (one at a time) between ");
        int count = 0;
        String choice;
        do{
            do{
                choice = colorChoice();
            }while (messageJesterBard.origin.get(Color.valueOf(choice)) <= 0 || !messageJesterBard.origin.containsKey(Color.valueOf(choice)));
            count ++;
            origin.add(Color.valueOf(choice));
            messageJesterBard.origin.remove(Color.valueOf(choice));

            do {
                System.out.println("Do you wish to select another student? y/n");
                choice = in.nextLine();
            }while (!choice.equals("y") && !choice.equals("n"));
        }while(count < messageJesterBard.maxStudents && choice.equals("y"));

        System.out.println("Please choose the students in the entrance you want to swap, one at a time");
        for(int i=0; i < count; i++){
            do{
                choice = colorChoice();
            }while (messageJesterBard.entrance.get(Color.valueOf(choice)) <= 0 || !messageJesterBard.entrance.containsKey(Color.valueOf(choice)));
            entrance.add(Color.valueOf(choice));
            messageJesterBard.entrance.remove(Color.valueOf(choice));
        }

        adapter.sendMessage(new RMessageJesterBard(messageJesterBard.characterName, nickname, origin, entrance));
    }

    /**
     * Ask the user whether to use the loaded game save or not.
     */
    @Override
    public void askUseSavedGame(SMessageLoadGame message) {
        String choice;
        do {
            System.out.println("A saved game was found. This game is a " + message.numOfPlayers + " players " + (message.expert ? "expert game" : "simple game"));
            System.out.println("Do you wish to continue playing this game? y/n");
            choice = in.nextLine();
        }while (!choice.equals("y") && !choice.equals("n"));
        if (choice.equals("y")){
            adapter.sendMessage(new RMessageLoadGame(true));
        }else{
            adapter.sendMessage(new RMessageLoadGame(false));
        }
        System.out.print("\n");
    }

    /**
     * Used to set the client flag for expert game handling.
     *
     * @param messageExpert message containing the flag value
     */
    @Override
    public void setExpert(SMessageExpert messageExpert) {
        expert = messageExpert.expert;
    }

    /**
     * Used to notify all clients and update the assistant card chosen by a player.
     *
     * @param messageAssistantStatus containing nickname and chosen assistant
     */
    @Override
    public void showAssistantStatus(SMessageAssistantStatus messageAssistantStatus) {
        System.out.println(
                messageAssistantStatus.nickname + " chose the " + messageAssistantStatus.chosenAssistant + " assistant card. " +
                        "Value: " + messageAssistantStatus.chosenAssistant.getCardValue() + ", movements: " +
                        messageAssistantStatus.chosenAssistant.getMotherNatureMovement()
        );
    }

    /**
     * Provides a {@link String} containing a list of existing {@link Color}s
     * @return {@link String} of comma and space separated colors (e.g. "RED, GREEN, BLUE")
     */
    private String colorList(){
        String value = "";
        for(Color color: Color.values()){
            value = value.concat(color.toString() + ", ");
        }
        return value.substring(0, value.length()-2);
    }

    /**
     * Checks if the provided {@link String} is linked to a {@link Color}.
     * @param color {@link String} to be checked
     * @return true if the provided color is valid
     */
    private boolean isValidColor(String color){
        try{
            Color.valueOf(color);
        }catch (IllegalArgumentException ignored){
            return false;
        }
        return true;
    }

    /**
     * Prompts the user to pick a color.
     * @return {@link String} name of the color
     */
    private String colorChoice(){
        String choice;
        System.out.println("Please choose a color among the following: " + colorList());
        choice = in.nextLine();
        while (!isValidColor(choice)){
            System.out.println("Please choose a valid color (" + colorList() + "): ");
            choice = in.nextLine();
        }
        return choice;
    }

    /**
     * Prints a {@link Map} with {@link Color} as a key and {@link Integer} as content.
     * @param map to be printed
     */
    private void printColorMap(Map<Color, Integer> map){
        for(Color color: map.keySet()){
            if(map.get(color) != 0)
                System.out.print(colored(color) + map.get(color) + " " + color + endColor() + "\t");
        }
    }

    /**
     * Start coloring text of provided color.
     * @param color
     * @return ANSI color escape sequence
     */
    private String colored(Color color){
        return CliColors.colored(color);
    }

    /**
     * End text coloring.
     * @return ANSI color escape code for reset
     */
    private String endColor(){
        return CliColors.endColor();
    }

    /**
     * Gets int from standard input.
     * @return int value
     */
    private int nextInt() {
        boolean valid = false;
        Integer number = null;
        do {
            String string = in.nextLine();
            try {
                number = Integer.parseInt(string);
                valid = true;
            } catch (NumberFormatException e){
                System.out.println("Please input a number");
            }
        }while (!valid);
        return number;
    }

}
