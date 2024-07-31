import java.util.concurrent.atomic.AtomicMarkableReference;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicStampedReference;

public class JUCAtomicReference {

    public static void main(String[] args) {
        AtomicReference<Integer> atomicReference = new AtomicReference<>();

        // ABA问题：无法感知 A -> B -> A


        // 解决
        // AtomicStampedReference(可以知道修改过几次), AtomicMarkableReference(只能知道是否修改过)
    }
}
