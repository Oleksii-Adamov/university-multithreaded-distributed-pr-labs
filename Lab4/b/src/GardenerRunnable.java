import java.util.concurrent.ThreadLocalRandom;

public class GardenerRunnable implements Runnable {
    private Garden garden;

    public GardenerRunnable(Garden garden) {
        this.garden = garden;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            int i = ThreadLocalRandom.current().nextInt(0, garden.getNumRows());
            int j = ThreadLocalRandom.current().nextInt(0, garden.getNumColumns());
            try {
                if (garden.isWithered(i, j)) {
                    garden.incMatrixCell(i, j, 10);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(e);
            }
        }
    }
}
