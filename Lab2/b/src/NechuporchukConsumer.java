import java.util.concurrent.BlockingQueue;

public class NechuporchukConsumer implements Runnable {
    private BlockingQueue<Integer> queue;
    private int poisonPill;
    private int sum = 0;

    public int getSum() {
        return sum;
    }

    public NechuporchukConsumer(BlockingQueue<Integer> queue, int poisonPill) {
        this.queue = queue;
        this.poisonPill = poisonPill;
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            try {
                int taken = queue.take();
                if (taken != poisonPill) {
                    sum += taken;
                }
                else {
                    Thread.currentThread().interrupt();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(e);
            }
        }
    }
}
