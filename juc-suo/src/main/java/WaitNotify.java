import lombok.extern.slf4j.Slf4j;

// 保护性暂停
@Slf4j
public class WaitNotify {
    public static void main(String[] args) {
        GuardObject guardObject = new GuardObject();
        new Thread(() -> {
            log.info("等待结果中.....");
            Object response = guardObject.get(1100);
            log.info("response:{}", response);
        }, "t1").start();

        new Thread(() -> {
            log.info("产生结果中.....");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            guardObject.complete("123");
        }, "t2").start();
    }
}
class GuardObject{
    private Object response;

    // 获取结果
    public Object get(long waitTime){
        synchronized (this){
            long begin = System.currentTimeMillis();
            long totalTime = 0;
            while(response == null){
                long time = waitTime - totalTime;
                if(time <= 0) break;
                try {
                    this.wait(time);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                totalTime = System.currentTimeMillis() - begin;
            }
            return response;
        }
    }

    // 产生结果
    public void complete(Object response){
        synchronized (this){
            this.response = response;
            this.notifyAll();
        }
    }
}
