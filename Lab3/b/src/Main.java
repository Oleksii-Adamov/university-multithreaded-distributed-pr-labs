import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Scanner in = new Scanner(System.in);
        System.out.print("Number of clients =");
        int num_clients = in.nextInt();

        BarberShop barberShop = new BarberShop();
        Thread barberThread = new Thread(new BarberRunnable(barberShop));
        barberThread.setDaemon(true);
        barberThread.start();

        Thread[] clientThreads = new Thread[num_clients / 2];
        for (int i = 0; i < num_clients / 2; i++) {
            clientThreads[i] = new Thread(new ClientRunnable(i, barberShop));
            clientThreads[i].setDaemon(true);
            clientThreads[i].start();
        }
        for (int i = 0; i < num_clients / 2; i++) {
            clientThreads[i].join();
        }
        Thread.sleep(8000);
        for (int i = num_clients / 2; i < num_clients; i++) {
            Thread thread = new Thread(new ClientRunnable(i, barberShop));
            thread.setDaemon(true);
            thread.start();
        }
        barberThread.join();
    }
}