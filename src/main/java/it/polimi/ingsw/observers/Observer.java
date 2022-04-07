package it.polimi.ingsw.observers;
/**
 * This interface should be implemented by a class when it wants to be notified of changes in observable objects.
 */
public interface Observer {
    /**
     * This method is called by the {@link Observable#notifyObservers()} method whenever the state of the observed object changes.
     *
     * @param observable the observable object.
     */
    abstract void notify(Observable observable);
}
