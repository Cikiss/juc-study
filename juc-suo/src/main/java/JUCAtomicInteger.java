import java.util.concurrent.atomic.AtomicInteger;

public class JUCAtomicInteger {

    public static void main(String[] args) {
        AtomicInteger i = new AtomicInteger(0);

        // ++ i
        i.incrementAndGet();
        // i ++
        i.getAndIncrement();

        /**
         * int pre, next;
         * do {
         *     pre = get();
         *     next = updateFunction.applyAsInt(pre);
         * }while(!compareAndSet(pre, next));
         * return next;
         */
        i.updateAndGet(value -> value * 10);

    }
}
