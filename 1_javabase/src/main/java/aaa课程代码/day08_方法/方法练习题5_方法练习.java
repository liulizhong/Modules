package aaa课程代码.day08_方法;

/**
 5、声明一个三角形类，
 （1）属性有三条边：side1,side2, side3，私有化，并提供get/set
 （2）方法1：判断三条边的值是否可以组成一个三角形 boolean isTriangle()
 （3）方法2：判断是否是直角三角形boolean isRightTriangle()
 如果side1,side2,side3的值不能构成三角形，或不是直角三角形，都返回false
 （4）方法3：判断是否是等腰三角形boolean isIsoscelesTriangle()
 如果side1,side2,side3的值不能构成三角形，或不是等腰三角形，都返回false
 （5）方法:4：获取三角形的面积 double getArea()  提示：海伦公式
 （6）方法5：获取三角形的周长double getPerimeter()
 */
public class 方法练习题5_方法练习 {
}

class Triangle {
    private double b1;
    private double b2;
    private double b3;

    public Triangle() {
    }

    public Triangle(double b1, double b2, double b3) {
        this.b1 = b1;
        this.b2 = b2;
        this.b3 = b3;
    }

    //  （2）方法1：判断三条边的值是否可以组成一个三角形 boolean isTriangle()
    public boolean isTriangle() {
        if (b1 + b2 > b3 & b1 + b3 > b2 & b3 + b2 > b1) {
            return true;
        }
        return false;
    }
    // （3）方法2：判断是否是直角三角形boolean isRightTriangle()
    public boolean isRightTriangle() {
        if (!isTriangle()) {
            return false;
        }
        if (b1*b1 + b2*b2 == b3*b3 || b1*b1 + b3*b3 == b2*b2 || b3*b3 + b2*b2 == b1*b1) {
            return true;
        }
        return false;
    }
    // （4）方法3：判断是否是等腰三角形boolean isIsoscelesTriangle()
    public boolean isIsoscelesTriangle() {
        if (!isTriangle()) {
            return false;
        }
        if (b1 == b2 || (b1 == b3 || (b3 == b2))) {
            return true;
        }
        return false;
    }
    // （5）方法:4：获取三角形的面积 double getArea()  提示：海伦公式
    public double getArea(){
        if(!isTriangle()){//不是三角形
            return 0.0;
        }

        double p = (b1 +b2 + b3)/2;
        return Math.sqrt(p * (p-b1) *(p-b2)*(p-b3));
    }
    // （6）方法5：获取三角形的周长double getPerimeter()
    public double getPerimeter(){
        if(!isTriangle()){//不是三角形
            return 0.0;
        }
        return b1 +b2 +b3;
    }
}
