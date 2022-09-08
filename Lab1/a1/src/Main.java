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
        slider.setBounds(100, 20, 250, 40);

        JButton btn = new JButton("Пуск");
        btn.setBounds(10, 10, 80, 40);

        JLabel labelLeftThreadPriority = new JLabel("\"Left(10)\" thead priority");
        labelLeftThreadPriority.setBounds(10, 60, 150, 40);

        JSpinner spinboxLeftThreadPriority = new JSpinner(new SpinnerNumberModel(5, 1, 10, 1));
        spinboxLeftThreadPriority.setBounds(165, 60, 50, 40);

        JLabel labelRightThreadPriority = new JLabel("\"Right(90)\" thead priority");
        labelRightThreadPriority.setBounds(10, 105, 150, 40);

        JSpinner spinboxRightThreadPriority = new JSpinner(new SpinnerNumberModel(5, 1, 10, 1));
        spinboxRightThreadPriority.setBounds(165, 105, 50, 40);

        Thread leftThread = new Thread(new SliderRunnable(slider, 10));
        leftThread.setDaemon(true);
        Thread rightThread = new Thread(new SliderRunnable(slider, 90));
        rightThread.setDaemon(true);
        // threads start on btn click
        btn.addActionListener(e -> {

            // reading thread priorities
            int leftThreadPriority = getSpinnerValue(spinboxLeftThreadPriority);
            int rightThreadPriority = getSpinnerValue(spinboxRightThreadPriority);

            // setting thread priorities
            leftThread.setPriority(leftThreadPriority);
            rightThread.setPriority(rightThreadPriority);

            // staring threads
            leftThread.start();
            rightThread.start();
        });

        // setuping GUI
        panel.add(btn);
        panel.add(slider);
        panel.add(spinboxLeftThreadPriority);
        panel.add(labelLeftThreadPriority);
        panel.add(spinboxRightThreadPriority);
        panel.add(labelRightThreadPriority);

        win.setContentPane(panel);
        win.setVisible(true);
    }
}