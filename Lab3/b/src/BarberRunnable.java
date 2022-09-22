public class BarberRunnable implements Runnable{

    private BarberShop barberShop;

    public BarberRunnable(BarberShop barberShop) {
        this.barberShop = barberShop;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                barberShop.barberBinSem.acquire();
                barberShop.serveClient();
                barberShop.barberBinSem.release();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(e);
            }
        }
    }
}
