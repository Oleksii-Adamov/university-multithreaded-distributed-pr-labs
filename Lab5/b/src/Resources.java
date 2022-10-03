import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ThreadLocalRandom;

public class Resources {

    private StringBuffer[] stringArr;

    public StringBuffer[] getStringArr() {
        return stringArr;
    }

    private CheckTerminateRunnable checkTerminateRunnable;

    private CyclicBarrier barrier;

    public Resources(StringBuffer[] stringArr) {
        this.stringArr = stringArr;
        System.out.println("Initial strings:");
        for (int i = 0; i < stringArr.length; i++) {
            System.out.println(stringArr[i].toString());
        }
        checkTerminateRunnable = new CheckTerminateRunnable(stringArr);
        barrier = new CyclicBarrier(stringArr.length, checkTerminateRunnable);
    }

    public boolean changeString(int index) throws BrokenBarrierException, InterruptedException {

        barrier.await();

        if (checkTerminateRunnable.isToTermianate()) {
            return true;
        }

        int randIndex = ThreadLocalRandom.current().nextInt(0, stringArr[index].length());

        if (stringArr[index].charAt(randIndex) == 'A') {
            stringArr[index].setCharAt(randIndex, 'C');
            System.out.println("Thread " + index + " changed A -> C");
        }
        else if (stringArr[index].charAt(randIndex) == 'C') {
            stringArr[index].setCharAt(randIndex, 'A');
            System.out.println("Thread " + index + " changed C -> A");
        }
        else if (stringArr[index].charAt(randIndex) == 'B') {
            stringArr[index].setCharAt(randIndex, 'D');
            System.out.println("Thread " + index + " changed B -> D");
        }
        else if (stringArr[index].charAt(randIndex) == 'D') {
            stringArr[index].setCharAt(randIndex, 'B');
            System.out.println("Thread " + index + " changed D -> B");
        }

        return false;
    }
}
