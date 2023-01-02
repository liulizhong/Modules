package aaa课程代码.day07_类￥属性;

import java.util.Scanner;

/**
 对象数组：
     1、声明一个圆类型，有属性：半径radius
     2、声明一个圆的数组，要存储3个圆对象
     3、为三个圆的半径赋值，从键盘输入
     4、求三个圆的面积，并显示
     5、按照半径排序
 */
public class  类和属性练习题3_对象数组排序 {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        //数组的声明并初始化的格式：
        //元素的数据类型[] 数组名 = new 元素的数据类型[长度];
        //元素的数据类型？这个数组用来存什么
        //2、声明一个圆的数组，要存储3个圆对象
        Circles[] arr = new Circles[3];//强调一下，这一步没有创建圆对象，只是准备了三个空间，用来装圆对象

        //为圆的半径赋值
        for(int i=0; i<arr.length; i++){
            //(1)先创建圆对象
            arr[i] = new Circles();
            System.out.println("arr[i]");

            //(2)为半径赋值
            System.out.print("请输入第" + (i+1) + "个圆的半径：");
            arr[i].radius = input.nextDouble();
        }

        //遍历
        System.out.println("半径\t面积");
        for(int i=0; i<arr.length; i++){
            System.out.println(arr[i].radius + "\t" + Math.PI * arr[i].radius * arr[i].radius);
        }

        //排序：
        //Arrays.sort(arr);//需要java.lang.Comparable接口

        //冒泡排序：从小到大，每一轮从左到右
        for(int i=1; i<arr.length; i++){
			/*
			当i=1,第一轮，比较2次，j=0,1  j<arr.length-i = 3-1 =2
			当i=2,第二轮，比较1次，j=0    j<arr.length-2 = 3-2 =1
			*/
            for(int j=0; j<arr.length - i; j++){
                //前面的元素比后面的元素大，交换
                if(arr[j].radius > arr[j+1].radius){//对象的属性比较
                    //交换
                    //temp的类型和数组的元素的类型一样
                    Circles temp = arr[j];
                    arr[j] = arr[j+1];
                    arr[j+1] = temp;
                }
            }
        }

        System.out.println("排序后：");
        System.out.println("半径\t面积");
        for(int i=0; i<arr.length; i++){
            System.out.println(arr[i].radius + "\t" + Math.PI * arr[i].radius * arr[i].radius);
        }
    }
}

//声明一个圆类型，有属性：半径radius
class Circles{
    double radius;
}
