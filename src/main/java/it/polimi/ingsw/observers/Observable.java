package it.polimi.ingsw.observers;

import java.util.ArrayList;

/** This class provides methods for a class to notify changes to all classes implementing
 * {@link it.polimi.ingsw.observers.Observer} registered on the given object.
 */
public abstract class Observable {
    boolean changed = false;
    ArrayList<Observer> observers  = new ArrayList<>();

    public void notifyObservers(){
        if (changed) {
            for (Observer obs : observers) {
                obs.notify(this);
            }
            clearChanged();
        }
    }

    public void addObserver(Observer obs){
        observers.add(obs);
    }

    public void removeObserver(Observer obs){
        observers.remove(obs);
    }

    public void setChanged(){
        changed = true;
    }

    protected void clearChanged(){
        changed = false;
    }

}
