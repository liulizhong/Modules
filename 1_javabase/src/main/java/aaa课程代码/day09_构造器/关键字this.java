package aaa课程代码.day09_构造器;

/**
 关键字：this

 意思：当前对象
 出现在：
 （1）构造器中：正在new的那个对象
 （2）非静态方法中：调用该方法的对象

 this的用法：
 （1）this.属性
 当成员变量与局部变量重名时，如果要表示成员变量，那么需要在成员变量前面加“this.”，
 即如果没有局部变量与成员变量重名，那么在本类中使用成员变量时，可加可不加。

 （2）this.方法
 表示访问当前对象的其他方法，完全可以省略。

 （3）this()或this(实参列表)
 this()表示访问本类的无参构造
 this(实参列表)表示访问本类的有参构造
 this()和this(实参列表)必须在构造器的首行，第一句。
 要避免递归调用。

 局部变量：
 声明：
 （1）方法体{}
 （2）方法签名中()：形参
 （3）代码块中
 内存存储：栈

 成员变量：
 声明：类中方法外
 内存存储：堆

 局部变量有作用域的问题，成员变量不谈作用域的问题。

 Java找变量：就近原则
 */
public class 关键字this {
    public static void main(String[] args){
        Circles c1 = new Circles(1.5);
        c1.setRadius(2.0);
        System.out.println(c1.getInfo());

        Students s1 = new Students();
        Students s2 = new Students(2,"李四");
        Students s3 = new Students(3,"老三",23,"10086","白庙");
    }
}
class Circles{
    private double radius;

    //无参构造
    public Circles(){

    }
    //有参构造
    public Circles(double radius){
        this.radius = radius;
    }

    public void setRadius(double radius){
        this.radius = radius;
    }
    public double getRadius(){
        return radius;
    }

    public double getArea(){
        return Math.PI * radius * radius;
    }

    public String getInfo(){
        //return "半径：" + radius;
        return "半径： " + this.getRadius() + "，面积：" + this.getArea();
    }
}

class Students{
    private int id;
    private String name;
    private int age;
    private String tel;
    private String address;

    public Students(){
        System.out.println("一个学生来报到...");
    }
    public Students(int id, String name){
        this();
        System.out.println("两个参数的构造器");
        this.id = id;
        this.name = name;
    }
    public Students(int id, String name,int age, String tel, String address){
        //this.id = id;
        //this.name = name;
        this(id,name);
        System.out.println("5个参数的构造器");
        this.age = age;
        this.tel =tel;
        this.address = address;
    }
}