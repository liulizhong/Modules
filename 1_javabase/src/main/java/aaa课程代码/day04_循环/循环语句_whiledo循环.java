package aaa课程代码.day04_循环;

import java.util.Scanner;

/**
 三、do...while循环结构
     语法结构：
         do{
         循环体：需要重复执行的语句块;
         }while(循环条件);

     执行过程：
         （1）执行一次循环体语句
         （2）判断循环条件
         （3）如果成立，继续执行一次循环体语句，然后回到（2）
     如果不成立，结束do..while循环

     执行特点：
        循环体语句至少执行一次，满足这种特征时，可以考虑使用do...while

     请使用switch...case和循环结构实现如下程序
         ----欢迎使用尚硅谷ATM----
         1、取款
         2、存款
         3、查询
         4、退出
         请选择：

         当用户输入1，请打印“欢迎使用取款功能！”
                   2，请打印“欢迎使用存款功能！”
                   3，请打印“欢迎使用查询功能！”
                   4，请打印“谢谢使用！”
                   其他，请打印“输入有误！”
 */
public class 循环语句_whiledo循环 {
    public static void main(String[] args) {
        // 语法案例：如上便的程序
        Scanner scanner = new Scanner(System.in);
        int input;
        do{
            System.out.println("----欢迎使用尚硅谷ATM----");
            System.out.println("1、取款");
            System.out.println("2、存款");
            System.out.println("3、查询");
            System.out.println("4、退出");
            System.out.println("请选择：");
            input = scanner.nextInt();
            switch (input) {
                case 1:
                    System.out.println("1欢迎使用取款功能");
                    break;
                case 2:
                    System.out.println("2欢迎使用存款功能");
                    break;
                case 3:
                    System.out.println("3欢迎使用查询功能");
                    break;
                case 4:
                    System.out.println("4谢谢使用！！！");
                    break;
                default:
                    System.out.println("输入有误！");
            }
        }while (input != 4);
    }
}
