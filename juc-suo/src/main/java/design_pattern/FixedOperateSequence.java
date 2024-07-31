package design_pattern;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.LockSupport;


// 固定顺序运行 先2后1
@Slf4j
public class FixedOperateSequence {
    public static void main(String[] args) {
        Thread t1 = new Thread(() -> {
            LockSupport.park();
            log.info("t1");
        }, "t1");

        Thread t2 = new Thread(() -> {
            log.info("t2");
            LockSupport.unpark(t1);
        }, "t2");

        t1.start();
        t2.start();
    }
}
