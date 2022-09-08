import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ActionButtonStopListener implements ActionListener {

    private ThreadSliderManager threadSliderManager;

    public ActionButtonStopListener(ThreadSliderManager threadSliderManager) {
        this.threadSliderManager = threadSliderManager;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (SemaphoreGlobal.semaphore == Semaphore.BUSY && threadSliderManager.isAlive()) {
            threadSliderManager.stopThread();
            SemaphoreGlobal.semaphore = Semaphore.FREE;
        }
    }
}
