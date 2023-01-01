package aaa课程代码.day02_运算符;

/**
 一、算术运算符
 二元运算符，双目运算符：操作数有两个
 加：+
 减：-
 乘：*
 除：/
 整数与整数只保留整数
 取模：% 取余
 正负号，只看被模数

 一元运算符，单目运算符：操作数有1个
 正号：+
 负号：-

 自增：++
 ++在前，先自增
 ++在后，后自增
 自减：--
 --在前，先自减
 --在后，后自减
 */
public class 算数运算符 {
    public static void main(String[] args) {
        int a = 2;
        int b = 5;

        System.out.println("a+b = " + (a + b));       // 7
        System.out.println("a-b = " + (a - b));       // -3
        System.out.println("a*b = " + a * b);         // 10
        System.out.println("a/b = " + a / b);         // 0
        System.out.println("a/b = " + (double)a / b); // 0.4
        System.out.println("a%b = " + a % b);         // 2

        System.out.println("-5%-2 = " + -5 % -2);   // -1 // 正负号，只看被模数
        System.out.println("5%-2 = " + 5 % -2);     // 1
        System.out.println("-5%2 = " + -5 % 2);     // -1
        System.out.println("5%2 = " + 5 % 2);       // 1

        int c = -a;
        System.out.println("c = " + c); //-2
        int d = -c;
        System.out.println("d = " + d); //2

        System.out.println("--------------------自增测试----------------------------");

        int x = 1;
        x++;//把x叫做自增变量
        System.out.println("x =" + x);//x=2

        ++x;
        System.out.println("x =" + x);//x=3

        int y = ++x;//(1)先x自增1 x=4 (2)把x的值赋值给y
        System.out.println("x =" + x);//x=4
        System.out.println("y =" + y);//y=4

        int z = x++;//(1)先把x=4的值放在操作数栈中（一个临时区域）（2）x自增x=5（3）把操作数栈中的值赋值给z
        System.out.println("x =" + x);//x=5
        System.out.println("z =" + z);//z=4

        x = x++;//(1)先把x=5的值放在操作数栈中（一个临时区域）（2）x自增x=6（3）把操作数栈中的值赋值给x
        System.out.println("x =" + x);//x=5

        //(1)把x=5的值放在操作数栈中（一个临时区域） 5
        //(2)x先自增 x=6
        //(3)再把x=6的值放在操作数栈中（一个临时区域） 6
        //(4)再把x=6的值放在操作数栈中（一个临时区域） 6
        //(5）x再自增 x=7
        //(6)把操作数栈中的三个值加起来赋值给m
        int m = x + ++x + x++;
        System.out.println("x =" + x);//x=7
        System.out.println("m =" + m); // 17=5+6+6

        //(1)先把x=7的值放在操作数栈中（一个临时区域）
        //(2)x先自增 x=8
        //(3)先把x=8的值放在操作数栈中（一个临时区域）
        //(4)先把x=8的值放在操作数栈中（一个临时区域）
        //(5)x再自增 x=9
        //(6)弹出上面的两个 8*8=64，结果压入操作数栈
        //(7)弹出操作数栈中的两个值64+7 结果赋值给n
        int n = x + ++x * x++;
        System.out.println("x =" + x); // 9
        System.out.println("n =" + n); // 71=7+8*8

        x=1;

        //从左往右把值压入栈，遇到自增，++在前先自增在压入值，++再后，先压入值，在自增
        int p = x + (++x * x++);
        System.out.println("x =" + x); //3
        System.out.println("p =" + p); //1+(2*2)
    }
}
