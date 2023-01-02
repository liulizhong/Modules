package aaa课程代码.day07_类￥属性;

/**
 * 1、练习题
 * 声明一个矩形类Rectangle，并声明它的属性：长、宽，
 * 创建两个对象，并为他们的属性赋值，并计算他们的面积
 * <p>
 * ★ 照葫芦画瓢
 */
public class 类和属性练习题1_矩形类和属性 {
    public static void main(String[] args) {
        //1、创建矩形类Rectangle对象
        Rectangle r1 = new Rectangle();
        r1.length = 2.5;
        r1.width = 1.2;

        //2、创建另一个矩形类Rectangle对象
        Rectangle r2 = new Rectangle();
        r2.length = 3.5;
        r2.width = 3.0;

        System.out.println("r1的面积：" + r1.length * r1.width);
        System.out.println("r2的面积：" + r2.length * r2.width);
    }
}

class Rectangle {
    double length;//长
    double width;//宽
}
