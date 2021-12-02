package java设计模式;
/*
  生产者与消费者问题（把解决生产者与消费者问题的代码模式成为生产者与消费设计模式
  问题的描述：
  该问题描述了两个（多个共享固定大小缓冲区的线程——即所谓的“生产者”和“消费者”——在实际运行时会发生的问题。生产者的主要作用是生成一定量的数据放到缓冲区中，然后重复此过程。与此同时，消费者也在缓冲区消耗这些数据。该问题的关键就是要保证生产者不会在缓冲区满时加入数据，消费者也不会在缓冲区中空时消耗数据。
要解决该问题，就必须让生产者在缓冲区满时等待(wait)，等到下次消费者消耗了缓冲区中的数据的时候，生产者才能被唤醒(notify)，开始往缓冲区添加数据。同样，也可以让消费者在缓冲区空时进入等待(wait)，等到生产者往缓冲区添加数据之后，再唤醒消费者(notify)。通常采用线程间通信的方法解决该问题。
依据线程间的通信方式解决这类的生产者和消费者的问题的模式，叫做生产者与消费者设计模式

  举例：
  例如：小米工厂的库房限，只能存10台小米电视
  小米生产线与小米的销售团队之间就会构成生产者与消费者问题。

  问题：
  （1）库房是限的，生产者与消费者可能需要“通信，协作”
    所以当库房满了，生产者应该停下来，等销售团队销售了产品，生产者应该被唤醒，继续生产
  	同样，如果库房空了，销售团队也应该停下来，等生产者生产了产品，销售团队被唤醒，继续销售
  （2）库房：共享数据，因为生产者线程与销售团队都要对库房进行操作
  多个线程操作共享数据，需要同步


  java.lang.Object类：
  （1）wait()
  （2）notify() 或notifyAll()

  如果只一个线程需要唤醒，那么就用notify()，如果多个线程需要唤醒，那么用notifyAll()

  因为wait()和notify()或notifyAll()这几个方法，规定了，只能由锁对象调用。
  锁对象，可以是任意类型的对象。


  当多个生产者与多个消费者：
  （1）wait()被唤醒后，要重新判断条件，所以用while
  （2）notify改成notifyAll()
 */
public class 生产者和消费者设计模式 {
    public static void main(String[] args) {
        //多个生产者和多个销售者
        Worker w = new Worker();
        Worker w2 = new Worker();
        Saler s = new Saler();
        Saler s2 = new Saler();
        w.start();
        s.start();
        w2.start();
        s2.start();
    }
}

//仓库
class Houseware{
    private static Object[] data = new Object[1];//数组是长度一旦确定是不能修改的，这里不考虑扩容
    private static int count = 0;

    //往数组中添加数据
    //静态方法的同步锁：当前类.class
    synchronized public static void add(Object obj){
        while(count >= data.length){//数组已满了
            try {
                //当前线程wait()
                //锁对象.wait()
                Houseware.class.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        data[count] = obj;
        count++;
        System.out.println("生产者生产了一台电视机，库存量是：" + count);

        Houseware.class.notifyAll();//因为这里只一个生产者一个消费者，那么唤醒的是对方
    }

    //从数组中取走数据
    synchronized public static void take(){
        while(count <= 0){
            try {
                //当前线程也停下来wait()
                Houseware.class.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("销售者，卖出了一台电视机：" + data[0]);
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //默认删除[0]元素
        //假设count = 3，移动[1][2]
        //假设count = 1,移动0个
        System.arraycopy(data, 1, data, 0, count-1);
        data[count-1] = null;
        count--;

        System.out.println("库存量是：" + count);

        Houseware.class.notifyAll();//因为这里只一个生产者一个消费者，那么唤醒的是对方
    }
}

class Worker extends Thread{
    private int i = 1;
    public void run(){
        while(true){
            Houseware.add(new TV("小米电视" + i++));
        }
    }
}

class Saler extends Thread{
    public void run(){
        while(true){
            Houseware.take();
        }
    }
}

class TV{
    private String name;

    public TV(String name) {
        super();
        this.name = name;
    }

    @Override
    public String toString() {
        return "TV [name=" + name + "]";
    }

}