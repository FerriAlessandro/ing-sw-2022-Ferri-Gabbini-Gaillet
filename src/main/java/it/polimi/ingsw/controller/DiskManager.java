package it.polimi.ingsw.controller;

import java.io.*;

/**
 * This class provides methods for storing on disk the game state and therefore implementing persistence.
 * @author A.G. Gaillet
 * @version 1.0
 */
public class DiskManager {
    private static final String filename = "save.bin";

    /**
     * Saves the provided {@link GameController} and contained game to disk.
     * @param gameController to be saved
     */
    public static void saveGame(GameController gameController){
        System.out.println("Saving game");
        try{
            writeFile(gameController, new File(filename));
        }catch (IOException e){
            System.out.println("Unable to save the game");
        }
    }

    /**
     * Loads the saved game version if it matches the provided settings.
     * @param numPlayers required
     * @param expert true if the game is to be an expert game
     * @return the {@link GameController} matching the provided settings, or null if one is not found
     */
    /*public static GameController loadGame(int numPlayers, boolean expert){
        System.out.println("Checking existence of saved game");
        GameController read = readFile(new File(filename));
        if(read == null){
            System.out.println("No saved game");
            return null;
        }
        System.out.println("Found a save");

        if(read.isExpert() == expert && read.getNickNames().size() == numPlayers){
            System.out.println("Save is compatible with requests");
            return read;
        }
        System.out.println("Save not compatible with requests");
        return null;
    }*/

    /**
     * Loads a saved game if it exists.
     * @return
     */
    public static GameController loadGame(){
        System.out.println("Checking existence of saved game");
        GameController read = readFile(new File(filename));
        if(read == null){
            System.out.println("No saved game");
        }else {
            System.out.println("Found a save");
        }
        return read;

    }

    /**
     * Writes to disk the provided {@link GameController}
     * @param gameController to be stored
     * @param file to use as storage
     * @throws IOException when unable to access directory and/or create file
     */
    private static void writeFile(GameController gameController, File file) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(file); ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(gameController);
            oos.flush();
        }
    }

    /**
     * Reads file from disk and returns content.
     * @param file to be read
     * @return {@link GameController} if file exists and contains a {@link GameController}, otherwise returns null
     */
    private static GameController readFile(File file){
        GameController gameController = null;
        try{
            FileInputStream fileInputStream = new FileInputStream(file);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            gameController = (GameController) objectInputStream.readObject();
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("Unable to find any saved game");
        }
        return gameController;
    }
}
