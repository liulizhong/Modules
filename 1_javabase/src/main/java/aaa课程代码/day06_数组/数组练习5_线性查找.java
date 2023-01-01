package aaa课程代码.day06_数组;


import java.util.Scanner;

public class 数组练习5_线性查找 {
    public static void main(String[] args) {
        // 线性查找：从头到尾挨个遍历,找到了，就标记，没找到也有特殊标记
        // 从键盘输入一个学员的姓名，看是否在这个数组中,如果找到，就显示找到，否则就显示“不存在”
        String[] arr = {"张超","申康","张超","张嘉飞"};
        Scanner input = new Scanner(System.in);
        System.out.print("请输入学员的姓名：");
        String name = input.next();

        int index = -1; // 因为-1，一定不在下标范围中，即假设不存在
        for(int i=0; i<arr.length; i++){
            if(name.equals(arr[i])){
                System.out.println("找到了，下标是：" + i);
                index = i;
//                System.exit(0); // 结束整个Java程序，下边的代码都不执行了
                break;//结束当前循环
            }
        }

        if(index == -1 ) System.out.println(name + "不存在");
    }
}
