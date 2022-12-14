import javax.swing.*;

public class Main {

    public static int getSpinnerValue(JSpinner spinner) {
        try {
            spinner.commitEdit();
        } catch ( java.text.ParseException e ) { System.err.println(e); }
        return (Integer) spinner.getValue();
    }

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
        slider.setBounds(100, 20, 250, 40);

        JButton btnStart = new JButton("Пуск");
        btnStart.setBounds(10, 10, 80, 40);

        JLabel labelLeftThreadPriority = new JLabel("\"Left(10)\" thead priority");
        labelLeftThreadPriority.setBounds(10, 60, 150, 40);

        JSpinner spinboxLeftThreadPriority = new JSpinner(new SpinnerNumberModel(5, 1, 10, 1));
        spinboxLeftThreadPriority.setBounds(165, 60, 50, 40);

        JLabel labelRightThreadPriority = new JLabel("\"Right(90)\" thead priority");
        labelRightThreadPriority.setBounds(10, 105, 150, 40);

        JSpinner spinboxRightThreadPriority = new JSpinner(new SpinnerNumberModel(5, 1, 10, 1));
        spinboxRightThreadPriority.setBounds(165, 105, 50, 40);

        // defining threads
        Thread leftThread = new Thread(new SliderRunnable(slider, 10));
        leftThread.setDaemon(true);
        Thread rightThread = new Thread(new SliderRunnable(slider, 90));
        rightThread.setDaemon(true);

        // adding change priority listeners to spinners
        spinboxLeftThreadPriority.addChangeListener(new SpinnerChangeThreadPriorityListener(leftThread));
        spinboxRightThreadPriority.addChangeListener(new SpinnerChangeThreadPriorityListener(rightThread));

        // threads start on btn click
        btnStart.addActionListener(e -> {

            // reading thread priorities
            int leftThreadPriority = getSpinnerValue(spinboxLeftThreadPriority);
            int rightThreadPriority = getSpinnerValue(spinboxRightThreadPriority);

            // staring threads
            leftThread.start();
            rightThread.start();

            // setting thread priorities
            leftThread.setPriority(leftThreadPriority);
            rightThread.setPriority(rightThreadPriority);
        });

        // setuping GUI
        panel.add(btnStart);
        panel.add(slider);
        panel.add(spinboxLeftThreadPriority);
        panel.add(labelLeftThreadPriority);
        panel.add(spinboxRightThreadPriority);
        panel.add(labelRightThreadPriority);

        win.setContentPane(panel);
        win.setVisible(true);
    }
}