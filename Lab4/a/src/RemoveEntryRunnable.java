import java.io.IOException;

public class RemoveEntryRunnable implements Runnable {
    private Database database;
    private String name;
    private String phone;

    public RemoveEntryRunnable(Database database, String name, String phone) {
        this.database = database;
        this.name = name;
        this.phone = phone;
    }

    @Override
    public void run() {
        try {
            database.removeEntry(name, phone);
        } catch (IOException | InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }
}
