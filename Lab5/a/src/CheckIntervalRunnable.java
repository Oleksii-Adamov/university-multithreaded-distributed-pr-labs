public class CheckIntervalRunnable implements Runnable{
    private Formation formation;
    private int beg;
    private int end;

    private boolean stable = false;

    public boolean isStable() {
        return stable;
    }

    public CheckIntervalRunnable(Formation formation, int beg, int end) {
        this.formation = formation;
        this.beg = beg;
        this.end = end;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted() && !formation.isStable()) {
            stable = true;
            for (int i = beg; i < end; i++) {
                if (formation.getArr()[i] == 1) {
                    if (formation.pairCheckAndAct(i)) {
                        stable = false;
                        i++;
                    }
                }
            }
            try {
                formation.getBarrier().await();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(e);
            }
        }
    }
}
