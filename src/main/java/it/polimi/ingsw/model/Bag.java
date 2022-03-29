package it.polimi.ingsw.model;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.EnumMap;
import java.io.Serializable;
import java.util.Random;
import java.util.stream.Stream;

import it.polimi.ingsw.model.enumerations.Color;

/**
 * Represents the bag from which students are randomly extracted.
 * @author A.G. Gaillet
 * @version 1.0
 * @serial
 * @see Color
 */
public class Bag implements Serializable{
    private static final long serialVersionUID = -8230183335476058063L;
    private final EnumMap<Color, Integer> numOfStudents;
    private static final int maxStudByColor = 26;
    private static final Random rand = new Random();

    /**
     * This is the constructor of the class. It initializes a full {@link Bag}.
     */
    public Bag(){
        numOfStudents = new EnumMap<>(Color.class){};
        for(Color color : Color.values()){
            numOfStudents.put(color, maxStudByColor);
        }
    }

    /**
     * This method returns an ArrayList of colors representing a given number of randomly extracted students.
     * @param num number of students to be extracted from the {@link Bag}
     * @return An {@link ArrayList} of randomly extracted colors representing students;
     * if num > number of remaining students it returns all remaining students
     */
    public ArrayList<Color> getStudents(int num){
        ArrayList<Color> students = new ArrayList<>();
        Color[] colors = Color.values();
        int count = 0;
        while(count < num && numRemaining()>0){
            Color color = colors[rand.nextInt(colors.length)];
            if(numRemaining(color) > 0){
                count++;
                students.add(color);
                numOfStudents.put(color, numOfStudents.get(color) - 1);
            }
        }
        return students;
    }

    /**
     * This method returns the number of students of a given color remaining in the {@link Bag}.
     * @param color given {@link Color} of students
     * @return number of remaining students of the given {@link Color}
     */
    public int numRemaining(Color color){
        return numOfStudents.get(color);
    }

    /**
     * This method returns the total number of students remaining in the {@link Bag}.
     * @return total number of remaining students
     */
    public int numRemaining(){
        return Stream.of(Color.values())
                .mapToInt(this::numRemaining)
                .sum();
    }

    /**
     * This method adds to the bag the given number of students of the given color.
     * @param num number of students to be added to the {@link Bag}
     * @param color {@link Color} of the students to be added to the {@link Bag}
     * @throws InvalidParameterException when the given number would cause the given color's number of students in the {@link Bag} to be higher than structural maximum
     */
    public void addStudents(int num, Color color) throws InvalidParameterException {
        if(numOfStudents.get(color) + num > maxStudByColor){
            throw new InvalidParameterException();
        }
        numOfStudents.put(color, numOfStudents.get(color) + num);
    }
}
