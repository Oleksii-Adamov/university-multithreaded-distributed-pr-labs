import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Database {
    private File file;

    private FileReader reader;

    ReadWriteLock readWriteLock = new ReadWriteLock();

    public Database() throws IOException {
        this.file = new File("database.txt");
        this.file.createNewFile();
        reader = new FileReader(file);
        FileWriter writer = new FileWriter(file);
        writer.write("A - 0123");
        writer.write("B - 1234");
        writer.close();
    }

    public List<String> findNamesByNumber(String number) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(reader);
        String currentLine = bufferedReader.readLine();
        List<String> names = new ArrayList<String>();
        while (currentLine != null) {
            String[] parts = currentLine.split(" - ");
            if (parts[1] == number) {
                names.add(parts[0]);
            }
            currentLine = bufferedReader.readLine();
        }
        bufferedReader.close();
        return names;
    }

}
