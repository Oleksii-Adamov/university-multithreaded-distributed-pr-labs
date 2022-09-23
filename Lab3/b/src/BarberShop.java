import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class BarberShop {
    private Queue<Client> clientQueue = new LinkedList<>();
    private ReentrantLock barberBinSem = new ReentrantLock();
    Condition queueEmptyCondition = barberBinSem.newCondition();

    synchronized public void addClient(Client client) throws InterruptedException {
        barberBinSem.lock();
        System.out.println("Client " + client.id + " is waiting in queue");
        clientQueue.add(client);
        if (clientQueue.size() == 1) {
            queueEmptyCondition.signal();
        }
        barberBinSem.unlock();
    }

    public void serveClient() throws InterruptedException {
        barberBinSem.lock();
        while (clientQueue.isEmpty()) {
            System.out.println("Barber is sleeping");
            queueEmptyCondition.await();
            System.out.println("Barber awake");
        }
        Client client = clientQueue.remove();
        // awaking client is not necessary
        System.out.println("Serving client " + client.id);
        Thread.sleep(2000);
        System.out.println("Client " + client.id + " served");
        barberBinSem.unlock();
    }
}
