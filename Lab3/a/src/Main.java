import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        // input
        Scanner in = new Scanner(System.in);
        System.out.print("Jar capacity =");
        int jarMaxAmount = in.nextInt();
        System.out.print("Number of bees (threads) =");
        int numberOfThreads = in.nextInt();
        Jar jar = new Jar(jarMaxAmount);
        // starting threads
        for (int i = 0; i < numberOfThreads; i++) {
            Thread thread = new Thread(new BeeRunnable(jar));
            thread.setDaemon(true);
            thread.start();
        }
        Thread bear_thread = new Thread(new BearRunnable(jar));
        bear_thread.setDaemon(true);
        bear_thread.start();
        bear_thread.join();
    }
}