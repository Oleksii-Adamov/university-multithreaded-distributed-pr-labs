public class Jar {
    private BinarySemaphore bearBinSem = new BinarySemaphore();

    private BinarySemaphore beeBinSem = new BinarySemaphore();

    private int maxAmount;

    private volatile int curAmount = 0;

    public Jar(int maxAmount) throws InterruptedException {
        this.maxAmount = maxAmount;
        bearBinSem.Lock();
    }

    public void eatHoney() throws InterruptedException {
        bearBinSem.Lock();
        System.out.println("Bear starts eating...");
        curAmount = 0;
        Thread.sleep(3000);
        System.out.println("Bear ate all honey!");
        beeBinSem.Unlock();
    }

    public void addHoney() throws InterruptedException {
        beeBinSem.Lock();
        Thread.sleep(500);
        System.out.println("Jar cur amount: " + (curAmount + 1));
        curAmount++;
        if (curAmount == maxAmount) {
            System.out.println("Jar is full!");
            bearBinSem.Unlock();
        }
        else {
            beeBinSem.Unlock();
        }
    }
}
