package aaa课程代码.day02_运算符;

import java.util.Scanner;

/**
 3、编程题：从键盘输入一个65~90之间的整数，显示它对应的字母？
 4、编程题：从键盘输入一个整数，判断是偶数还是奇数？
 5、编程题：从键盘输入一个整数，判断是正数、负数、零？
 */
public class 练习题1_3 {
    public static void main(String[] args) {
        // 编程3：从键盘输入一个65~90之间的整数，显示它对应的字母？
        System.out.println("输入一个65~90之间的整数：");
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        char str = (char) n;
        System.out.println(str);
        // 编程4：判断是偶数还是奇数？
        if (n % 2 == 1 || n%2 == -1) {
            System.out.println("奇数");
        } else {
            System.out.println("偶数");
        }
        // 编程5：判断是正数、负数、零？
        if (n > 0) {
            System.out.println("正数");
        } else if (n < 0) {
            System.out.println("负数");
        } else {
            System.out.println("零");
        }
    }
}
