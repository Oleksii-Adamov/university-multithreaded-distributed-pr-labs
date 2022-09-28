import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Database {
    private File file;

    ReadWriteLock readWriteLock = new ReadWriteLock();

    public Database() throws IOException {
        this.file = new File("database.txt");
        this.file.createNewFile();
    }

    public List<String> findNamesByPhone(String phone) throws IOException, InterruptedException {
        readWriteLock.lockReadLock();
        Thread.sleep(3000);
        List<String> names = new ArrayList<>();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            String currentLine = bufferedReader.readLine();
            while (currentLine != null) {
                String[] parts = currentLine.split(" - ");
                if (parts.length == 2 && Objects.equals(parts[1], phone)) {
                    names.add(parts[0]);
                }
                currentLine = bufferedReader.readLine();
            }
        }
        if(names.isEmpty()) {
            System.out.println("Not Found name by " + phone);
        }
        for (String name : names) {
            System.out.println("Found " + name + " by " + phone);
        }
        readWriteLock.unlockReadLock();
        return names;
    }

    public List<String> findPhonesByName(String name) throws IOException, InterruptedException {
        readWriteLock.lockReadLock();
        Thread.sleep(3000);
        List<String> phones = new ArrayList<>();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            String currentLine = bufferedReader.readLine();
            while (currentLine != null) {
                String[] parts = currentLine.split(" - ");
                if (parts.length == 2 && Objects.equals(parts[0], name)) {
                    phones.add(parts[1]);
                }
                currentLine = bufferedReader.readLine();
            }
        }
        if(phones.isEmpty()) {
            System.out.println("Not Found phone by " + name);
        }
        for (String phone : phones) {
            System.out.println("Found " + phone + " by " + name);
        }
        readWriteLock.unlockReadLock();
        return phones;
    }

    public void addEntry(String name, String phone) throws IOException, InterruptedException {
        readWriteLock.lockWriteLock();
        Thread.sleep(3000);
        try (FileWriter writer = new FileWriter(file, true)) {
            writer.write(name + " - " + phone + "\n");
        }
        System.out.println("Added " + name + " - " + phone);
        readWriteLock.unlockWriteLock();
    }

    public void removeEntry(String name, String phone) throws IOException, InterruptedException {
        readWriteLock.lockWriteLock();
        Thread.sleep(3000);
        List<String> entriesToWrite = new ArrayList<>();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            String currentLine = bufferedReader.readLine();
            while (currentLine != null) {
                String[] parts = currentLine.split(" - ");
                if (!(Objects.equals(parts[0], name) && Objects.equals(parts[1], phone))) {
                    entriesToWrite.add(currentLine);
                }
                currentLine = bufferedReader.readLine();
            }
        }
        try (FileWriter writer = new FileWriter(file, false)) {
            for (String entry : entriesToWrite) {
                writer.write(entry + "\n");
            }
        }
        System.out.println("Removed " + name + " - " + phone);
        readWriteLock.unlockWriteLock();
    }

}
