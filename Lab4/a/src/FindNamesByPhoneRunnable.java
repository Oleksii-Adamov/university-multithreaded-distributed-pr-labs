import java.io.IOException;
import java.util.List;

public class FindNamesByPhoneRunnable implements Runnable{
    private Database database;

    private String phone;

    public FindNamesByPhoneRunnable(Database database, String phone) {
        this.database = database;
        this.phone = phone;
    }

    @Override
    public void run() {
        try {
            List<String> names = database.findNamesByPhone(phone);
//            for (String name : names) {
//                System.out.println("Found " + name + " by " + phone);
//            }
        } catch (IOException | InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }
}
