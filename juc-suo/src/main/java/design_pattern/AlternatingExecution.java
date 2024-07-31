package design_pattern;


import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 交替输出
 *  a
 *  b
 *  c
 */
public class AlternatingExecution {
    static Thread t1;
    static Thread t2;
    static Thread t3;
    public static void main(String[] args) throws InterruptedException {
        // 方法1：ReentrantLock
        AwaitSignal awaitSignal = new AwaitSignal(5);
        Condition a = awaitSignal.newCondition();
        Condition b = awaitSignal.newCondition();
        Condition c = awaitSignal.newCondition();
        new Thread(() -> {
            awaitSignal.print("a", a, b);
        }).start();
        new Thread(() -> {
            awaitSignal.print("b", b, c);
        }).start();
        new Thread(() -> {
            awaitSignal.print("c", c, a);
        }).start();

        Thread.sleep(1000);
        try {
            awaitSignal.lock();
            System.out.println("开始.....");
            a.signal();
        }finally {
            awaitSignal.unlock();
        }

        Thread.sleep(1000);
        System.out.println("------------------------");

        // 方法2：park,unpark
        ParkUnpark parkUnpark = new ParkUnpark(5);
        t1 = new Thread(() ->{
            parkUnpark.print("a", t2);
        });
        t2 = new Thread(() ->{
            parkUnpark.print("b", t3);
        });
        t3 = new Thread(() ->{
            parkUnpark.print("c", t1);
        });
        t1.start();
        t2.start();
        t3.start();
        Thread.sleep(1000);
        LockSupport.unpark(t1);
    }
}

class AwaitSignal extends ReentrantLock{
    private int loopNumber;

    public AwaitSignal(int loopNumber) {
        this.loopNumber = loopNumber;
    }

    public void print(String str, Condition current, Condition next){
        for (int i = 0; i < loopNumber; i++) {
            try {
                lock();
                current.await();
                System.out.println(str);
                next.signal();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                unlock();
            }
        }
    }
}

class ParkUnpark{
    private Integer loopNumber;

    public ParkUnpark(Integer loopNumber) {
        this.loopNumber = loopNumber;
    }

    public void print(String str, Thread next){
        for (int i = 0; i < loopNumber; i++) {
            LockSupport.park();
            System.out.println(str);
            LockSupport.unpark(next);
        }
    }
}
