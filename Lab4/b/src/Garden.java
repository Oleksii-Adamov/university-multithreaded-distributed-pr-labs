import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Garden {
    private int numRows;
    private int numColumns;
    private int[][] matrix;

    private ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    private Lock readLock = readWriteLock.readLock();

    private Lock writeLock = readWriteLock.writeLock();

    public Garden(int numRows, int numColumns) {
        this.numRows = numRows;
        this.numColumns = numColumns;
        this.matrix = new int[numRows][numColumns];
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numColumns; j++) {
                matrix[i][j] = ThreadLocalRandom.current().nextInt(0, 100);
            }
        }
    }

    public int getNumRows() {
        return numRows;
    }

    public int getNumColumns() {
        return numColumns;
    }

    public void setMatrixCell(int i, int j, int val) throws InterruptedException {
        writeLock.lock();
        matrix[i][j] = val;
        Thread.sleep(3000);
        System.out.println("Nature changed (" + i + ", " + j + ") cell to " + val);
        writeLock.unlock();
    }

    public boolean isWithered(int i, int j) {
        readLock.lock();
        boolean ret = matrix[i][j] < 50;
        readLock.unlock();
        return ret;
    }

    public void incMatrixCell(int i, int j, int incVal) throws InterruptedException {
        writeLock.lock();
        matrix[i][j] += incVal;
        Thread.sleep(3000);
        System.out.println("Gardener increased (" + i + ", " + j + ") cell by " + incVal);
        writeLock.unlock();
    }

    public void printMatrix() throws InterruptedException {
        readLock.lock();
        Thread.sleep(3000);
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numColumns; j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }
        readLock.unlock();
    }

    public void writeMatrixToFile(FileWriter writer) throws IOException, InterruptedException {
        readLock.lock();
        Thread.sleep(3000);
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numColumns; j++) {
                writer.write(matrix[i][j] + " ");
            }
            writer.write("\n");
        }
        System.out.println("Wrote to file!");
        readLock.unlock();
    }
}
