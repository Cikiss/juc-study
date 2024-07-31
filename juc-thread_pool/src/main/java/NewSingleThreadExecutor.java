import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * public static ExecutorService newSingleThreadExecutor() {
 *         return new FinalizableDelegatedExecutorService
 *             (new ThreadPoolExecutor(1, 1,
 *                                     0L, TimeUnit.MILLISECONDS,
 *                                     new LinkedBlockingQueue<Runnable>()));
 *     }
 * 多个线程排队执行，线程数固定为1（装饰器模式，不可以修改线程数量）
 */
public class NewSingleThreadExecutor {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
    }
}
