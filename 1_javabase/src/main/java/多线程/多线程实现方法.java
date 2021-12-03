package 多线程;

/*
    多线程实现方式：
    一、继承Thread类:一个线程就是Thread类的一个对象，
         1、自定义线程类，继承java.lang.Thread类
         2、重写Thread类中的run()
             run()中就是该线程要完成的任务代码，方法体称为线程体
         3、创建线程对象
         4、启动线程：start()，一个线程只能被start一次
         千万注意千万不要手动调用run()方法，要是手动调用了的话就是去多线程了，顺序执行了
         start()：告知操作系统，操作系统告知CPU需要调度我的xx线程。并且要告知JVM再开启一块独立的虚拟机栈。
    二、实现Runnable接口
         1、自定义线程类，实现java.lang.Runnable接口
         2、实现接口的run()：run()中就是该线程要完成的任务代码，方法体称为线程体
         3、创建实现类对象
         4、创建Thread类对象，真正的线程对象
         5、启动线程：start()	一个线程只能被start一次
         Thread.currentThread().getName()方法可以获取线程名称
    三、静态代理：
         （1）主题：接口
                代理者与被代理者都要实现主题接口，例如：Runnable
         （2）代理者：Thread
                因为Thread类中start()等方法
         （3）被代理者：MyRunnable
    四、多线程所有方法
        1、Thread(Runnable target,String name)            //构造器，name指线程名称，target指Runnable接口的实现类
        2、setName(); / getName();                       //设置和获取线程名，实现Runnable接口的需要：Thread.currentThread().getName()
        3、run()                                           //编写线程体，该线程要执行的代码
        4、start()                                     //启动线程
        5、static void sleep(毫秒)	                       //Thread.sleep(毫秒值)1000毫秒 = 1秒
        6、static yield()	                               //Thread.yield()，一般在某个线程的run方法里执行。暂停当前线程（Thread.yield()代码）该线程主动放弃CPU资源，和其他线程重新抢占CPU资源。
        7、join()                                      //加塞。例如在main线程中加入“j.join（）；”那么main线程要等到join线程结束后才能继续执行【j.join（1000）：主线程等1秒便继续执行】
        8、static Thread currentThread()：             //在继承Thread类的线程体中，直接用this,在实现Runnable接口的线程体中或main主线程体中，要获取当前线程对象：Thread.currentThread()
        9、m.setDamemon(true);	//守护线程，为其他线程服务，例如垃圾回收线程，如果内存中其他线程已经死亡，那么守护线程会自动终止。
        10、getPriority(MIN_PRIORITY)                   //获取线程优先级：[1,10]，10最高，主线程默认是5
        11、sePriority();                               //设置优先级
        12、synchronized                                //锁，线程安全问题时将方法加锁解决
        13、wait();                                     //线程进入等待状态，一般多线程模式会用到
        14、notify()/notifyAll()                        //唤醒，针对上边的wait，等待的会被唤醒，单个随即唤醒/全部唤醒
 */
public class 多线程实现方法 {
    public static void main(String[] args) throws InterruptedException {
        //// 【一】、多线程实现方法一继承Thread类
        MyThread myThread1 = new MyThread("Thread线程1");
        MyThread myThread2 = new MyThread("Thread线程2");
        myThread1.start();
        myThread2.start();

        //// 【二】、多线程实现方法二实现Runnable接口
        MyRunnable myRunnable = new MyRunnable();
        Thread runnable1 = new Thread(myRunnable, "Runtable线程1");
        Thread runnable2 = new Thread(myRunnable, "Runtable线程2");
        runnable1.start();
        runnable2.start();

        //// 【三】、启动主线程。获取当前线程名方法：Thread.currentThread().getName()
        for (int i = 0; i <= 100; i++) {
            System.out.println(Thread.currentThread().getName() + ":" + i);
        }

        //// 【四】线程的常用方法
        // 1、Thread四种构造器：Thread()、Thread(String name)、Thread(Runnable target)、Thread(Runnable target,String name)
        Thread runtable = new Thread(myRunnable, "Runtable线程");
        // 2、设置线程名称和获取线程名称
        runtable.setName("Runtable修改线程");
        runtable.getName();                 // 获取线程名方式一：通过线程对象
        Thread.currentThread().getName();   // 获取线程名方式二：在线程方法内[无法获取线程对象]
        // 3、启动线程
        runtable.start();
        // 4、线程休眠3秒
        runtable.sleep(3000);
        // 5、放弃线程资源[一般在某个线程run方法里执行此句，暂停当前线程（Thread.yield()代码）在那个线程体中，主动放弃CPU资源，和其他线程共同抢占CPU资源。]
        runtable.yield();   // yield()会导致线程回到就绪状态，也可能下次抢到的还是它。
        Thread.yield();
        // 6、例如在main线程中加入“j.join（）；”那么main线程要等到join线程结束后才能继续执行。j.join（1000）表示主线程等1秒便继续执行。j.join（）
        runtable.join();
        // 7、获取当前线程对象
        Thread.currentThread();
        // 8、守护线程：为其他线程服务，例如垃圾回收线程，如果内存中其他线程已经死亡，那么守护线程会自动终止。（意思就是要等其它线程结束后，守护线程没执行完就直接结束：要反应一小会反应时间）
        runtable.setDaemon(true);
        // 9、设置、获取线程优先级：优先级高的线程：更多机会，概率抢到CPU,所以我们不要让业务逻辑依赖于优先级
        //		优先级的等级：1-10。默认优先级是5。主线程优先级就是5
        int runtablePriority = runtable.getPriority();
        runtable.setPriority(9);
        // 10、线程安全问题-关键字 - synchronized
        // synchronized(同步监视器对象) { 需要同步的代码 }
        // 【修饰符】 synchronized 返回值类型  方法名(【形参列表】)【throws 异常列表】{ 方法体 }
        // 11、线程通信问题-关键字 - synchronized、wait()、notify()、notifyAll()
        runtable.wait();
        runtable.notifyAll();

    }
}

// 多线程实现方式一继承Thread类
class MyThread extends Thread {
    public MyThread(String name) {
        super(name);
    }

    @Override
    public void run() {
        for (int i = 0; i <= 100; i++) {
            System.out.println(Thread.currentThread().getName() + ":" + i);
        }
    }
}

// 多线程实现方法二实现Runnable接口
class MyRunnable implements Runnable {

    @Override
    public void run() {
        for (int i = 0; i <= 100; i++) {
            System.out.println(Thread.currentThread().getName() + ":" + i);
        }
    }
}
