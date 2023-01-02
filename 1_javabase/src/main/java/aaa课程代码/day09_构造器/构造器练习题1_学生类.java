package aaa课程代码.day09_构造器;

/**
 1、编写一个Student类，包含name、gender、age、id、score属性，
     分别为String、String、int、int、double类型。（属性私有化，并提供get/set方法）
     声明一个无参，一个有参的构造器
     类中声明一个say方法，返回String类型，方法返回信息中包含所有属性信息。
     在另一个TestStudent类中的main方法中，创建Student对象，为属性赋值，
     并将调用say()显示结果。

 习惯：
     类{
         属性列表
         构造器列表
         其他方法列表
     }
 */
public class 构造器练习题1_学生类 {
    public static void main(String[] args){
        Student s1 = new Student();//调用无参构造
        Student s2 = new Student("张三","男",23,1,89.5);

        //获取信息（1）一个一个获取，用get方法（2）获取拼接信息 say()
        System.out.println(s1.say());
        System.out.println(s2.say());

        //获取s2的成绩
        System.out.println("s2的成绩：" + s2.getScore());

        //修改s2的成绩
        s2.setScore(100);
        System.out.println("s2的成绩：" + s2.getScore());
    }
}
class Student{
    private String name;
    private String gender;
    private int age;
    private int id;
    private double score;

    //显式声明无参构造
    public Student(){
    }
    //显式声明有参构造
    public Student(String n,String g, int a, int i, double s){
        name = n;
        gender = g;
        age = a;
        id = i;
        score = s;
    }

    public String say(){
        return "姓名：" + name + "，年龄：" + age +"，学号：" + id + "，性别：" + gender +",成绩：" + score;
    }

    public void setName(String n){
        name = n;
    }
    public String getName(){
        return name;
    }
    public void setGender(String g){
        gender = g;
    }
    public String getGender(){
        return gender;
    }
    public void setAge(int a){
        age = a;
    }
    public int getAge(){
        return age;
    }
    public void setId(int i){
        id = i;
    }
    public int getId(){
        return id;
    }
    public void setScore(double s){
        score = s;
    }
    public double getScore(){
        return score;
    }
}