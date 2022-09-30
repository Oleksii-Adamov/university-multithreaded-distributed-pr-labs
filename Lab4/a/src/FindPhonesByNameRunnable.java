import java.io.IOException;
import java.util.List;

public class FindPhonesByNameRunnable implements Runnable{
    private Database database;

    private String name;

    public FindPhonesByNameRunnable(Database database, String name) {
        this.database = database;
        this.name = name;
    }

    @Override
    public void run() {
        try {
            List<String> phones = database.findPhonesByName(name);
        } catch (IOException | InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }
}
