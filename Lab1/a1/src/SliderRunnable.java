import javax.swing.*;

public class SliderRunnable implements Runnable {
    private final JSlider slider;
    private final int value;

    public SliderRunnable(JSlider slider, int value) {
        this.slider = slider;
        this.value = value;
    }
    public void run() {
        while (!Thread.interrupted()) {
            synchronized (slider) {
                slider.setValue(value);
            }
        }
    }
}
