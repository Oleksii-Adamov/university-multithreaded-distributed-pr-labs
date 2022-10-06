import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        int n = 150;
        int[] arr = new int[n];
        for (int i = 0; i < n; i++) {
            int randInt = ThreadLocalRandom.current().nextInt(0, 2);
            if (randInt == 0) {
                arr[i] = -1;
            }
            else {
                arr[i] = 1;
            }
        }

        int numThreads = 3;

        Formation formation = new Formation(arr);

        CheckIntervalRunnable[] runnables = new CheckIntervalRunnable[numThreads];

        int[] splitIndexes = new int[numThreads - 1];

        for (int index = 0, i = 0; i < numThreads; index += 50, i++) {
            runnables[i] = new CheckIntervalRunnable(formation, index, index + 49);
            if (i < numThreads - 1) {
                splitIndexes[i] = index + 49;
            }
        }

        formation.setSplits(splitIndexes, runnables);

        System.out.println("Initial:");
        formation.print();
        System.out.println();

        ExecutorService executorService = Executors.newFixedThreadPool(numThreads);

        for (CheckIntervalRunnable runnable : runnables) {
            executorService.submit(runnable);
        }

        executorService.shutdown();
        executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);

        System.out.println("Stable!");
        formation.print();

    }
}