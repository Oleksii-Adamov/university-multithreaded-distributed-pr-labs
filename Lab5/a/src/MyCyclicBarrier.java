public class MyCyclicBarrier {
    private Runnable runnable;
    private int numParties;

    private int count = 0;

    private final Object sync = new Object();

    public MyCyclicBarrier(int numParties, Runnable runnable) {
        this.runnable = runnable;
        this.numParties = numParties;
    }

    public void await() throws InterruptedException {
        synchronized (sync) {
            count++;
            if (count == numParties) {
                Thread thread = new Thread(runnable);
                thread.start();
                thread.join();
                count = 0;
                sync.notifyAll();
            }
            else {
                sync.wait();
            }
        }
    }
}
