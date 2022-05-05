package it.polimi.ingsw.controller;

import java.io.*;
import java.util.HashMap;

//TODO: add javadoc
public class DiskManager {
    private static final String filename = "save.bin";
    public static void saveGame(GameController gameController){
        System.out.println("Saving game");
        try{
            writeFile(gameController, new File(filename));
        }catch (IOException e){
            System.out.println("Unable to save the game");
        }
    }

    public static GameController loadGame(int numPlayers, boolean expert){
        System.out.println("Checking existence of saved game");
        GameController read = readFile(new File(filename));
        if(read == null){
            System.out.println("No saved game");
            return null;
        }
        System.out.println("Found a save");
        if(read.isExpert() == expert && read.getNickNames().size() == numPlayers){
            System.out.println("Save is compatible with requests");
            read.playersView = new HashMap<>();
            return read;
        }
        System.out.println("Save not compatible with requests");
        return null;
    }

    private static void writeFile(GameController gameController, File file) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(file); ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(gameController);
            oos.flush();
        }
    }

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
