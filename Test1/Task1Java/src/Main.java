import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(4);
        Harbor harbor = new Harbor(2, 10, new int[] {1, 2, 3, 4, 5});
        executorService.submit(new ShipRunnable(harbor, new Ship(1, 2, new int[] {6, 7}), 2, 2));
        executorService.submit(new ShipRunnable(harbor, new Ship(2, 3, new int[] {8, 9, 10}), 2, 1));
        executorService.submit(new ShipRunnable(harbor, new Ship(3, 2, new int[] {11, 12}), 2, 2));

        executorService.shutdown();
        executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
    }
}