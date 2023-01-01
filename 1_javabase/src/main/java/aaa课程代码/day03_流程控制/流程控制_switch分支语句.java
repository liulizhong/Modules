package aaa课程代码.day03_流程控制;

import java.util.Scanner;

/**
 一、分支结构
 1、条件判断：if..else
 2、选择结构：switch..case

 二、switch分支
    1、语法结构：
     switch(表达式){
         case 常量值1:
            语句块1;
            【break;】
         case 常量值2:
             语句块2;
            【break;】
         ....
         【
         default:
             语句块n+1；
             【break;】
         】
     }

     2、执行特点：
         1、入口
             （1）当switch(表达式)中表达式的值与其中一个case后面的常量值“相等”，
                 那么就从这个case进入
             （2）当switch(表达式)中表达式的值与所有的case后面的常量值都不匹配，
                 那么找default，如果存在，就从default进入

         2、出口
             （1）自然出口，switch的结束}
             （2）中断出口，break
         3、★ 一旦找到“入口”，就会一直从上往下执行，直到遇到“出口”【从匹配的case往下所有case都执行包括default】


     3、说明：凡是用switch..case可以实现的，用if...else都可以实现。
         只不过很多时候，用switch...case的可读性和运行的效率更高。
         但是switch...case使用的场景要严格一点：一般是等值判断。

     4、要求：
     （1）switch()中的表达式的类型只支持：
            byte,short,char,int四种基本数据类型，枚举（JDK1.5），String（JDK1.7）
     （2）case后面必须是常量
     （3）所有case后面的值不能重复
 */
public class 流程控制_switch分支语句 {
    public static void main(String[] args) {
        /**
         练习1：输入星期几的数字，判断是星期几并输出
         */
        Scanner input = new Scanner(System.in);
        System.out.print("请输入星期值：");
        int week = input.nextInt();
        switch(week){
            case 1:
                System.out.println("星期一");
                System.out.println("Monday");
                break;
            case 2:
                System.out.println("Tuesday");
                break;
            case 3:
                System.out.println("Wednesday");
                break;
            case 4:
                System.out.println("Thursday");
                break;
            case 5:
                System.out.println("Friday");
                break;
            case 6:
                System.out.println("Saturday");
                break;
            case 7:
                System.out.println("Sunday");
                break;
            default:
                System.out.println("输入错误！");
                break;
        }

        /**
         练习2：从键盘分别输入年、月、日，判断这一天是当年的第几天
            注：判断一年是否是闰年的标准：
                1）可以被4整除，但不可被100整除
                2）可以被400整除
         */
        System.out.print("年：");
        int year = input.nextInt();
        System.out.print("月：");
        int month = input.nextInt();
        System.out.print("日：");
        int day = input.nextInt();

        //判断这一天是当年的第几天
        //(1)假设1~month-1的所有的满月天数
        //(2)加上day天
        //(3)当年是否是闰年，如果闰年，2月份要特殊处理
        int days = day;
        //(1)假设1~month-1的所有的满月天数
        switch(month){
            case 12:
                //days = 11月 + 10月 + 9月....;
                //days += 11月的满月天数;
                days += 30;
            case 11:
                //days += 10月的满月天数;
                days += 31;
            case 10:
                //days += 9月的满月天数;
                days += 30;
            case 9:
                days += 31;//8月
            case 8:
                days += 31;//7月
            case 7:
                days += 30;//6月
            case 6:
                days += 31;//5月
            case 5:
                days += 30;//4月
            case 4:
                days += 31;//3月
            case 3:
                days += 28;//2月
            case 2:
                days += 31;//1月
        }
        //(3)当年是否是闰年，如果闰年，2月份要特殊处理
        if(month>2){
			/*
			   注：判断一年是否是闰年的标准：
			   1）可以被4整除，但不可被100整除
			   2）可以被400整除
			   */
            if(year%4==0 && year%100!=0 || year%400==0){
                days++;
            }
        }
        System.out.println(year+"年" + month + "月" + day + "日是这一年的第" + days + "天");
    }
}
