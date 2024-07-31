package design_pattern;

// 懒汉单例
public final class Singleton {
    // double-checked-locking
    private Singleton(){}
    private static volatile Singleton INSTANCE = null;
    public static Singleton getInstance(){
        if(INSTANCE == null){
            synchronized(Singleton.class){
                if(INSTANCE == null){
                    INSTANCE = new Singleton();
                }
            }
        }
        return INSTANCE;
    }

}

// 静态内部类
final class Singleton1{
    private Singleton1(){};
    private static class LazyHolder{
        static final Singleton1 INSTANCE = new Singleton1();
    }

    public static Singleton1 getInstance(){
        return LazyHolder.INSTANCE;
    }
}
