package it.polimi.ingsw.view;

import it.polimi.ingsw.model.enumerations.AssistantCard;
import it.polimi.ingsw.model.enumerations.Characters;
import it.polimi.ingsw.model.enumerations.Color;
import it.polimi.ingsw.model.enumerations.TowerColor;
import it.polimi.ingsw.network.Adapter;
import it.polimi.ingsw.network.messages.*;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.Scanner;

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
        System.out.print("Specify the ip address of the server (press Enter for default)");
        String ipTemp = in.nextLine();
        if(!ipTemp.isEmpty() && !ipTemp.isBlank()){
            ip = ipTemp;
        }
        System.out.print("Specify the port number of the server (press Enter for defaults)");
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
        int index = 0;
        do {
            System.out.println("Please pick an assistant card by providing its id: ");
            for(AssistantCard assistantCard : message.cards){
                index = message.cards.indexOf(assistantCard) + 1;
                System.out.println(message.cards.indexOf(assistantCard) + 1 + " - " + assistantCard);
            }
            choice = in.nextInt();
        }while (choice < 1 || choice > message.cards.size());
        adapter.sendMessage(new RMessageAssistant(message.cards.get(index - 1), nickname));
    }

    /**
     * Ask the player to pick a character card from the provided available cards (or no card).
     *
     * @param messageCharacter message containing available characters
     */
    @Override
    public void showCharacterChoice(SMessageCharacter messageCharacter){
        int choice;
        int index = 0;
        do{
            System.out.println("These are the available character cards: ");
            for(Characters character : messageCharacter.effects){
                index = messageCharacter.effects.indexOf(character) + 1;
                System.out.print(index);
                System.out.print(" " + character + "\n");
                System.out.println(character.getEffect());
                System.out.println("\t This card costs " + messageCharacter.cardCost.get(character) + " coins");
                if(messageCharacter.students.get(character)!=null){
                    System.out.println("\t Students on the card: ");
                    for(Color color : messageCharacter.students.get(character).keySet()){
                        System.out.println("\t " + messageCharacter.students.get(character).get(color) + " x " + color);
                    }
                }else if (character.equals(Characters.GRANDMA_HERB) && messageCharacter.noEntryTiles!=null){
                    System.out.println("\t " + messageCharacter.noEntryTiles + " no entry tiles");
                }
            }
        }
        do {
            System.out.println("Please pick a character card by providing its id (use 0 to discard the choice)");
            choice = in.nextInt();
        }while (choice < 0 && choice > messageCharacter.effects.size());
        Characters chosenCharacter;
        if(choice!=0){
            chosenCharacter = messageCharacter.effects.get(choice - 1);
        }else{
            chosenCharacter = null;
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
            for (Color color: gameState.studEntrance.get(player).keySet()){
                System.out.print("\t\t" + gameState.studEntrance.get(player).get(color) + " x " + color);
            }

            System.out.print("\n\tDining room: ");
            for (Color color: gameState.studDining.get(player).keySet()){
                System.out.print("\t\t" + gameState.studDining.get(player).get(color) + " x " + color);
            }
        }

        System.out.println("\n\nClouds: ");
        for (int cloud: gameState.studClouds.keySet()){
            int cloudIdx = cloud + 1;
            System.out.print("\nCloud " + cloudIdx + ": ");
            for (Color color: gameState.studClouds.get(cloud).keySet()){
                System.out.print("\t" + gameState.studClouds.get(cloud).get(color) + " x " + color);
            }
        }

        System.out.println("\n\nIslands: ");
        for (int island: gameState.studIslands.keySet()){
            int islandIdx = island + 1;
            System.out.print("\nIsland " + islandIdx + ": ");
            for (Color color: gameState.studIslands.get(island).keySet()){
                System.out.print("\t" + gameState.studIslands.get(island).get(color) + " x " + color);
            }
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
            System.out.println(message.winner + "won the game!!!");
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
        Color chosenColorEnum = null;

        boolean available = false;
        do {
            System.out.print("Please pick the color (");
            for (Color color: Color.values()) {
                    System.out.print(color + " ");
                }
            System.out.print("): ");
            chosenColor =  in.nextLine();
            try {
                chosenColorEnum = Color.valueOf(chosenColor);
                available = true;
            }catch (IllegalArgumentException ignored){
                System.out.println("Please pick a valid color");
            }
        }while (!available);

        do{
            System.out.print("Please choose the destination by providing its id (1-12 islands), (0 dining room): ");
            destination = in.nextInt();
        }while (destination < 0 || destination > 12);

        adapter.sendMessage(new RMessageMove(chosenColorEnum, destination, nickname));
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
}
