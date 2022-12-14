public class ReadWriteLock {
    private volatile boolean writeLocked = false;

    private volatile int readers = 0;

    private final Object syncObj = new Object();

    public void lockReadLock() throws InterruptedException {
        synchronized (syncObj) {
            while (writeLocked) {
                syncObj.wait();
            }
            readers++;
        }
    }

    public void unlockReadLock() {
        synchronized (syncObj) {
            readers--;
            if (readers == 0) {
                syncObj.notifyAll();
            }
        }
    }

    public void lockWriteLock() throws InterruptedException {
        synchronized (syncObj) {
            while (writeLocked || readers > 0) {
                syncObj.wait();
            }
            writeLocked = true;
        }
    }

    public void unlockWriteLock() {
        synchronized (syncObj) {
            writeLocked = false;
            syncObj.notifyAll();
        }
    }
}
