package it.polimi.ingsw.controller;

import java.io.*;
import java.util.HashMap;

public class DiskManager {
    private static final String filename = "save.bin";
    public static void saveGame(InputController inputController){
        System.out.println("Saving game");
        try{
            writeFile(inputController, new File(filename));
        }catch (IOException e){
            System.out.println("Unable to save the game");
        }
    }

    public static InputController loadGame(int numPlayers, boolean expert){
        System.out.println("Checking existence of saved game");
        InputController read = readFile(new File(filename));
        if(read == null){
            System.out.println("No saved game");
            return null;
        }
        System.out.println("Found a save");
        if(read.getGameController().isExpert() == expert && read.getGameController().getNickNames().size() == numPlayers){
            System.out.println("Save is compatible with requests");
            read.getGameController().playersView = new HashMap<>();
            return read;
        }
        System.out.println("Save not compatible with requests");
        return null;
    }

    private static void writeFile(InputController inputController, File file) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(file); ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(inputController);
            oos.flush();
        }
    }

    private static InputController readFile(File file){
        InputController inputController = null;
        try{
            FileInputStream fileInputStream = new FileInputStream(file);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            inputController = (InputController) objectInputStream.readObject();
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("Unable to find any saved game");
        }
        return inputController;
    }
}
