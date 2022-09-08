import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class SpinnerChangeThreadPriorityListener implements ChangeListener {
    private Thread thread;
    public SpinnerChangeThreadPriorityListener(Thread thread) {
        this.thread = thread;
    }
    @Override
    public void stateChanged(ChangeEvent e) {
        JSpinner source_spinner = (JSpinner) e.getSource();
        thread.setPriority((Integer) source_spinner.getValue());
    }
}
