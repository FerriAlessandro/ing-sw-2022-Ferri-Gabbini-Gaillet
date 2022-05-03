package it.polimi.ingsw.network;

public class Timer extends Thread{
    private long lastTimestamp = 0;
    private final ClientHandler clientHandler;
    private final Object lockTimestamp = new Object();
    private final Object lockAlive = new Object();
    private boolean alive;

    public Timer(ClientHandler clientHandler){
        this.clientHandler = clientHandler;
        alive = false;
    }

    public void reset(){
        synchronized (lockTimestamp) {
            this.lastTimestamp = System.nanoTime();
        }
    }

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

    public void euthanize(){
        synchronized (lockAlive) {
            alive = false;
        }
    }

    public void revive(){
        synchronized (lockAlive) {
            alive = true;
        }
    }
}
