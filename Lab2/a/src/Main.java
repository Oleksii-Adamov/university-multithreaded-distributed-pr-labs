import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        System.out.print("Number of rows = ");
        int numRows = in.nextInt();
        System.out.print("Number of columns = ");
        int numColumns = in.nextInt();
        System.out.print("Number of threads to solve the problem = ");
        int numThreads = in.nextInt();
        boolean[][] forest = new boolean[numRows][numColumns];
        int answerRow = ThreadLocalRandom.current().nextInt(0, numRows);
        int answerColumn = ThreadLocalRandom.current().nextInt(0, numColumns);
        forest[answerRow][answerColumn] = true;
        System.out.println("True answer is: ("+ answerRow + ", " + answerColumn + ")");
        BeeSwarmsManager beeSwarmsManager = new BeeSwarmsManager(forest, numThreads);
        int[] answer = beeSwarmsManager.solve();
        System.out.println("Calculated answer is: (" + answer[0] + ", " + answer[1] + ")");
    }
}