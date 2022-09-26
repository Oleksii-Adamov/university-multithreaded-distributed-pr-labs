import java.util.concurrent.ThreadLocalRandom;

public class NatureRunnable implements Runnable{
    private Garden garden;

    public NatureRunnable(Garden garden) {
        this.garden = garden;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            int i = ThreadLocalRandom.current().nextInt(0, garden.getNumRows());
            int j = ThreadLocalRandom.current().nextInt(0, garden.getNumColumns());
            int val = ThreadLocalRandom.current().nextInt(0, 100);
            try {
                garden.setMatrixCell(i, j, val);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(e);
            }
            System.out.println("Nature changed (" + i + ", " + j + ") cell to " + val);
        }
    }
}
