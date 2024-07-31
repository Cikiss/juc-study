public class Synchronized {
    public static void main(String[] args) {
        // 1.一般使用方法
        Object room = new Object();
        synchronized (room){

        }
        // 2.加在方法上
        class Test{
            public synchronized void test1(){

            }
            // 等价于
            public void test2(){
                synchronized (this){

                }
            }
        }

        // 3.wait\notify
        // wait与sleep的区别
        // 1.wait是Object类的方法，sleep是Thread的方法
        // 2.wait会释放锁，sleep不会释放锁
        // 3.wait需在synchronized环境下使用
        /**
         * 使用方法
         * Thread1:
         * synchronized(lock){
         *      while(条件不成立){
         *          lock.wait();
         *      }
         *      // 干活
         * }
         *
         * Thread2:
         * synchronized(lock){
         *      lock.notifyAll();
         * }
         */
    }
}
