package aaa课程代码.day04_循环;

/**
     1、流程控制语句关键字：break和continue

     break：可以用于结束switch和循环的关键字
     continue：只能用于循环，提取结束本次循环，跳过剩下的循环体语句

    2、break和continue使用时，可以在想结束的那层循环前面加“标签”，然后在break和continue后边加上标签就行了

    3、循环的终极目的：重复执行某些语句
         循环的四要素：控制好：
         （1）什么时候开始 --》 初始化
         （2）什么时候结束 --》循环条件
         （3）每次的循环变量怎么变 --》迭代语句
         （4）哪些需要循环 --》循环体
 */
public class 流程控制语句_跳转关键字_brak_continue {
    public static void main(String[] args) {
        // 【案例1】：执行到偶数，便结束循环，结果：1
        for(int i=1; i<=5; i++){
            if(i % 2 == 0) break;
            System.out.println(i);
        }
        // 【案例2】：执行到偶数，便跳过此次循环，结果：1、3、5
        for(int i=1; i<=5; i++){
            if(i % 2 == 0) continue;
            System.out.println(i);
        }
    }
}
