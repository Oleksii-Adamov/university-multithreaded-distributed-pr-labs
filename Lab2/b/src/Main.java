import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadLocalRandom;

public class Main {
    private static int soldiersPipeline(int[] staff, int maxQueueSize) {
        BlockingQueue<Integer> queue1 = new LinkedBlockingQueue<>(maxQueueSize);
        BlockingQueue<Integer> queue2 = new LinkedBlockingQueue<>(maxQueueSize);

        Thread ivanovThread = new Thread(new IvanovProducer(queue1, -1,staff));
        ivanovThread.setDaemon(true);
        ivanovThread.start();

        Thread petrovThread = new Thread(new PetrovProducerConsumer(queue1, queue2, -1, -1));
        petrovThread.setDaemon(true);
        petrovThread.start();

        NechuporchukConsumer nechuporchukThread = new NechuporchukConsumer(queue2, -1);
        nechuporchukThread.setDaemon(true);
        nechuporchukThread.start();

        try {
            nechuporchukThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
        return nechuporchukThread.getSum();
    }
    public static void main(String[] args) {
        // input
        Scanner in = new Scanner(System.in);
        System.out.print("Staff array size = ");
        int size = in.nextInt();
        System.out.print("Max queue size = ");
        int maxQueueSize = in.nextInt();
        // fill staff array with random ints from [0,99]
        int[] staff = new int[size];
        int sum = 0;
        for (int i = 0; i < size; i++) {
            staff[i] = ThreadLocalRandom.current().nextInt(0, 100);
            sum += staff[i];
        }
        System.out.println("Actual sum = " + sum);

        int computedSum = soldiersPipeline(staff, maxQueueSize);
        System.out.println("Computed sum = " + computedSum);
    }
}