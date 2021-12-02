package 多线程;

/*
    多生产者和多消费者问题：
        （1）线程安全问题：因为生产者与消费者共享“缓冲区”，本例中工作台
        （2）缓冲区大小限问题，所以需要生产者与消费者“协调”工作
                一个wait一个notify
        （3）出现工作台有复数问题
        （4）卡死问题，不生产也不消费了

    解决方法：
        （1）将put和take方法加锁synchronized，也就是共享变量为this(工作台是一个)。
        （2）wait() => notify()/notifyAll()
        （3）虚假唤醒：因notify唤醒了同类方法，可以改循环判断，即唤醒后重新判断条件while(?)
        （4）因为唤醒了同类判断条件不成立，即都wait了，换成notifyAll()可解决

    这些方法必须由“同步监视器对象”调用。

    为什么wait()和notify()在Object类中呢？
        因为这两个方法必须由“同步监视器对象”调用，而同步监视器对象是任意类型的对象。
 */
public class 线程通信问题_多生产者多消费者 {
    public static void main(String[] args) {
        Workbench wb = new Workbench();
        Cook c = new Cook(wb);
        Waiter w = new Waiter(wb);

        c.start();
        w.start();

        Cook c2 = new Cook(wb);
        Waiter w2 = new Waiter(wb);

        c2.start();
        w2.start();
    }
}

class Cook extends Thread{
    private Workbench w ;

    public Cook(Workbench w) {
        super();
        this.w = w;
    }

    public void run(){
        while(true){
            w.put();
        }
    }
}

class Waiter extends Thread{
    private Workbench w;

    public Waiter(Workbench w) {
        super();
        this.w = w;
    }

    public void run(){
        while(true){
            w.take();
        }
    }
}

class Workbench{
    private static final int MAX_VALUE = 10;//工作台最多可以放10盘菜
    private int num;//表示工作台上的菜的数量

    //非静态方法的同步监视器对象是this
    public synchronized void put(){
        while(num>=MAX_VALUE){
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        num++;
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("厨师又炒了一盘菜，工作台上现在有：" + num);
        this.notifyAll();//通知的是与我是同一个监视器对象的wait的一个线程
    }

    public synchronized void take(){
        while(num <= 0){
            //服务员线程应该等待
            try {
                this.wait();//这个this，现在是同步监视器对象
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        num--;
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("服务员取走了一盘菜，工作台上现在有：" + num);
        this.notifyAll();//通知的是与我是同一个监视器对象的wait的一个线程
    }
}
