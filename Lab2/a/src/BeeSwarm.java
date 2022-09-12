public class BeeSwarm implements Runnable {
    private boolean[][] matrix;
    private BeeSwarmsManager manager;
    BeeSwarm(boolean[][] matrix, BeeSwarmsManager manager) {
        this.matrix = matrix;
        this.manager = manager;
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            int rowIndex = manager.getRowIndex();
            if (rowIndex == -1) {
                Thread.currentThread().interrupt();
            }
            else {
                int numColumns = matrix[rowIndex].length;
                System.out.println("Thread " + Thread.currentThread().getName() + " at row " + rowIndex);
                for (int j = 0; j < numColumns; j++) {
                    if (matrix[rowIndex][j]) {
                        System.out.println("Thread " + Thread.currentThread().getName() + " found answer at row " + rowIndex);
                        manager.answerFound(rowIndex, j);
                        break;
                    }
                }
                System.out.println("Thread " + Thread.currentThread().getName() + " finished row " + rowIndex);
            }
        }
        System.out.println("Thread " + Thread.currentThread().getName() + " is interrupted");
    }
}
