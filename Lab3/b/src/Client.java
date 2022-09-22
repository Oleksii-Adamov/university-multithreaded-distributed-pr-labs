import java.util.concurrent.Semaphore;

public class Client {
    public int id;
    public Semaphore binSem = new Semaphore(1);
    public Client(int id) {
        this.id = id;
    }
}
