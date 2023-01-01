package aaa课程代码.day02_运算符;

import java.util.Scanner;

/**
 大于：>
 小于：<
 大于等于：>=
 小于等于：<=
 等于：==
 不等于：!=

 比较运算符的结果一定是true/false

 基本数据类型：可以使用这些比较运算符

 引用数据类型：不可以使用这些比较运算符，除了==，但是要慎用
 */
public class 比较运算符 {
    public static void main(String[] args) {
        //比较两个数的大小
        int a = 30;
        int b = 20;

        if(a > b){
            System.out.println("a>b");
        }else if(a < b){
            System.out.println("a<b");
        }else{
            System.out.println("a=b");
        }

        if(a == 20){
            System.out.println("a等于20");
        }else{
            System.out.println("a不等于20");
        }

        if(a != 20){
            System.out.println("a不等于20");
        }else{
            System.out.println("a等于20");
        }

        String str1 = "hello";
        String str2 = "hello";
        System.out.println(str1 == str2);//true   比较的是两个对象的地址
        //System.out.println(str1 > str2);//错误的，引用数据类型的值之间不能比较大小的

        Scanner input = new Scanner(System.in);
        System.out.print("请输入hello：");
        String s = input.next();

        System.out.println("输入的s = " + s);
        System.out.println(str1 == s);//false  比较的是两个对象的地址  谨慎使用
    }
}
