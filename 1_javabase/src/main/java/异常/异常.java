package 异常;

import java.util.Scanner;

/*
1、异常处理方式一：try..catch...finally的语法结构
         try{
            可能发生异常的代码
         }catch(异常类型1 异常名称){
            异常1的处理代码
         }catch(异常类型2 异常名称){
            异常1的处理代码
         }...
         finally{
            无论是否try部分发生异常，都要执行的代码。
         }

 说明：
     （1）可能的形式：try...catch，try...finally,try...catch...finally
     （2）执行特点
             如果try中没异常发生，那么catch部分不会执行，如果finally，会执行
             如果try中异常发生，那么会根据异常的类型，去匹配对应catch，如果匹配上了，就进入对应的catch部分执行，处理完后，如果finally执行finally，继续try..catch下面的代码。
             如果try中异常发生，又没对应的catch可以捕获它，如果finally执行finally，然后把异常抛给上级，当前方法就结束执行了。
     （3）catch可以很多个，从上到下的匹配顺序，如果多个catch的异常类型没包含关系，顺序是随意，如果包含关系，那么小上大下（子上父下
     （4）finally块中的代码一定会执行的

2、异常处理方式二：throws
      throws用于声明某个方法会抛出什么异常类型，然后调用者就知道需要处理什么异常了。
      throws语法：
      		声明方法时
      		【修饰符】 返回值类型  方法名（【形参列表】throws 异常类型列表{
      		}
      		表示在该方法中，可能发生这些异常，但是我没处理这些异常，让调用者来处理。
      throws：
      		方法重写时，要求子类重写的方法抛出的异常类型必须<=父类被重写的方法抛出的异常类型。
      		因为多态时，编译时try..catch是照父类方法抛出的异常捕获的处理的。
      throws：一般抛出编译时异常

3、自定义异常
    核心类库中已经预定义了很多异常，但是的时候仍然不合适。
    因为的时候，异常的类型不够见名知意。

    自定义异常：
    必须继承Throwable或它的子类，一般是继承Exception下面。

    自定义异常的对象，只能用throw抛出，JVM不会自动抛出。

4、面试题
 final，finalize，finally的区别？
     答案：
         final：最终的，是一个修饰符；
         final修饰类，表示该类不能被继承；
         final修饰方法，表示该方法不能被重写；
         final修饰变量，表示该变量的值不能被修改，是常量。

     finally：和try...catch结构一起使用，表示finally中语句无论是否发生异常，也无论是否捕获异常，也无论try和catch是否return语句，它都必须执行的部分。
     finalize：它是一个方法名，在Object类中声明，表示当对象要被GC回收之前，调用的，只调用一次。
 */
public class 异常 {
    public static void main(String[] args) throws 自定义异常, IllegalArgumentException {
        Scanner input = new Scanner(System.in);
        int money = 100;
        while (true) {
            try {                                           // 【1】、异常的处理方式一try..catch..finally
                System.out.println("请输入取款金额：");
                double take_money = input.nextDouble();
                if (money < 0) {
                    throw new IllegalArgumentException("取款金额不能为负数");//结束本次循环    // 【2】、异常的处理方式二throw + throws
                }
                if (take_money > money) {
                    throw new 自定义异常("余额不足");
                }
                money -= take_money;
            } catch (自定义异常 e) {
                System.out.println(e.getMessage());
                System.out.println("请重新输入取款金额：");
            } catch (IllegalArgumentException e) {
                System.out.println("抱歉，你脑子问题，取款金额能负数吗？");
                break;
            } finally {
                System.out.println("现余额：" + money + "元！！！");
            }
        }
    }
}

class 自定义异常 extends Exception {         // 【3】、自定义异常
    public 自定义异常(String message) {
        super(message);
    }
}
