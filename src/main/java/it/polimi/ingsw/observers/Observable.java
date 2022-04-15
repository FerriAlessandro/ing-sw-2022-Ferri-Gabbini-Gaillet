package it.polimi.ingsw.observers;

import java.util.ArrayList;

/** This class provides methods for a class to notify changes to all classes implementing
 * {@link it.polimi.ingsw.observers.Observer} registered on the given object.
 */
public abstract class Observable {
    boolean changed = false;
    ArrayList<Observer> observers  = new ArrayList<>();

    /**
     * Method that notifies all {@link Observer} objects registered on this {@link Observable} of a change.
     */
    public void notifyObservers(){
        if (changed) {
            for (Observer obs : observers) {
                obs.notify(this);
            }
            clearChanged();
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
     * Method to remove an {@link Observer} from this observable object.
     * @param obs {@link Observer} to be removed
     */
    public void removeObserver(Observer obs){
        observers.remove(obs);
    }

    /**
     * Method to set the internal changed flag
     */
    public void setChanged(){
        changed = true;
    }

    /**
     * Method to reset the internal changed flag
     */
    protected void clearChanged(){
        changed = false;
    }

}
