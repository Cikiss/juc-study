import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class NewScheduledThreadPool {
    public static void main(String[] args) {
        ScheduledExecutorService pool = Executors.newScheduledThreadPool(2);

        // 延时执行
        pool.schedule(() -> {
            System.out.println("task1");
        }, 1, TimeUnit.SECONDS);
        pool.schedule(() -> {
            System.out.println("task2");
        }, 1, TimeUnit.SECONDS);

        // 循环执行

        // 1.周期循环
        pool.scheduleAtFixedRate(() -> {
            System.out.println("running...");
        }, 1, 1, TimeUnit.SECONDS);
        // 2.固定延时时间循环
        pool.scheduleWithFixedDelay(() -> {
            System.out.println("running...");
        }, 1, 1, TimeUnit.SECONDS);

        /**
         * 处理异常：
         * 1.try catch
         * 2.Callable get
         */
    }
}
