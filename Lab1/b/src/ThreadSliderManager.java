import javax.swing.*;

public class ThreadSliderManager {

    private Thread thread;

    public Thread getThread() {
        return thread;
    }

    private JSlider slider;

    private int priority;

    private int sliderValue;

    public ThreadSliderManager(JSlider slider, int sliderValue, int priority) {
        this.slider = slider;
        this.sliderValue = sliderValue;
        this.priority = priority;
    }

    public void startThread() {
        this.thread = new Thread(new SliderRunnable(slider, sliderValue));
        thread.setDaemon(true);
        thread.start();
        thread.setPriority(priority);
    }

    public void stopThread() {
        this.thread.interrupt();
    }

    public boolean isAlive() {
        if (thread != null) {
            return thread.isAlive();
        }
        else {
            return false;
        }
    }
}
