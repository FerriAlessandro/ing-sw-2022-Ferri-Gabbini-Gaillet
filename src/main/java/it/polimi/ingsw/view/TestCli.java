package it.polimi.ingsw.view;

import it.polimi.ingsw.model.enumerations.AssistantCard;
import it.polimi.ingsw.model.enumerations.Characters;
import it.polimi.ingsw.model.enumerations.Color;
import it.polimi.ingsw.model.enumerations.TowerColor;
import it.polimi.ingsw.network.Adapter;
import it.polimi.ingsw.network.messages.*;

import java.security.InvalidParameterException;
import java.util.*;

/**
 * This class implements a very basic Command Line Interface.
 * @author A.G. Gaillet
 * @version 1.0
 */
public class TestCli implements ViewInterface {
    private final Scanner in = new Scanner(System.in);
    private Adapter adapter;
    private String nickname;

    /**
     * Main function for the Command Line Interface
     */
    public void startGame(){
        System.out.println( "\n\n" +
                            "   ▄████████    ▄████████ ▄██   ▄      ▄████████ ███▄▄▄▄       ███      ▄█     ▄████████ \n" +
                            "  ███    ███   ███    ███ ███   ██▄   ███    ███ ███▀▀▀██▄ ▀█████████▄ ███    ███    ███ \n" +
                            "  ███    █▀    ███    ███ ███▄▄▄███   ███    ███ ███   ███    ▀███▀▀██ ███▌   ███    █▀  \n" +
                            " ▄███▄▄▄      ▄███▄▄▄▄██▀ ▀▀▀▀▀▀███   ███    ███ ███   ███     ███   ▀ ███▌   ███        \n" +
                            "▀▀███▀▀▀     ▀▀███▀▀▀▀▀   ▄██   ███ ▀███████████ ███   ███     ███     ███▌ ▀███████████ \n" +
                            "  ███    █▄  ▀███████████ ███   ███   ███    ███ ███   ███     ███     ███           ███ \n" +
                            "  ███    ███   ███    ███ ███   ███   ███    ███ ███   ███     ███     ███     ▄█    ███ \n" +
                            "  ██████████   ███    ███  ▀█████▀    ███    █▀   ▀█   █▀     ▄████▀   █▀    ▄████████▀  \n" +
                            "               ███    ███                                                                \n" +
                            "\n\nWelcome to the Eryantis board game"
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
    }

    /**
     * Asks the player to specify the game settings.
     */
    @Override
    public void askGameSettings() {
        System.out.print("Enter the desired number of players: ");
        int num = in.nextInt();
        while(num<= 0){
            System.out.print("Please enter valid a number: ");
            num = in.nextInt();
        }
        System.out.print("Do you want to play an expert game or not: y/n");
        String choice = in.nextLine();
        while(!choice.equals("y") && !choice.equals("n")){
            System.out.print("Please enter valid a answer (y/n): ");
            choice = in.nextLine();
        }
        if(choice.equals("y")){
            adapter.sendMessage(new RMessageGameSettings(num, true));
        }else{
            adapter.sendMessage(new RMessageGameSettings(num, false));
        }
    }

    /**
     * Ask the player to move mother nature.
     */
    @Override
    public void askMotherNatureMove() {
        int islandIdx;
        System.out.println("It's your turn to move mother nature");
        do {
            System.out.print("Please choose the island of destination by providing its id number: ");
            islandIdx =  in.nextInt();
        }while (islandIdx < 1 || islandIdx >12);
        adapter.sendMessage(new RMessageMotherNature(islandIdx, nickname));
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
            System.out.println("Please pick an assistant card by providing its id: ");
            for(AssistantCard assistantCard : message.cards){
                index = message.cards.indexOf(assistantCard) + 1;
                System.out.println(index + " - " + assistantCard);
            }
            choice = in.nextInt();
        }while (choice < 1 || choice > message.cards.size());
        System.out.println("Chosen assistant : ");
        adapter.sendMessage(new RMessageAssistant(message.cards.get(choice - 1), nickname));
    }

