package aaa课程代码.day02_运算符;

import java.util.Scanner;

/**
 五、三元运算符（条件运算符）

 条件表达式 ? 结果表达式1 : 结果表达式2
 如果条件表达式为true，那么整个表达式的结果取“结果表达式1”的值，否则取“结果表达式2”的值

 “结果表达式1”和“结果表达式2”结果的类型必须是一致的

 一元：单目，一个操作数，例如：正号、负号、自增、自减、逻辑非
 二元：双目，两个操作数，例如：+,-,*,/,%,>,<....
 三元：三目，三个操作数
 */
public class 三元运算符 {
    public static void main(String[] args){
        Scanner input = new Scanner(System.in);//比喻成买了一个键盘，放到桌子上

        System.out.print("请输入第一个整数：");
        int a = input.nextInt();//比喻成用键盘打字，输入一个整数，整数用a装起来

        System.out.print("请输入第二个整数：");
        int b = input.nextInt();

        System.out.print("请输入第三个整数：");
        int c = input.nextInt();

        //找出大的那个，赋值给max
        //如果a>b，那么取a的值，否则取b的值，赋给max
        int max = a>b ? a : b;
        System.out.println("a，b中的最大值 " + max);

        max=max<c?c:max;
        System.out.println("三个数的max = " + max);

    }
}
