public class Main {

    public static void main(String[] args) throws InterruptedException {
        Garden garden = new Garden(5, 5);

        Thread natureThread = new Thread(new NatureRunnable(garden));
        natureThread.setDaemon(true);
        natureThread.start();

        Thread ScreenMonitorThread = new Thread(new ScreenMonitorRunnable(garden));
        ScreenMonitorThread.setDaemon(true);
        ScreenMonitorThread.start();

        Thread FileMonitorThread = new Thread(new FileMonitorRunnable(garden));
        FileMonitorThread.setDaemon(true);
        FileMonitorThread.start();

        Thread GardenerThread = new Thread(new GardenerRunnable(garden));
        GardenerThread.setDaemon(true);
        GardenerThread.start();

        natureThread.join();
    }
}