package aaa课程代码.day02_运算符;

import java.util.Scanner;

/**
 键盘输入：

 Java中的核心类库中有很多的类，其中
 java.lang包下的类型，不需要导包什么，直接用，例如：String，System，Math...
 但是其他包下的类型，要使用，需要用全名称

 为了简化名称，可以使用导包语句
 import 包.类名;

 必须写在源文件的上面，类的外面
 */
public class 键盘输入 {
    public static void main(String[] args) {
        //(1)声明一个键盘输入的工具对象
        //Scanner就是一个键盘输入的工具类，类名首字母大写
        //input是一个变量名，工具名，自己取，按照变量的命名规则和规范即可
        //new Scanner(System.in)新建一个键盘输入的工具对象
        //java.util.Scanner input = new java.util.Scanner(System.in);
        Scanner input = new Scanner(System.in);

        //提示输入，为了界面的友好性
        System.out.print("请输入年龄：");
        //nextInt()是一个方法，表示接下来要输入一个整数
        //方法名的命名规范，第一个单词首字母小写，从第二个单词开始首字母大写
        int age = input.nextInt();

        System.out.println("age = " + age);

        System.out.print("体重：");
        double weight = input.nextDouble();
        System.out.println("weight = " + weight);

        System.out.print("姓名：");
        String name = input.next();
        System.out.println("name = " + name);

        System.out.print("性别：");
        //因为字符类型是字符串类型中的一个字符而已
        //input.next()先得到一个字符串，然后从字符串取一个字符
        char gender = input.next().charAt(0);//Java所有的下标，索引从0开始的
        System.out.println("gender = " + gender);
    }
}