    /**
     * Ask the player to pick a character card from the provided available cards (or no card).
     *
     * @param messageCharacter message containing available characters
     */
    @Override
    public void showCharacterChoice(SMessageCharacter messageCharacter){
        int choice;
        int index;

        System.out.println("These are the available character cards: ");
        for(Characters character : messageCharacter.effects){
            index = messageCharacter.effects.indexOf(character) + 1;
            System.out.print(index);
            System.out.print(" " + character + "\n");
            System.out.println(character.getEffect());
            System.out.println("\t This card costs " + messageCharacter.cardCost.get(character) + " coins");
            if(messageCharacter.students.get(character)!=null){
                System.out.println("\t Students on the card: ");
                printColorMap(messageCharacter.students.get(character));
            }else if (character.equals(Characters.GRANDMA_HERB)){
                System.out.println("\t " + messageCharacter.noEntryTiles + " no entry tiles");
            }
        }

        do {
            System.out.println("Please pick a character card by providing its id (use 0 to discard the choice)");
            choice = in.nextInt();
        }while (choice < 0 || choice > messageCharacter.effects.size());
        Characters chosenCharacter;
        if(choice!=0){
            chosenCharacter = messageCharacter.effects.get(choice - 1);
        }else{
            chosenCharacter = Characters.NONE;
        }
        adapter.sendMessage(new RMessageCharacter(chosenCharacter, this.nickname));
    }

    /**
     * Display lobby.
     *
     * @param message containing information on connected and desired players.
     */
    @Override
    public void showLobby(SMessageLobby message) {
        System.out.println("Waiting for " + message.numPlayersTotal + " players to be connected...");
        System.out.println("Current players: ");
        List<String> players = message.currentPlayers;
        for (String nickname : players){
            System.out.println(nickname);
        }
    }

    /**
     * Display a disconnection message.
     */
    @Override
    public void showDisconnectionMessage() {
        System.out.println("Connection lost - you were disconnected");
    }

    /**
     * Shows the game status displaying the board.
     * @param gameState message containing game status.
     */
    @Override
    public void showBoard(SMessageGameState gameState) {
        System.out.println("THIS IS THE CURRENT SITUATION OF THE TABLE: ");
        System.out.println("Professor ownership: ");
        for (Color color: gameState.professors.keySet()){
            System.out.println("\t" + color + ": " + gameState.professors.get(color));
        }

        System.out.println("\nPlayer-boards: ");
        for (String player: gameState.studEntrance.keySet()){
            System.out.println("Player: " + player.toUpperCase());

            System.out.print("\tEntrance: ");
            printColorMap(gameState.studEntrance.get(player));

            System.out.print("\n\tDining room: ");
            printColorMap(gameState.studDining.get(player));
        }

        System.out.println("\n\nClouds: ");
        for (int cloud: gameState.studClouds.keySet()){
            int cloudIdx = cloud + 1;
            System.out.print("\nCloud " + cloudIdx + ": ");
            printColorMap(gameState.studClouds.get(cloud));
        }

        System.out.println("\n\nIslands: ");
        for (int island: gameState.studIslands.keySet()){
            int islandIdx = island + 1;
            System.out.print("\nIsland " + islandIdx + ": ");
            printColorMap(gameState.studIslands.get(island));
            if(!gameState.colorTowerIslands.get(island).equals(TowerColor.NONE) && gameState.numTowersIslands.get(island)!=0){
                System.out.print("\t" + gameState.numTowersIslands.get(island) + gameState.colorTowerIslands.get(island) + " towers");
            }
            if(gameState.motherNaturePosition == island){
                System.out.print("\t MOTHER NATURE");
            }
            if(gameState.forbiddenTokens.get(island)!=0){
                System.out.print(gameState.forbiddenTokens.get(island) + "forbidden tokens - Mother Nature CANNOT move here");
            }
        }
    }

    /**
     * Display a "someone has won" message.
     * @param message containing information on who won.
     */
    @Override
    public void showWinMessage(SMessageWin message) {
        if(message.getType().equals(MessageType.S_WIN)){
            System.out.println(message.winMessage);
        }else{
            new InvalidParameterException().printStackTrace();
        }
    }

    /**
     * Display a generic text message.
     * @param message containing the {@link String} to be displayed.
     */
    @Override
    public void showGenericMessage(SMessageInvalid message) {
        System.out.println(message.getError());
    }

