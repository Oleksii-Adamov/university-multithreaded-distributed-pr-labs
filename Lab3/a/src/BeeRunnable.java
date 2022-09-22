public class BeeRunnable implements Runnable{
    private Jar jar;

    public BeeRunnable(Jar jar) {
        this.jar = jar;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                jar.addHoney();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(e);
            }
        }
    }
}
