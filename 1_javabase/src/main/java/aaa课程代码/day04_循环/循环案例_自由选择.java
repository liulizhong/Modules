package aaa课程代码.day04_循环;


import java.util.Scanner;

public class 循环案例_自由选择 {
    public static void main(String[] args) {
        // 【案例1】：输入两个正整数m和n，求其最大公约数和最小公倍数
        Scanner scanner = new Scanner(System.in);
        System.out.println("输入第一个正整数：");
        int num1 = scanner.nextInt();
        System.out.println("输入第二个正整数：");
        int num2 = scanner.nextInt();
        // 最大公约数
        int small = num1 > num2 ? num2 : num1;
        for (; small > 1; small--) {
            if (num1 % small == 0 && num2 % small == 0) break;
        }
        System.out.println(num1 + "和" + num2 + "的最大公约数是" + small);
        // 最小公倍数
        int big = num1 > num2 ? num1 : num2;
        for (; ; big++) {
            if (big % num1 == 0 && big % num2 == 0) break;
        }
        System.out.println(num1 + "和" + num2 + "的最小公倍数是" + big);
        System.out.println(num1 + "和" + num2 + "的最小公倍数是" + num1 * num2 / small);

        // 【案例2】：输出：
        //        *
        //        **
        //        ***
        for (int i = 1; i <= 5; i++) {//外循环
            //System.out.println("*****");
            //分解为：（1）连续打印5颗*（2）换行
            for (int j = 1; j <= i; j++) {//内循环
                System.out.print("*");
            }
            System.out.println();
        }

        // 【案例3】：输出：找出1-100之间所有的素数
        for (int i = 2; i <= 100; i++) {
            int count = 0;
            for (int j = 2; j < i; j++) {
                if (i % j == 0) count++;
            }
            if (count == 0) System.out.println(i + "是质数！！！");
        }

        // 【案例4】：一个数如果恰好等于它的因子之和，这个数就称为"完数"。（因子：除去这个数本身的约数），找出1000以内的所有完数。例如6=1＋2＋3
        for (int i = 1; i <= 1000; i++) {
            int yin = 0;
            for (int j = 1; j < i; j++) {
                if (i % j == 0) yin += j;
            }
            if (i == yin) System.out.println(i + "是完数！！");
        }

        // 【案例5】：假设从2000年1月1日开始三天打鱼，两天晒网，问今天是打鱼开始晒网，从键盘输入今天的日期年、月、日
        System.out.println("输入年份");
        int year = scanner.nextInt();
        System.out.println("输入月份");
        int month = scanner.nextInt();
        System.out.println("输入日期");
        int day = scanner.nextInt();
        int daySum = day;
        // 1、先算本年度天数
        for (int i = 1; i < month; i++) {
            if (i == 1 || i == 3 || i == 5 || i == 7 || i == 8 || i == 10 || i == 12) {
                daySum += 31;
            } else if (i == 2) {
                if ((year / 4 == 0 && year / 100 != 0) || (year / 400 == 0)) {
                    daySum += 29;
                } else {
                    daySum += 28;
                }
            } else {
                daySum+=30;
            }
        }
        // 2、累计年份天数
        for (int i = 2000; i < year; i++) {
            if ((year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)) {
                daySum += 366;
            } else {
                daySum+=365;
            }
        }
        if (daySum % 5 == 4 || daySum % 5 == 0) {
            System.out.println("今天在筛网！！！");
        } else {
            System.out.println("今天在打鱼！！！");
        }

        // 【案例6】：打印菱形：
        //            *
        //           ***
        //          *****
        //         *******
        //        *********
        //         *******
        //          *****
        //           ***
        //            *
        for (int i = 5; i > 0; i--) {
            for (int j = 1; j < i; j++) {
                System.out.print(" ");
            }
            for (int k = 1; k <= (5-i)*2+1; k++) {
                System.out.print("*");
            }
            System.out.println();
        }
        for (int i = 4; i > 0; i--) {
            for (int j = 1; j <= 5-i; j++) {
                System.out.print(" ");
            }
            for (int k = 1; k <= i*2-1; k++) {
                System.out.print("*");
            }
            System.out.println();
        }

        // 【案例6】：打印空心菱形：
        //    *
        //   * *
        //  *   *
        // *     *
        //*       *
        // *     *
        //  *   *
        //   * *
        //    *
        for (int i = 5; i > 0; i--) {
            for (int j = 1; j < i; j++) {
                System.out.print(" ");
            }
            System.out.print("*");
            for (int k = 1; k <= (5-i-1)*2+1; k++) {
                System.out.print(" ");
            }
            if (i != 5) {
                System.out.print("*");
            }
            System.out.println();
        }
        for (int i = 4; i > 0; i--) {
            for (int j = 1; j <= 5-i; j++) {
                System.out.print(" ");
            }
            System.out.print("*");
            for (int k = 1; k <= i*2-3; k++) {
                System.out.print(" ");
            }
            if (i != 1) {
                System.out.print("*");
            }
            System.out.println();
        }


    }
}
