package aaa课程代码.day07_类￥属性;

/**
 一、类的成员之一：属性
    属性：类的数据描述特征

     例如：
         圆：半径
         商品：价格，名称，销量，描述
         学生：姓名、年龄、性别、电话号码、成绩、身高、体重....

     1、如何声明属性
         位置：属性一定是在类中方法外声明
         语法格式：
            [修饰符] 数据类型 属性名;

     2、为属性赋值
         (1)属性有默认值
             和数组的元素的默认值是一样的：
                 byte,short,int,long：0
                 float,double：0.0
                 char：\u0000
                 boolean：false
                 引用数据类型：null
         (2)在其他类中：
            对象.属性 = 值;


     3、如何访问属性
        在其他类中，需要通过“对象.属性”

     4、属性的特点
         （1）每一个对象的属性都是独立
         （2）属性有默认值
 */
public class 类_成员1_属性 {
    public static void main(String[] args) {
        //1、先创建对象
        Circle c1 = new Circle();
        System.out.println("c1对象的半径是：" + c1.radius);//0.0
        c1.radius = 1.2;
        System.out.println("c1对象的半径是：" + c1.radius);//1.2
        c1.radius = 2;
        System.out.println("c1对象的半径是：" + c1.radius);//2.0
        System.out.println("c1的面积：" + Math.PI * c1.radius * c1.radius);

        System.out.println("------------------------------------");

        //创建另一个对象
        Circle c2 = new Circle();
        System.out.println("c2对象的半径是：" + c2.radius);//0.0
        c2.radius = 5.0;

        System.out.println("c1对象的半径是：" + c1.radius);//2.0
        System.out.println("c2对象的半径是：" + c2.radius);//5.0
    }
}

// 声明一个类 - 圆
class Circle{
    //数据类型 属性名;
    double radius;// 类中方法外
//    double radius = 3.0;//方法外  会影响该类的所有的对象的radius的属性值

    //public void test(){
    //	double radius;//方法中，作用域是方法执行完
    //}
}