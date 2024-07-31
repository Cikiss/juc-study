import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class ForkJoin {
    public static void main(String[] args) {
        ForkJoinPool forkJoinPool = new ForkJoinPool(4);
        System.out.println(forkJoinPool.invoke(new MyTask(5)));
    }
}


//有返回值
class MyTask extends RecursiveTask<Integer>{

    private int n;

    MyTask(int n) {
        this.n = n;
    }

    @Override
    protected Integer compute() {
        if(n == 1) return 1;
        MyTask t1 = new MyTask(n - 1);
        t1.fork();

        return n + t1.join();
    }
}

// 无返回值
//class MyTask extends RecursiveAction{
//
//}