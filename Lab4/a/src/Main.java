import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) throws InterruptedException, IOException {
        Database database = new Database();
        ExecutorService executorService = Executors.newFixedThreadPool(4);
        // writing
        executorService.submit(new AddEntryRunnable(database, "Oleksii", "+380663129815"));
        executorService.submit(new AddEntryRunnable(database, "Andrew", "+380661234916"));

        // reading
        executorService.submit(new FindNamesByPhoneRunnable(database,"+380663129815"));
        executorService.submit(new FindPhonesByNameRunnable(database,"Andrew"));
        executorService.submit(new FindPhonesByNameRunnable(database,"Oleksii"));

        // writing and reading
        executorService.submit(new RemoveEntryRunnable(database, "Andrew", "+380661234916"));
        executorService.submit(new FindNamesByPhoneRunnable(database, "+380663129815"));
        executorService.submit(new AddEntryRunnable(database, "Kirill", "+380662345679"));

        executorService.shutdown();
        executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
    }
}