public class BeeSwarmsManager {
    private volatile boolean isWorkDone = false;

    private int[] answer = new int[2];

    private int numberOfThreads;

    private ThreadGroup threadGroup = new ThreadGroup("BeeSwarm thread group");
    private volatile int nextRowIndex = 0;

    private boolean[][] matrix;

    public int getNumberOfThreads() {
        return numberOfThreads;
    }

    public void setNumberOfThreads(int numberOfThreads) {
        this.numberOfThreads = numberOfThreads;
    }

    public boolean[][] getMatrix() {
        return matrix;
    }

    public void setMatrix(boolean[][] matrix) {
        this.matrix = matrix;
    }

    public BeeSwarmsManager(boolean[][] matrix, int numberOfThreads) {
        this.matrix = matrix;
        this.numberOfThreads = numberOfThreads;
    }

    public int[] solve() {
        this.startThreads();
        while (!isWorkDone) {
            try {
                Thread.sleep(1);
            }
            catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(e);
            }
        }
        // sleep for debugging/demo purposes to see that threads interrupted (without sleep main could finish faster,
        // and end others as Deamons
        try {
            Thread.sleep(1000);
        }
        catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
        return this.answer;
    }

    public synchronized int getRowIndex() {
        if (!isWorkDone && this.nextRowIndex < this.matrix.length) {
            return this.nextRowIndex++;
        }
        else {
            return -1;
        }
    }

    public synchronized void answerFound(int rowIndex, int columnIndex) {
        this.threadGroup.interrupt();
        this.answer[0] = rowIndex;
        this.answer[1] = columnIndex;
        this.isWorkDone = true;
    }

    private void startThreads() {
        for (int i = 0; i < this.numberOfThreads; i++) {
            Thread thread = new Thread(this.threadGroup, new BeeSwarm(this.matrix, this));
            thread.setDaemon(true);
            thread.start();
        }
    }
}
