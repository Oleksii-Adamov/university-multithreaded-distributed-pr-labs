import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;

public class BarberShop {
    private Queue<Client> clientQueue = new LinkedBlockingQueue<Client>();

    public Semaphore barberBinSem = new Semaphore(1);

    synchronized public void addClient(Client client) throws InterruptedException {
        System.out.println("Client " + client.id + " is waiting in queue");
        clientQueue.add(client);
        client.binSem.acquire();
        if (clientQueue.size() == 1) {
            System.out.println("Awake by " + client.id);
            barberBinSem.release();
        }
    }

    public void serveClient() throws InterruptedException {
        if (clientQueue.isEmpty()) {
            System.out.println("Barber is sleeping");
            // was a bug of sleeping and immidietly awake and the sleeping til notify, this while fixed it
            while (clientQueue.isEmpty()) {
                barberBinSem.acquire();
            }
            System.out.println("Barber awake");
        }
        else {
            Client client = clientQueue.remove();
            // awaking client is not necessary
            System.out.println("Serving client " + client.id);
            Thread.sleep(2000);
            System.out.println("Client " + client.id + " served");
            client.binSem.release();
        }
    }
}
