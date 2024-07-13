import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CommonMethod {

    // 1.run 简单的调用，可多次调用，仍是同步

    // 2.start 启动线程，不可多次调用

    // 3.sleep 进入TIMED_WAITING，睡眠结束进入就绪态，其他线程interrupt可打断睡眠

    // 4.yield 进入就绪态，不一定会暂停，看优先级

    // 5.join 等待线程运行结束，保证线程同步，底层是wait，可设置最大等待时间

    // 6.interrupt 打断阻塞态的线程会抛异常，打断正常运行的线程：将打断状态设为true

    // 7.isInterrupted 判断是否被打断，不会清除打断标记

    // 8.interrupted 判断是否被打断，清除打断标记
    public static void main(String[] args) throws InterruptedException {
        TwoPhaseTermination tpt = new TwoPhaseTermination();
        tpt.start();
        Thread.sleep(3500);
        tpt.stop();
    }
}

@Slf4j
// 两阶段终止
class TwoPhaseTermination{
    private Thread monitor;

    public void start(){
        monitor = new Thread(() -> {
            while(true){
                Thread current = Thread.currentThread();
                if(current.isInterrupted()){
                    log.info("料理后事");
                    break;
                }
                try {
                    Thread.sleep(1000);
                    log.info("执行监控记录");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    // 重新设置打断标记
                    current.interrupt();
                }
            }
        });
        monitor.start();
    }

    public void stop(){
        monitor.interrupt();
    }

}
