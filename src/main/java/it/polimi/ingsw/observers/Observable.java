package it.polimi.ingsw.observers;

import java.util.ArrayList;

/** This class provides methods for a class to notify changes to all classes implementing
 * {@link it.polimi.ingsw.observers.Observer} registered on the given object.
 */
public abstract class Observable {
    private final ArrayList<Observer> observers  = new ArrayList<>();

    /**
     * Method that notifies all {@link Observer} objects registered on this {@link Observable} of a change.
     */
    public void notifyObservers(){
        for (Observer obs : observers) {
            obs.notify(this);
        }
    }

    /**
     * Method to add an {@link Observer} to this observable object.
     * @param obs {@link Observer} to be added
     */
    public void addObserver(Observer obs){
        observers.add(obs);
    }

    /**
     * Method to remove all observers from this observable.
     */
    public void clearObservers(){
        for(Observer obs : observers){
            observers.remove(obs);
        }
    }
}
