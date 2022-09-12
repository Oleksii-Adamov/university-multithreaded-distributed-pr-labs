import java.util.concurrent.BlockingQueue;

public class PetrovProducerConsumer implements Runnable {
    private BlockingQueue<Integer> queueToTake;
    private BlockingQueue<Integer> queueToPut;
    private int poisonPillToConsume;
    private int poisonPillToProduce;

    public PetrovProducerConsumer(BlockingQueue<Integer> queueToTake, BlockingQueue<Integer> queueToPut,
                                  int poisonPillToConsume, int poisonPillToProduce) {
        this.queueToTake = queueToTake;
        this.queueToPut = queueToPut;
        this.poisonPillToConsume = poisonPillToConsume;
        this.poisonPillToProduce = poisonPillToProduce;
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            try {
                int taken = queueToTake.take();
                if (taken != poisonPillToConsume) {
                    queueToPut.put(taken);
                }
                else {
                    queueToPut.put(poisonPillToProduce);
                    Thread.currentThread().interrupt();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(e);
            }
        }
    }
}
