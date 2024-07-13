import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

@Slf4j
public class CreateThread {
    public static void main(String[] args) throws Exception {
        // 方法1 重新Thread的run方法
        Thread t1 = new Thread(){
            @Override
            public void run() {
                log.info("1:running....");
            }
        };
        t1.setName("t1");
        t1.start();

        // 方法2 Runnable 重写Runnable中的run方法
        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                log.info("2:running....");
            }
        }, "t2");
        // lambda
        t2 = new Thread(() -> log.info("2:running...."), "t2");

        // 方法3 FutureTask + Callable
        FutureTask<Integer> task = new FutureTask<>(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                log.info("3:running....");
                Thread.sleep(1000);
                return 1;
            }
        });
        Thread t3 = new Thread(task, "t3");
        t3.start();
        log.info("{}", task.get());
        // lambda
        task = new FutureTask<>(() ->{
            log.info("3:running....");
            return 1;
        });
    }
}
