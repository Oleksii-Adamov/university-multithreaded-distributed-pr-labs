import java.util.concurrent.BlockingQueue;

public class IvanovProducer implements Runnable {
    private  BlockingQueue<Integer> queue;
    private int poisonPill;

    int[] staff;

    public IvanovProducer(BlockingQueue<Integer> queue, int poisonPill, int[] staff) {
        this.queue = queue;
        this.poisonPill = poisonPill;
        this.staff = staff;
    }

    @Override
    public void run() {
        try {
            int i = 0;
            for (i = 0; i < staff.length && !Thread.interrupted(); i++) {
                queue.put(staff[i]);
                synchronized (System.out) {
                    System.out.println("Ivanov(1) puts " + staff[i]);
                }
            }
            if (i == staff.length) {
                queue.put(poisonPill);
            }
            synchronized (System.out) {
                System.out.println("Ivanov(1) finished");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }
}
