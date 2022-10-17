import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Ship {

    private int id;

    private BlockingQueue<Integer> cargoQueue;

    public Ship(int id, int capacity, int[] cargoArr) throws InterruptedException {
        this.id = id;
        cargoQueue = new ArrayBlockingQueue<>(capacity);
        for (int cargo : cargoArr) {
            cargoQueue.put(cargo);
        }
    }

    public void putCargo(Integer cargo) throws InterruptedException {
        this.cargoQueue.put(cargo);
    }

    public Integer takeCargo() throws InterruptedException {
        return this.cargoQueue.take();
    }

    public int getId() {
        return id;
    }
}
