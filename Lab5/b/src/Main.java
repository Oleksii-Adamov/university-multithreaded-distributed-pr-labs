import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {
    private static void runTask(StringBuffer[] stringArr) throws InterruptedException {
        Resources resources = new Resources(stringArr);

        ExecutorService executorService = Executors.newFixedThreadPool(4);
        for (int index = 0; index < 4; index++) {
            executorService.submit(new ChangeStringRunnable(resources, index));
        }
        executorService.shutdown();
        executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);

        StringBuffer[] finalStringArr = resources.getStringArr();
        System.out.println("Final strings:");
        for (int i = 0; i < finalStringArr.length; i++) {
            System.out.println(stringArr[i].toString());
        }
    }
    public static void main(String[] args) throws InterruptedException {
        System.out.println("Hello world!");
        runTask(new StringBuffer[] {new StringBuffer("ABABA"), new StringBuffer("ABABA"),
                new StringBuffer("ABABA"), new StringBuffer("DDDAA")});
        Thread.sleep(3000);
        runTask(new StringBuffer[] {new StringBuffer("ABABA"), new StringBuffer("ABABA"),
                new StringBuffer("ABDBA"), new StringBuffer("DDDAA")});
    }
}