public class BinarySemaphore {
    private Object lock = new Object();
    private volatile boolean is_locked = false;

    void Lock() throws InterruptedException {
        synchronized (lock) {
            while (is_locked) {
                lock.wait();
            }
            is_locked = true;
        }
    }

    void Unlock() {
        synchronized (lock) {
            is_locked = false;
            lock.notify();
        }
    }
}
