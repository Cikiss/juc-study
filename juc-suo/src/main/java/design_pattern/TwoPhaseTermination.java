package design_pattern;

// 两阶段终止，优雅停止
public class TwoPhaseTermination {
    public static void main(String[] args) throws InterruptedException {
        Termination termination = new Termination();
        termination.start();
        termination.start();
        termination.start();
        Thread.sleep(3000);
        termination.stop();
    }
}

class Termination{

    private Thread monitorThread;

    private volatile boolean isStop = false;

    private boolean isStart = false;

    public void start(){
        synchronized (this){
            // Balking模式
            if(isStart) return;
            isStart = true;
        }
        monitorThread = new Thread(() -> {
            while(true){
                Thread current = Thread.currentThread();
                if(isStop){
                    System.out.println("料理后事");
                    break;
                }
                try {
                    Thread.sleep(1000);
                    System.out.println("监控");
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        monitorThread.start();
    }

    public void stop(){
        isStop = true;
    }
}
