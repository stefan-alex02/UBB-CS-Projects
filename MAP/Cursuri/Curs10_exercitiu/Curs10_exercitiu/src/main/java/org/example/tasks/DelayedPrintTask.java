package org.example.tasks;

public class DelayedPrintTask extends PrintTask implements Runnable {
    private final long delayInMiliseconds;

    public DelayedPrintTask(String message, long delayInMiliseconds) {
        super(message);
        this.delayInMiliseconds = delayInMiliseconds;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(delayInMiliseconds);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        super.run();
    }
}