    /**
     * Ask the player to pick a cloud.
     */
    @Override
    public void askCloud() {
        System.out.println("It's your turn to choose an island");
        int cloudIdx;
        do {
            System.out.print("Please choose the cloud you wish to pick by providing its id number: ");
            cloudIdx =  in.nextInt();
        }while (cloudIdx < 1 || cloudIdx >4);
        adapter.sendMessage(new RMessageCloud(cloudIdx, nickname));
    }

    /**
     * Ask the player to move a student.
     */
    @Override
    public void askMove(){
        System.out.println("It's your turn to move the students");
        System.out.println("Please pick your next move.");
        String chosenColor;
        int destination;

        System.out.print("Please pick the color (" + colorList() + "): ");
        chosenColor = colorChoice();

        do{
            System.out.print("Please choose the destination by providing its id (1-12 islands), (0 dining room): ");
            destination = in.nextInt();
        }while (destination < 0 || destination > 12);

        adapter.sendMessage(new RMessageMove(Color.valueOf(chosenColor), destination, nickname));
    }

    /**
     * Prompts the user to try again. Should be followed by specific request.
     */
    @Override
    public void askAgain() {
        System.out.println("Invalid request. Please try again...");
    }

    /**
     * Update current player.
     *
     * @param messageCurrentPlayer
     */
    @Override
    public void showCurrentPlayer(SMessageCurrentPlayer messageCurrentPlayer) {
        System.out.println("It's now " + messageCurrentPlayer.nickname + "'s turn");
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
     * Ask additional information on chosen character effect when necessary.
     *
     * @param message containing data required to perform the related operation
     */
    @Deprecated
    @Override
    public void askCharacterMove(SMessage message) {
        switch (message.getType()){
            case S_JESTERBARD:
                jesterBardScene((SMessageJesterBard) message);
                break;

            case S_ROGUEMUSHROOMPICKER:
                rogueMushroomPickerScene((SMessageRogueMushroomPicker) message);
                break;

            case S_MONKPRINCESS:
                monkPrincessScene((SMessageMonkPrincess) message);
                break;

            case S_GRANDMAHERBHERALD:
                grandmaHerbHeraldScene((SMessageGrandmaherbHerald) message);
                break;

            default:
                new InvalidParameterException().printStackTrace();
        }
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
            islandIdx =  in.nextInt();
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
                islandIdx =  in.nextInt();
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
        switch (messageJesterBard.characterName){
            case JESTER:
                System.out.print("on the card: ");
                break;
            case BARD:
                System.out.print("in the dining room: ");
                break;
            default:
                new InvalidParameterException().printStackTrace();
                break;
        }
        printColorMap(messageJesterBard.origin);

        System.out.print("\nThese are the students in your entrance: ");
        printColorMap(messageJesterBard.entrance);

        System.out.print("\nPlease choose the students to be taken from the ");
        switch (messageJesterBard.characterName){
            case JESTER:
                System.out.print("card\n");
                break;
            case BARD:
                System.out.print("dining room\n");
                break;
            default:
                new InvalidParameterException().printStackTrace();
                break;
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

            do {
                System.out.println("Do you wish to select a student? y/n");
                choice = in.nextLine();
            }while (!choice.equals("y") && !choice.equals("n"));
        }while(count < messageJesterBard.maxStudents && choice.equals("y"));

        System.out.println("Please choose the students in the entrance you want to swap, one at a time");
        for(int i=0; i < count; i++){
            do{
                choice = colorChoice();
            }while (messageJesterBard.entrance.get(Color.valueOf(choice)) <= 0 || !messageJesterBard.entrance.containsKey(Color.valueOf(choice)));
            entrance.add(Color.valueOf(choice));
        }

        adapter.sendMessage(new RMessageJesterBard(messageJesterBard.characterName, nickname, origin, entrance));
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
        do {
            System.out.println("Please choose a valid color (" + colorList() + "): ");
            choice = in.nextLine();
        }while (!isValidColor(choice));
        return choice;
    }

    /**
     * Prints a {@link Map} with {@link Color} as a key and {@link Integer} as content.
     * @param map to be printed
     */
    private void printColorMap(Map<Color, Integer> map){
        for(Color color: map.keySet()){
            if(map.get(color) != 0)
                System.out.print(map.get(color) + "x" + color + "\t");
        }
    }

}
