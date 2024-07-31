import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedList;
import java.util.Queue;

// 生产者消费者模型
@Slf4j
public class ProducerConsumer {
    public static void main(String[] args) {
        MessageQueue messageQueue = new MessageQueue(2);
        for (int i = 0; i < 3; i++) {
            int id = i;
            new Thread(() -> {
                messageQueue.put(new Message(id, "值" + id));
            }, "生产者" + id).start();
        }

        new Thread(() -> {
            while (true){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                messageQueue.get();
            }
        }, "消费者").start();
    }
}
@Slf4j
class MessageQueue{
    private Queue<Message> queue = new LinkedList<>();
    private Integer capacity;

    public MessageQueue(Integer capacity) {
        this.capacity = capacity;
    }

    public Message get(){
        synchronized (queue){
            while(queue.isEmpty()){
                try {
                    log.info("队列为空，消费者等待");
                    queue.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            Message remove = queue.remove();
            log.info("已消费：{}", remove);
            queue.notifyAll();
            return remove;
        }
    }

    public void put(Message message){
        synchronized (queue){
            while (queue.size() == capacity){
                try {
                    log.info("队列已满，生产者等待");
                    queue.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            queue.add(message);
            log.info("已生产：{}", message);
            queue.notifyAll();
        }
    }
}

@Slf4j
@Data
final class Message{
    private int id;
    private Object value;

    public Message(int id, Object value) {
        this.id = id;
        this.value = value;
    }

}
