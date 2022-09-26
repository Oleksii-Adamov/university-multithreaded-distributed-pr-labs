import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

public class FileMonitorRunnable implements Runnable {
    private Garden garden;
    private File file = new File("garden.txt");

    private FileWriter writer;

    public FileMonitorRunnable(Garden garden) {
        this.garden = garden;
        try {
            this.file.createNewFile();
            this.writer = new FileWriter(this.file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                garden.writeMatrixToFile(writer);
            } catch (InterruptedException | IOException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(e);
            }
            System.out.println("Wrote to file!");
        }
    }
}
