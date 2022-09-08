enum Semaphore {
    FREE,
    BUSY
}

public class SemaphoreGlobal {
    public static Semaphore semaphore = Semaphore.FREE;
}
