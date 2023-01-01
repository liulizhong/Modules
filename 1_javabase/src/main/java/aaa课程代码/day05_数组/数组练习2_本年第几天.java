package aaa课程代码.day05_数组;


import java.util.Scanner;

public class 数组练习2_本年第几天 {
    public static void main(String[] args) {
        // 【2】、从键盘分别输入年、月、日，判断这一天是当年的第几天
        //   注：判断一年是否是闰年的标准：
        //       1）可以被4整除，但不可被100整除
        //       2）可以被400整除
        //要求：使用循环和数组实现
        int[] days = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        Scanner scanner = new Scanner(System.in);
        System.out.println("输入年份：");
        int year = scanner.nextInt();
        System.out.println("输入月份：");
        int month = scanner.nextInt();
        System.out.println("输入日期：");
        int day = scanner.nextInt();
        int result = day;
        for (int i = 1; i < month; i++) {
            result += days[i];
        }
        if ((year % 4 == 0 && year % 100 != 0 && month > 2) || (year % 400) == 0 && month > 2) result++;
        System.out.println("这一年第读多少天：" + result);
    }
}
