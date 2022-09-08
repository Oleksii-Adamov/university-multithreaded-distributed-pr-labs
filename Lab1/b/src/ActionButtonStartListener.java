import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ActionButtonStartListener implements ActionListener {

    private ThreadSliderManager threadSliderManager;
    private JPanel panel;

    public ActionButtonStartListener(ThreadSliderManager threadSliderManager, JPanel panel) {
        this.threadSliderManager = threadSliderManager;
        this.panel = panel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (SemaphoreGlobal.semaphore == Semaphore.FREE) {
            SemaphoreGlobal.semaphore = Semaphore.BUSY;
            threadSliderManager.startThread();
        }
        else {
            JOptionPane.showMessageDialog(this.panel, "Зайнято потоком");
        }
    }
}
