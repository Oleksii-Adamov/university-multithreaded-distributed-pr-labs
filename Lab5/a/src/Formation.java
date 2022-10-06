public class Formation {
    private int[] arr;

    private MyCyclicBarrier barrier;

    private CheckBetweenSplitsRunnable checkBetweenSplitsRunnable;

    public Formation(int[] arr) {
        this.arr = arr;
    }

    public void setSplits(int[] splitIndexes, CheckIntervalRunnable[] runnables) {
        this.checkBetweenSplitsRunnable = new CheckBetweenSplitsRunnable(this, splitIndexes, runnables);
        barrier = new MyCyclicBarrier(splitIndexes.length + 1, checkBetweenSplitsRunnable);
    }

    public boolean pairCheckAndAct(int index) {
        if (index < arr.length - 1 && arr[index] == 1 && arr[index + 1] == -1) {
            arr[index] *= -1;
            arr[index + 1] *= -1;
            return true;
        }
        return false;
    }

    public MyCyclicBarrier getBarrier() {
        return barrier;
    }

    public boolean isStable() {
        return checkBetweenSplitsRunnable.isStable();
    }

    public int[] getArr() {
        return arr;
    }

    public void print() {
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + " ");
        }
        System.out.println();
    }
}
