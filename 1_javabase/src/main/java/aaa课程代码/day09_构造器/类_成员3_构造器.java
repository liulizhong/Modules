package aaa课程代码.day09_构造器;


public class 类_成员3_构造器 {
    public static void main(String[] args){
        Circle c = new Circle();//new 右边的就是构造器，调用的是默认的无参构造

        Rectangle r1 = new Rectangle();//调用无参构造
        Rectangle r2 = new Rectangle(2, 3.5);//2给l，3.5给w  调用有参构造
        System.out.println(r1.getInfo());
        System.out.println(r2.getInfo());

        //Triangle t1 = new Triangle();//调用无参构造，错误
    }
}


class Circle{
    private double radius;
    //有默认的无参构造
}


class Rectangle{
    private double length;
    private double width;

    //显式声明无参构造
    public Rectangle(){

    }
    //显式声明有参构造
    public Rectangle(double l, double w){
        //为属性赋值
        length = l;
        width = w;
    }

    public String getInfo(){
        return "长：" + length + "，宽：" + width;
    }
}


class Triangle{
    private double side1;
    private double side2;
    private double side3;

    //显式声明有参构造，没有默认的无参构造了
    public Triangle(double s1, double s2, double s3){
        side1 = s1;
        side2 = s2;
        side3 = s3;
    }
}