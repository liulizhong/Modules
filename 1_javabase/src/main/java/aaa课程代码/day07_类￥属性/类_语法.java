package aaa课程代码.day07_类￥属性;

/**
 一、类的概念
     类(class)：一类具有相同特性的事物的抽象描述，用一个Java类表示。
     例如：
         张超、申康、陈万八.... --> Student类
         姓名、性别、年龄...  -->数据描述特征
         吃饭、睡觉、学Java...  -->行为/功能描述特征

     类是多个对象的共性的抽取。
        对象(object/instance)是类的具体的个体。

     类是对象的设计模板，设计图。
         对象是类的实体。

     例如：
         宋红康、柴老师、苍老师....
         姓名、性别、年龄...  -->数据描述特征
         吃饭、睡觉、讲授技术....-->行为/功能描述特征

 二、类的声明和使用
    类必须先声明后使用

     一、如何声明一个类
         语法格式：
             [public等其他修饰符] class 类名称{
                类的成员列表;
             }

     class：声明类的关键字
         类名称：（1）遵循标识符的命名规则（2）遵循标识符的命名规范：每一个单词首字母大写，并且见名知意

     二、如何创建对象
         类名 对象名/变量名 = new 类名();
         说明：对象名/变量名 存储的是对象的首地址


     回忆：
         Scanner input = new Scanner(System.in);
         Random rand = new Random();
         Arrays a = new Arrays();//错误：Arrays()可以在Arrays中访问private【私有的方法】
 */

// 类的声明
public class 类_语法 {
    public static void main(String[] args) {
        // 创建类对象
        Student student = new Student();
        student.eat(); // 父类的方法调用
        student.study(); // 自己特有的方法
        new Teacher().teach();
        // 也可以创建本类的对象
        类_语法 testClass = new 类_语法();
    }
}

// 类的声明：基础类-人
class Person{
    //数据描述特征-->属性
    String name;
    String gender;
    int age;

    //方法
    void eat(){
        System.out.println("一顿不吃饿得慌");
    }
    void sleep(){
        System.out.println("听着柴老师的课睡的香");
    }
}

// 类的声明：学生类【算人的一种】
class Student extends Person{
    // 特有的方法
    void study(){
        System.out.println("Good good study , day day up");
    }
}

// 类的声明：老师类【算人的一种】
class Teacher extends Person{
    // 特有的方法
    void teach(){
        System.out.println("讲授毕生的技术...");
    }
}
