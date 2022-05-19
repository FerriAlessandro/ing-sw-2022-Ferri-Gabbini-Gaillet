package it.polimi.ingsw.view.cli.drawables;

import java.util.ArrayList;

/**
 * This abstract class provides utility methods for handling {@link Drawable} objects.
 * @author A.G. Gaillet
 * @version 1.0
 */
public abstract class Drawable {

    /**
     * This method returns a multi-line cascade of all provided drawables in a printable format. These need to be all the
     * same height.
     * @param objects to be added
     * @param numOnOneLine number of objects on the first line
     * @param height of elements
     * @return {@link String}
     */
    public String drawAll(ArrayList<Drawable> objects, int numOnOneLine, int height){
        String spacing = "  ";

        ArrayList<String> strings = new ArrayList<>();
        for (Drawable object : objects) {
            strings.add(object.toString());
        }
        String printable = "";

        //The first line of elements is created first to last
        for (int j = 0; j < height; j++) {
            for (int i = 0; i < numOnOneLine; i++) {
                printable = printable.concat(strings.get(i).substring(0, strings.get(i).indexOf("\n")));
                strings.set(i, strings.get(i).substring(strings.get(i).indexOf("\n")+1));
                printable = printable.concat(spacing);
            }
            printable = printable.concat("\n");
        }

        //The second line of elements is created last to first
        for (int j = 0; j < height; j++) {
            for (int i = objects.size()-1; i >= numOnOneLine; i--) {
                printable = printable.concat(strings.get(i).substring(0, strings.get(i).indexOf("\n")));
                strings.set(i, strings.get(i).substring(strings.get(i).indexOf("\n")+1));
                printable = printable.concat(spacing);
            }
            printable = printable.concat("\n");
        }

        return printable;
    }

    /**
     * This method provides a printable version of the game state.
     * @param islands
     * @param clouds
     * @param boards
     * @return {@link String}
     */
    public String drawFullScene(ArrayList<Drawable> islands, ArrayList<Drawable> clouds, ArrayList<Drawable> boards){
        String boardString = drawAll(boards, boards.size(), DrawablePlayerBoard.height);
        String[] boardLines = boardString.split("\\n");

        String padding1 = "      ";
        String padding2 = "             ";
        String islandString = drawAll(islands, islands.size()/2, DrawableIsland.height);
        String[] islandLines = islandString.split("\\n");
        String scene = "";

        scene = scene.concat("CLOUDS:\n");
        scene = scene.concat(drawAll(clouds, clouds.size(), DrawableCloud.height));
        scene = scene.concat("PLAYER BOARDS:          ");
        for(int i = 2; i < boards.size(); i++){
            scene = scene.concat(padding2);
        }
        scene = scene.concat(padding1 + "ISLANDS:\n");

        for(int i = 0; i < java.lang.Math.max(boardLines.length, islandLines.length); i++){
            if (i < boardLines.length) {
                scene = scene.concat(boardLines[i]);
            }else{
                for(int j = 0; j < boards.size(); j++) {
                    scene = scene.concat(padding2);
                }
            }
            scene = scene.concat(padding1);
            if(i < islandLines.length){
                scene = scene.concat(islandLines[i]);
            }
            scene = scene.concat("\n");
        }

        return scene;
    }

}
