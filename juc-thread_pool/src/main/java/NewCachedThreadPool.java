import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

/**
 * public static ExecutorService newCachedThreadPool() {
 *         return new ThreadPoolExecutor(0, Integer.MAX_VALUE,
 *                                       60L, TimeUnit.SECONDS,
 *                                       new SynchronousQueue<Runnable>());
 *     }
 *
 * SynchronousQueue：无容量，有线程来取才能put进去
 * 核心线程数为0（全部为救急线程），最大线程数为Integer.MAX_VALUE，生存时间为60s
 */
public class NewCachedThreadPool {
    public static void main(String[] args) throws Exception {
        ExecutorService pool = Executors.newCachedThreadPool();

        // submit task
        Future<String> future = pool.submit(() -> {
            System.out.println("begin");
            Thread.sleep(500);
            return "1";
        });

        // invokeAll tasks
        List<Future<String>> futures = pool.invokeAll(Arrays.asList(
                () -> {
                    System.out.println("begin");
                    Thread.sleep(500);
                    return "1";
                },() -> {
                    System.out.println("begin");
                    Thread.sleep(500);
                    return "2";
                }
        ));
        futures.forEach(f -> {
            try {
                System.out.println(f.get());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        // invokeAny：返回最先得到结果的之后结束

        // shutdown：不会接受新任务，执行完已提交的和队列中的任务，不会阻塞调用线程的执行

        // shutdownNow：将队列中的任务返回，打断正在执行的任务

        /**
         * 线程池大小：
         * CPU密集型：cpu核数 + 1
         * IO密集型：线程数 = cpu核数 * 期望cpu利用率 * 总时间（cpu计算时间 + 等待时间） / cpu计算时间
         */


    }
}
