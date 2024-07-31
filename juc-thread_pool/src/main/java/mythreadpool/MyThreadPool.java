package mythreadpool;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
public class MyThreadPool {
    public static void main(String[] args) {
        ThreadPool threadPool = new ThreadPool(1, 1000, TimeUnit.MICROSECONDS,
                1, ((queue, task) -> {
//                 * 1.死等
//                    queue.put(task);
//                 * 2.带超时的等待
//                    queue.offer(task, 500, TimeUnit.MICROSECONDS);
//                 * 3.放弃任务
//                    log.debug("放弃{}", task);
//                 * 4.抛出异常
//                    throw new RuntimeException("任务执行失败 " + task);
//                 * 5.调用者自己执行任务
                    task.run();

        }));
        for (int i = 0; i < 4; i++) {
            int t = i;
            threadPool.execute(() -> {
                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                log.info("{}", t);
            });
        }
    }
}

@FunctionalInterface
// 拒绝策略
interface RejectPolicy<T>{
    void reject(BlockingQueue<T> queue, T task);
}

@Slf4j
class ThreadPool{
    // 任务队列
    private BlockingQueue<Runnable> taskQueue;

    // 线程集合
    private HashSet<Worker> workers = new HashSet<>();

    // 核心线程数
    private int coreSize;

    // 获取任务的超时时间
    private long timeout;

    private TimeUnit timeUnit;

    // 拒绝策略
    private RejectPolicy<Runnable> rejectPolicy;

    public ThreadPool(int coreSize, long timeout, TimeUnit timeUnit, int queueCapacity, RejectPolicy<Runnable> rejectPolicy) {
        this.taskQueue = new BlockingQueue<>(queueCapacity);
        this.coreSize = coreSize;
        this.timeout = timeout;
        this.timeUnit = timeUnit;
        this.rejectPolicy = rejectPolicy;
    }

    // 执行任务
    public void execute(Runnable task){
        // 当任务数没有超过coreSize时，直接交给worker处理
        // 否则加入任务队列
        synchronized (workers) {
            if(workers.size() < coreSize){
                Worker worker = new Worker(task);
                log.debug("新增worker{}，{}", worker, task);
                workers.add(worker);
                worker.start();
            }else{
//                taskQueue.put(task);
                /**
                 * 拒绝策略（利用策略模式，让调用者自己选择）
                 * 1.死等
                 * 2.带超时的等待
                 * 3.放弃任务
                 * 4.抛出异常
                 * 5.调用者自己执行任务
                 */
                taskQueue.tryPut(rejectPolicy, task);
            }
        }
    }

    class Worker extends Thread{
        private Runnable task;

        Worker(Runnable task) {
            this.task = task;
        }

        @Override
        public void run() {
            // 执行任务
            // 1.当task不为空，执行任务
            // 2.执行任务队列中的任务
//            while(task != null || (task = taskQueue.take()) != null){
            while(task != null || (task = taskQueue.poll(timeout, timeUnit)) != null){
                try {
                    log.debug("正在执行{}", task);
                    task.run();
                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    task = null;
                }
            }
            synchronized (workers){
                log.debug("worker被移除{}", this);
                workers.remove(this);
            }
        }
    }
}

@Slf4j
class BlockingQueue<T>{
    // 1.任务队列
    private Deque<T> queue = new ArrayDeque<>();

    // 2.锁
    private ReentrantLock lock = new ReentrantLock();

    // 3.生产者条件变量
    private Condition fullWaitSet = lock.newCondition();

    // 4.消费者条件变量
    private Condition emptyWaitSet = lock.newCondition();

    // 5.容量
    private int capacity;

    BlockingQueue(int capacity) {
        this.capacity = capacity;
    }

    // 带超时的阻塞获取
    public T poll(long timeout, TimeUnit timeUnit){
        try {
            // 将超时时间统一转换为纳秒
            long nanos = timeUnit.toNanos(timeout);
            lock.lock();
            while(queue.isEmpty()){
                try {
                    // 返回的是剩余等待时间
                    if(nanos <= 0){
                        return null;
                    }
                    nanos = emptyWaitSet.awaitNanos(nanos);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            T t = queue.removeFirst();
            fullWaitSet.signal();
            return t;
        }finally {
            lock.unlock();
        }
    }

    // 阻塞获取
    public T take(){
        try {
            lock.lock();
            while(queue.isEmpty()){
                try {
                    emptyWaitSet.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            T t = queue.removeFirst();
            fullWaitSet.signal();
            return t;
        }finally {
            lock.unlock();
        }
    }

    // 阻塞添加
    public void put(T element){
        try {
            lock.lock();
            while(queue.size() == capacity){
                try {
                    log.debug("等待加入任务队列{}...", element);
                    fullWaitSet.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            log.debug("加入任务队列{}", element);
            queue.addLast(element);
            emptyWaitSet.signal();
        }finally {
            lock.unlock();
        }
    }

    // 带超时时间阻塞添加
    public boolean offer(T element, long timeout, TimeUnit timeUnit){
        try {
            long nanos = timeUnit.toNanos(timeout);
            lock.lock();
            while(queue.size() == capacity){
                try {
                    log.debug("等待加入任务队列{}...", element);
                    if(nanos <= 0){
                        return false;
                    }
                    nanos = fullWaitSet.awaitNanos(nanos);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            log.debug("加入任务队列{}", element);
            queue.addLast(element);
            emptyWaitSet.signal();
            return true;
        }finally {
            lock.unlock();
        }
    }

    // 获取大小
    public int size(){
        try {
            lock.lock();
            return queue.size();
        }finally {
            lock.unlock();
        }
    }

    public void tryPut(RejectPolicy<T> rejectPolicy, T task) {
        try {
            lock.lock();
            // 判断队列是否已满
            if(queue.size() == capacity){
                rejectPolicy.reject(this, task);
            }else{
                log.debug("加入任务队列{}", task);
                queue.addLast(task);
                emptyWaitSet.signal();
            }
        }finally {
            lock.unlock();
        }
    }
}
