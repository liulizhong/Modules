package aaa课程代码.day05_数组;

import java.util.Scanner;

/**
 * 数组：一组数据
 * <p>
 * 数组(array)就是把“有限的”“数据类型”相同的几个变量用统一的名字进行命名，以便统一管理他们。
 * 其中的每一个数据，我们称为“元素element”，我们可以通过“下标/索引index”进行区别每一个元素。
 * 这个数组中的元素的总个数，我们称为数组的长度(length)
 * <p>
 * 容器：
 * 变量也是容器，但是只能存一个数据
 * 数组是容器，可以存储多个数据
 * <p>
 * 例如：组长把本组学员（大概10或以上）的成绩，用变量保存起来，然后统一管理它们
 * （1）找最高分（2）找最低分（3）平均分（4）统计一下不及格
 * （5）按照成绩的从高到低排序
 * <p>
 * 一、如何声明/定义一个数组？
 * 问题：
 * （1）要存储的这组数的数据类型是什么-->元素的数据类型
 * （2）数组的名称是什么
 * 语法格式：
 * 元素的数据类型[] 数组名;
 * <p>
 * 数组的标记[]
 * <p>
 * 例如：
 * int[] scores;//推荐的写法
 * int scores[];//兼容C程序员的习惯
 * <p>
 * 原因：在Java中有一种引用数据类型，称为数组，元素的数据类型[]
 * <p>
 * <p>
 * 二、如何初始化一个数组
 * 问题：
 * （1）数组的长度，而且一旦确定，就不能修改
 * （2）数组的元素的值
 * <p>
 * 语法格式：
 * 数组名 = new 元素的数据类型[长度];
 * <p>
 * 数组的元素是有默认值的：
 * 元素的类型：
 * byte,short,int,long：0
 * float,double：0.0
 * char: \u0000 即字符编码为0的字符
 * boolean:false
 * 引用数据类型：null
 * <p>
 * 三、语法：
 * 1、声明数组
 * 2、初始化数组[动态初始化、静态初始化]
 * 3、使用
 * <p>
 * 1、如何表示/访问数组的元素
 * 语法格式：
 * 数组名[下标]
 * 下标的范围：0~数组的长度-1
 * <p>
 * 2、遍历数组
 * for(int i = 0; i<数组的长度; i++){
 * 数组名[i]表示元素
 * 变量的声明格式：
 * 数据类型  变量名;
 * 数据类型，可以是基本数据类型，也可以是引用数据类型
 * int[] scores;
 */
public class 一维数组_语法 {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        // 1、声明两个数组
        int[] scores; // 成绩
        String[] names; // 姓名

        // 2.1、静态初始化数组： 数组名 = new  元素的数据类型[]{元素1，元素2，。。。。}
        scores = new int[]{60, 70, 80}; // 这种初始化不能填写数组长度
        int[] scores2 = {60, 70, 80};

        // 2.2、动态初始化数组：数组名 = new 元素的数据类型[长度]，并赋值
        names = new String[10];    //存储10个学员的姓名
        System.out.println(names); // [I@7d4991ad  可以理解为数组的首地址，但是本质上 数组的类型@数组对象的hash值
        System.out.println(names[0]); // 默认值为null
        // 2.2、赋值
        for (int i = 0; i < names.length; i++) {
            System.out.print("请输入第" + i + "个学员的成绩：");
            String name = input.next();
            names[i] = name;
        }

        // 3、使用数组-取数打印
        for (int index = 0; index < 5; index++) {
            System.out.println("第" + (index + 1) + "个学员的成绩：" + scores[index]);
        }
    }
}
