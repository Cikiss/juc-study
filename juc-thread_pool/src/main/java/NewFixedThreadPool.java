import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * public static ExecutorService newFixedThreadPool(int nThreads, ThreadFactory threadFactory) {
 *         return new ThreadPoolExecutor(nThreads, nThreads,
 *                                       0L, TimeUnit.MILLISECONDS,
 *                                       new LinkedBlockingQueue<Runnable>(),
 *                                       threadFactory);
 *     }
 * 核心线程数=最大线程数，阻塞队列无界（可放任意数量任务）
 */
public class NewFixedThreadPool {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(2, new ThreadFactory() {
            private AtomicInteger t = new AtomicInteger(1);

            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, "thread" + t.getAndIncrement());
            }
        });
    }
}
