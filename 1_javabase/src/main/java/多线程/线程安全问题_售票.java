package 多线程;

import java.util.ArrayList;

/*
     所谓线程安全问题，其实就是在多线程并发执行时，对共享内存中共享对象的属性进行修改所导致的数据中途问题

     共享内存：对于一个线程来讲，栈内存是独享的，但是堆内存和方法去内存是共享的
     共享对象：对于多线程来讲，访问的是同一个对象，如果多线程访问的对象不是同一个，就不会出现线程安全问题


     卖票：两个窗口同时卖票

     问题现象：出现了两个窗口卖出，票号相同的票
     原因：两个线程操作了同一个资源（共享数据list），这就会线程安全问题。

     如何解决？两种同步方式解决方法：

     （1）同步代码块
     语法格式：
     	synchronized(同步监视器对象){
     		需要同步的代码
      }

      同步监视器对象的要求：
      （1）同步监视器对象的类型：可以是任意的引用数据类型
      （2）使用共享数据的这多线程要用同一个同步监视器对象

      （2）同步方法
      语法格式：
      【修饰符】 synchronized 返回值类型  方法名(【形参列表】)【throws 异常列表】{
      }

      同步监视器对象：
      非静态方法：this
      静态方法：当前类.class
      也必须保证多线程要用同一个同步监视器对象

      锁的代码的范围：不能太大也不能太小
  		太小，导致售票方法的过程没锁柱
  		太大，会导致失去多线程意义
      依据：哪些代码执行过程中，不允许其他线程“半路插进来”
 */
public class 线程安全问题_售票 {
    public static void main(String[] args) {
        Window w1 = new Window("窗口一");
        Window w2 = new Window("窗口二");

        w1.start();
        w2.start();
    }
}

// 多线程实现类
class Window extends Thread{
    static TicketService ts = new TicketService();
//	TicketService ts = new TicketService();//错误的，每个Window对象的属性是独立的 [这里应该是所有人共用一个购票系统]

    public Window(String name) {
        super(name);
    }

    public void run(){
//		synchronized (ts) {//错误
        while(ts.hasTicket()){
            System.out.println(getName());
            ts.sale();
        }
//		}
    }
}

class TicketService {
    private ArrayList<String> all = new ArrayList<String>();
//	private Vector<String> all = new Vector<String>(); //不管用，只是他的方法同步，但售票方法未同步

    public TicketService(){
        all.add("01车01A号");
        all.add("01车01B号");
        all.add("01车01C号");
        all.add("01车01D号");
        all.add("01车01E号");

        all.add("02车01A号");
        all.add("02车01B号");
        all.add("02车01C号");
        all.add("02车01D号");
        all.add("02车01E号");
    }

    //卖票
/*	public void sale(){
		if(all.size() > 0){
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			String ticket = all.remove(0);//返回的是集合刚刚删除的元素
			System.out.println("卖出：" + ticket + "，剩余：" + all.size());
		}else{
			System.out.println("票卖完了。。。。");
		}
	}*/

/*	//卖票
	public synchronized void sale(){
		if(all.size() > 0){
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			String ticket = all.remove(0);//返回的是集合刚刚删除的元素
			System.out.println("卖出：" + ticket + "，剩余：" + all.size());
		}else{
			System.out.println("票卖完了。。。。");
		}
	}*/

    //卖票
    public void sale(){
        synchronized (all) {  //监视器对象只要是调用者共享的一样的的就行，比如this、"a"都行
//		synchronized (this) {
//		synchronized ("") {
            if(all.size() > 0){
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                String ticket = all.remove(0);//返回的是集合刚刚删除的元素
                System.out.println("卖出：" + ticket + "，剩余：" + all.size());
            }else{
                System.out.println("票卖完了。。。。");
            }
        }
    }

    public boolean hasTicket(){
        return all.size()>0;
    }
}