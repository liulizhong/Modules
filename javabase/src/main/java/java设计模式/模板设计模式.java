package java设计模式;

/*
     设计模式：
     	针对某些问题和情景，以前的程序员总结出了一些代码的设计模板，即套路。
     《23种常见的设计模式》

     模板设计模式（了解）：

     表象：一些代码是确定的，有一些代码是不确定。
     情景：当解决某个问题，或者是完成某个功能的步骤是确定的，但是其中的一个或多个子步骤是不确定。
     		那么我们通常会用模板设计来解决。
     代码结构：
     		声明一个抽象父类，在父类中把这个问题的解决步骤，整体定下来，然后，不确定的子步骤通过“抽象方法”实现，
     		然后“抽象方法”由子类去实现它。

     例如：完成计算任意一段代码的运行时间
     （1）获取开始时间
     （2）运行代码                          不确定运行xx代码
     （3）获取结束时间
     （4）计算时间差

     后面应用：web中
     (1)服务器接收客户端的请求，
     (2)处理请求(service)		-->处理登录、订单....    doGet(),doPost()
     (3)并给出响应
 */
public class 模板设计模式 {
    public static void main(String[] args) {
        MyCalTime my = new MyCalTime();
        long time = my.getTime();
        System.out.println("耗时：" + time);
    }
}

abstract class CalTime {
    //long：毫秒
    //final：不希望子类重写我解决问题的总步骤
    public final long getTime() {
//		（1）获取开始时间
        long start = System.currentTimeMillis();//获取系统时间距离1970-1-1 0:0:0:0的毫秒值
//		 * （2）运行代码                          不确定运行xx代码
        doWork();
//		 * （3）获取结束时间
        long end = System.currentTimeMillis();
//		 * （4）计算时间差
        return end - start;
    }

    protected abstract void doWork();
}


//使用模板
class MyCalTime extends CalTime {
    @Override
    protected void doWork() {
        long sum = 0;
        for (int i = 0; i < 10000; i++) {
            sum += i;
        }
        System.out.println("和：" + sum);
    }

}
