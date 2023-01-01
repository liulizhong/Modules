package aaa课程代码.day02_运算符;

/**
 二、赋值运算符
 最基本的赋值运算符：=
 Java中把=右边的结果赋值给=左边的变量

 扩展的赋值运算符：
 +=
 -=
 *=
 /=
 %=
 >>=
 <<=
 ...

 注意：
 （1）把=右边的当做一个整体，先运算
 （2）再和=左边的变量进行(xx)运算
 （3）如果结果的类型不符合，会隐含强制类型转换
 （4）扩展的赋值运算符中间不要加空格
 */
public class 赋值运算符 {
    public static void main(String[] args) {
        //声明一个int型的变量
        int a;

        //把10赋值给a
        a = 10;

        //10 = a;//错误

        byte b1 = 1;
        byte b2 = 2;

        //b1 = b1 + b2;//编译报错
        b1 += b2;//相当于b1 = (byte)(b1 + b2);

        System.out.println("b1 = " + b1);
        System.out.println("b2 = " + b2);

        int x = 4;
        int y = 5;
        y *= x + y;//等价于 y = y * (x+y);
        System.out.println("x = " + x);//4
        System.out.println("y = " + y);//45
    }
}
