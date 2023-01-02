package aaa课程代码.day08_方法;

/**
 方法（method）：又称为函数（function），是代表一个独立的功能，可以被反复使用的功能

 方法的特点：
     （1）方法和变量一样：必须先声明后使用
     （2）不调用不执行，调用一次执行一次
     （3）一个方法最多只能返回一个结果，如果有多个数据，那么需要容器装进起来

 一、声明
     1、如何声明方法？
        位置：在类中方法外

     格式：
         【修饰符】 返回值类型 方法名(形参列表){
             方法体：实现该方法功能的代码块
         }

     方法名：
     规则：...
     规范：第一个单词首字母小写，从第二个单词开始首字母大写
         见名知意
         形参列表：形式参数列表，可以多个形参
         返回值类型：如果是void，说明没有返回值
         如果是其他的，说明有返回值


 二、调用
     1、其他类中
         （1）调用的格式：必须通过“对象.”
         （2）实参的选择看被调用的方法声明时是否有形参：实参的个数、类型、顺序要与形参一一对应
                 调用时()中的参数我们成为实参
                 实参的作用：给形参传值
         （3）是否有返回值
                 被调用方法的前面是void：说明没有返回值，不能打印也不能接收
                 被调用方法的前面不是void：说明有返回值，可以直接打印，也可以用变量接收

 三、方法的形式
     1、无参无返回值
     2、有参无返回值
     3、无参有返回值
     4、有参有返回值

 四、方法的设计
     属性没有特别说明，上来就加private

     1、如何设计方法？
         1)、方法的功能是什么？取一个见名知意的方法名
         2)、方法是否需要形参？完成这个功能是否需要别的数据辅助，除了属性等之外的数据
         3)、是否需要返回结果给调用者？
         4)、权限修饰符是什么？没有特别说明，一般方法都是public

     2、形参的声明格式
         形参也是变量，特殊的变量，在方法的()中声明的变量
         (数据类型 形参名)
         (数据类型 形参名1，数据类型 形参名2  。。。。)

 五、方法的参数
     方法的参数传递机制：
         形参
         实参

     实参-->形参

     形参是基本数据类型，实参给形参传值：数据值，形参的值修改了，不影响实参
 */
public class 类_成员2_方法 {
    //public static修饰符
    //void：返回值类型
    //main：方法名
    //String[] args：形参列表
    public static void main(String[] args){
        Account a = new Account();//比喻申请一张卡
        //a.balance = 1200;

        //System.out.println(a.setBalance(1200.0));	//错误的，因为a.setBalance(1200.0)无返回值
        //double b = a.setBalance(1200.0);//错误的，因为a.setBalance(1200.0)无返回值
        a.setBalance(1200.0);
        System.out.println("余额：" + a.getBalance());//正确的，因为a.getBalance()有返回值
        //直接打印返回值

        a.setBalance(8000);
        double yu = a.getBalance();//接收返回值//正确的，因为a.getBalance()有返回值
        System.out.println("余额：" + yu);

        // 测试参数改变：形参a【1->2】实参x【1->1】
        int x = 1;
        System.out.println("before：x = " + x);  // 1
        a.changeDouble(x);
        System.out.println("after：x = " + x);  // 1
    }
    //public static修饰符
    //void：返回值类型
    //test：方法名
    //无形参列表，简称无参
    public static void test(){
        System.out.println("测试");
    }

}
class Account{
    //一旦私有化，外界就无法直接访问
    private String id;//账户
    private double balance;//余额

    //提供一个方法：为balance赋值，即设置balance的值
    //public：修饰符
    //void：返回值类型
    //setBalance：方法名
    //double b：形参列表
    public void setBalance(double b){
        if(b>=0){
            balance = b;
        }else{
            //后面这个代码应该修改为异常
            System.out.println("余额不能为负数");
        }
    }

    //提供一个方法：返回balance的值，即获取balance的值
    //public：修饰符
    //double：返回值类型
    //getBalance：方法名
    //无参
    public double getBalance(){
        return balance;
    }

    // 参数测试：形参&实参值改变
    public void changeDouble(int a){
        System.out.println("before：a = " + a);
        a *= 2;
        System.out.println("after：a = " + a);
    }
}