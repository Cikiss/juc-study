import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;


public class ReEntrantLock {
    /**
     * ReentrantLock
     * 1.可重入
     * 2.可打断 lock.lock.lockInterruptibly()
     *   如果无竞争，获取到锁
     *   有竞争，进入EntryList，可以被其他线程用interrupt打断，停止等待
     * 3.锁超时 lock.tryLock(timeout, TimeUnit)
     * 4.默认不公平
     */
    private static ReentrantLock lock;
    public static void main(String[] args) {

        // 1.使用方法
        try{
            lock.lock();
        }finally {
            lock.unlock();
        }

        // 2.condition使用方法
        Condition condition1 = lock.newCondition();
        Condition condition2 = lock.newCondition();

        lock.lock();
        try {
            condition1.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // 唤醒condition1中某一个线程
        condition1.signal();
        // 唤醒condition1中所有线程
        condition1.signalAll();
        lock.unlock();
    }
}
