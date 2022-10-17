public class ShipRunnable implements Runnable {

    private Harbor harbor;
    private Ship ship;

    private int numToGive;
    private int numToTake;

    public ShipRunnable(Harbor harbor, Ship ship, int numToGive, int numToTake) {
        this.harbor = harbor;
        this.ship = ship;
        this.numToGive = numToGive;
        this.numToTake = numToTake;
    }

    @Override
    public void run() {
        try {
            harbor.moor(ship, numToGive, numToTake);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }
}
