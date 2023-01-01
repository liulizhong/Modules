package aaa课程代码.day04_循环;

import java.util.Scanner;

public class 循环语句_while循环_所有案例 {
    public static void main(String[] args) {
        // 【案例1】、while(true)案例：从键盘输入整数，输入0结束，统计输入的正数、负数的个数，并求和。
        Scanner scanner = new Scanner(System.in);
        int sum = 0;
        int zhengCount = 0;
        int fuCount = 0;
        int zhengSum = 0;
        int fuSum = 0;
        while (true) {
            System.out.println("输入一个正负数【0表示结束】：");
            int input = scanner.nextInt();
            if (input == 0) {
                break;
            } else if (input < 0) {
                fuSum+=input;
                System.out.println("你输入了一个负数，次数为：正数" + zhengCount + "次，负数" + ++fuCount + "次。正数总和"+zhengSum+",负数总和："+fuSum);
            } else if (input > 0) {
                zhengSum+=input;
                System.out.println("你输入了一个负数，次数为：正数" + ++zhengCount + "次，负数" + fuCount + "次。正数总和"+zhengSum+",负数总和："+fuSum);
            }
        }
        System.out.println("输入结束，最终正数" + zhengCount + "次，负数" + fuCount + "次。正数总和"+zhengSum+",负数总和："+fuSum);

        // 【案例2】、do_while案例：随机生成一个100以内的数，猜数字游戏
        //        从键盘输入数，如果大了提示，大了，如果小了，提示小了，如果对了，就不再猜了，并统计一共猜了多少次
        //        提示：随机数
        //        import java.util.Random;
        //        Random rand = new Random();
        //        int num= rand.nextInt(100);
        int cai = 0;
        int count = 0;
        System.out.println(cai);
        do {
            System.out.print("你猜：");
            cai = scanner.nextInt();
            if (cai > 27) {
                System.out.println("大了");
                count++;
            } else if (cai < 27) {
                System.out.println("小了");
                count++;
            }
        } while (cai != 27);
        System.out.println("你猜对了正确结果是27,一共猜的次数："+count);
    }
}
