import java.util.concurrent.BrokenBarrierException;

public class ChangeStringRunnable implements Runnable{
    private Resources resources;

    private int index;

    public ChangeStringRunnable(Resources resources, int index) {
        this.resources = resources;
        this.index = index;
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            try {
                if (resources.changeString(index)) {
                    break;
                }
            } catch (BrokenBarrierException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(e);
            }
        }
    }
}
