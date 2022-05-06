package it.polimi.ingsw.network;

/**
 * This class provides a timer {@link Thread} that disconnects the linked {@link ClientHandler} when it runs out.
 * @author A.G. Gaillet
 * @version 1.0
 * @see ClientHandler
 * @see Thread
 */
public class Timer extends Thread{
    private long lastTimestamp = 0;
    private final ClientHandler clientHandler;
    private final Object lockTimestamp = new Object();
    private final Object lockAlive = new Object();
    private boolean alive;

    /**
     * Constructor of the class.
     * @param clientHandler to be linked to this {@link Timer}
     */
    public Timer(ClientHandler clientHandler){
        this.clientHandler = clientHandler;
        alive = false;
    }

    /**
     * Reset the internal time counter.
     */
    public void reset(){
        synchronized (lockTimestamp) {
            this.lastTimestamp = System.nanoTime();
        }
    }

    /**
     * Start counting time and disconnect {@link ClientHandler} when limit elapsed.
     */
    public void run(){
        synchronized (lockAlive) {
            alive = true;
        }
        while(!Thread.currentThread().isInterrupted()){
            synchronized (lockTimestamp) {
                if (lastTimestamp == 0) {
                    lastTimestamp = System.nanoTime();
                } else if (((System.nanoTime() - lastTimestamp) / 1000000000.0 > 3) && alive) {
                    clientHandler.disconnect();
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    /**
     * Suspend check for expired time limit.
     */
    public void euthanize(){
        synchronized (lockAlive) {
            alive = false;
        }
    }

    /**
     * Restart checks for expired time limit.
     * To avoid an immediate disconnection this method should be invoked just after the {@link Timer#reset()} method.
     */
    public void revive(){
        synchronized (lockAlive) {
            alive = true;
        }
    }
}
