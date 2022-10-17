import java.util.concurrent.*;

public class Harbor {
    private int numDocks;

    private Semaphore docksSem;

    private BlockingQueue<Integer> cargoQueue;

    public Harbor(int numDocks, int capacity, int[] cargoArr) throws InterruptedException {
        this.numDocks = numDocks;
        this.docksSem = new Semaphore(numDocks);
        this.cargoQueue = new ArrayBlockingQueue<>(capacity);
        for (int cargo : cargoArr) {
            cargoQueue.put(cargo);
        }
    }

    public void moor(Ship ship, int numToGive, int numToTake) throws InterruptedException {
        docksSem.acquire();
        System.out.println(ship.getId() + " moored");

        // Thread to put onto ship
        Thread putThread = new Thread(() -> {
            int curNumToGive = numToGive;
            try {
                while (curNumToGive > 0 && !Thread.interrupted()) {
                    Thread.sleep(500);
                    Integer cargo = ship.takeCargo();
                    this.cargoQueue.put(cargo);
                    System.out.println(cargo + " taken from ship " + ship.getId());
                    curNumToGive--;
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(e);
            }
        });

        // Thread to take from ship
        Thread takeThread = new Thread(() -> {
            int curNumToTake = numToTake;
            try {
                while (curNumToTake > 0 && !Thread.interrupted()) {
                    Thread.sleep(500);
                    Integer cargo = this.cargoQueue.take();
                    ship.putCargo(cargo);
                    System.out.println(cargo + " give to ship " + ship.getId());
                    curNumToTake--;
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(e);
            }
        });

        putThread.start();
        takeThread.start();
        putThread.join();
        takeThread.join();

        System.out.println(ship.getId() + " unmoored");
        docksSem.release();
    }
}
