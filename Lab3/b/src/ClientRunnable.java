public class ClientRunnable implements Runnable{

    private Client client;

    private BarberShop barberShop;

    public ClientRunnable(int id, BarberShop barberShop) {
        this.client = new Client(id);
        this.barberShop = barberShop;
    }

    @Override
    public void run() {
        try {
            barberShop.addClient(client);
        }
        catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }
}
