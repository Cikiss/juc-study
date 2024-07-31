package design_pattern;

import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import java.util.concurrent.atomic.LongAdder;

public class JUCOther {
    public static void main(String[] args) {
        // 原子数组
        AtomicIntegerArray atomicIntegerArray = new AtomicIntegerArray(new int[6]);

        // 原子更新器
        AtomicReferenceFieldUpdater updater = AtomicReferenceFieldUpdater.newUpdater(zz.class, String.class, "a");

        // 原子累加器：设置多个累加单元
        /**
         * 原理：
         * @Contended注解将Cell分别在不同的CPU缓存行，中间添加padding，解决互相的缓存行失效（CPU为保证一致性，
         * @某个CPU核心更改了数据，其他CPU核心对应的整个缓存行必须失效）
         */
        LongAdder adder = new LongAdder();
//        LongAccumulator accumulator = new LongAccumulator()

    }
}
class zz{
    String a;
}
