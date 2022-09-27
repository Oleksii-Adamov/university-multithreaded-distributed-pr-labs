import java.io.IOException;

public class FindNamesByNumberRunnable implements Runnable{
    private Database database;

    public FindNamesByNumberRunnable(Database database) {
        this.database = database;
    }

    @Override
    public void run() {
        try {
            List<String> names = database.findNamesByNumber("0123");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
