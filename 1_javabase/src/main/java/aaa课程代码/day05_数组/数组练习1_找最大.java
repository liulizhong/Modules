package aaa课程代码.day05_数组;


import java.util.Scanner;

public class 数组练习1_找最大 {
    public static void main(String[] args) {
        // 【1】、请输入本组学员的人数，并输入每一个学员的姓名、体重。找出最重的学员的体重和姓名
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入学员的个数：");
        int count = scanner.nextInt();
        // 声明和初始化 体重&姓名 的数组
        Double[] weights = new Double[count];
        String[] names = new String[count];
        for (int i = 0; i < count; i++) {
            System.out.println("请输入第" + i + "个学员的姓名：");
            names[i] = scanner.next();
            System.out.println("请输入第" + i + "个学员的体重：");
            weights[i] = scanner.nextDouble();
        }
        int index = 0;
        for (int i = 0; i < count; i++) {
            if (weights[i] > weights[index]) index = i;
        }
        System.out.println("最重的学员：" + names[index] + "，体重：" + weights[index]);
    }
}
