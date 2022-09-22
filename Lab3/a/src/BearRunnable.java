public class BearRunnable implements Runnable{
    private Jar jar;

    public BearRunnable(Jar jar) {
        this.jar = jar;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                jar.eatHoney();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(e);
            }
        }
    }
}
