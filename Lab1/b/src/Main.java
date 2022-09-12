import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        // creating GUI objects
        JFrame win =new JFrame();
        win.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        win.setSize(400, 500);
        JPanel panel = new JPanel();
        panel.setLayout(null);

        JSlider slider = new JSlider(0, 100, 50);
        slider.setPaintTrack(true);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.setMajorTickSpacing(10);
        slider.setMinorTickSpacing(0);
        slider.setBounds(10, 20, 300, 40);

        JButton btnStop1 = new JButton("СТОП 1");
        btnStop1.setBounds(10, 70, 100, 40);

        JButton btnStart1 = new JButton("ПУСК 1");
        btnStart1.setBounds(110, 70, 100, 40);

        JButton btnStop2 = new JButton("СТОП 2");
        btnStop2.setBounds(10, 120, 100, 40);

        JButton btnStart2 = new JButton("ПУСК 2");
        btnStart2.setBounds(110, 120, 100, 40);

        // defining thread managers
        ThreadSliderManager threadSliderManager1 = new ThreadSliderManager(slider, 10, Thread.MIN_PRIORITY);

        ThreadSliderManager threadSliderManager2 = new ThreadSliderManager(slider, 90, Thread.MAX_PRIORITY);

        // defining button listeners
        btnStart1.addActionListener(new ActionButtonStartListener(threadSliderManager1, panel));

        btnStart2.addActionListener(new ActionButtonStartListener(threadSliderManager2, panel));

        btnStop1.addActionListener(new ActionButtonStopListener(threadSliderManager1));

        btnStop2.addActionListener(new ActionButtonStopListener(threadSliderManager2));

        // setuping GUI
        panel.add(btnStop1);
        panel.add(btnStart1);
        panel.add(btnStop2);
        panel.add(btnStart2)`;
        panel.add(slider);

        win.setContentPane(panel);
        win.setVisible(true);
    }
}