public class CheckBetweenSplitsRunnable implements Runnable{
    private boolean stable = false;
    private Formation formation;
    private int[] splitIndexes;

    private CheckIntervalRunnable[] runnables;

    public CheckBetweenSplitsRunnable(Formation formation, int[] splitIndexes, CheckIntervalRunnable[] runnables) {
        this.formation = formation;
        this.splitIndexes = splitIndexes;
        this.runnables = runnables;
    }

    @Override
    public void run() {
        stable = true;
        for (int index : splitIndexes) {
            if (formation.pairCheckAndAct(index)) {
                stable = false;
            }
        }
        for (int i = 0; i < runnables.length && stable; i++) {
            stable = stable & runnables[i].isStable();
        }
        formation.print();
    }

    public boolean isStable() {
        return stable;
    }
}
