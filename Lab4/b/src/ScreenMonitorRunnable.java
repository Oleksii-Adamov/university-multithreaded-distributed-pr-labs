import java.util.concurrent.ThreadLocalRandom;

public class ScreenMonitorRunnable implements Runnable{
    private Garden garden;

    public ScreenMonitorRunnable(Garden garden) {
        this.garden = garden;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                garden.printMatrix();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(e);
            }
        }
    }
}
