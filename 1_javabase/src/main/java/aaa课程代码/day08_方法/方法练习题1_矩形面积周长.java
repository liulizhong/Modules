package aaa课程代码.day08_方法;

/**
 1、声明一个矩形类，有属性：长、宽，私有化
 2、提供属性的get/set方法
 3、提供求面积和求周长的方法

 return 返回值;
 (1)返回结果
 (2)结束当前方法

 属性和方法，在本类中方法，可以直接方法，不需要加“对象.”
 */
public class 方法练习题1_矩形面积周长 {
    public static void main(String[] args) {
        Rectangle rectangle = new Rectangle();
        rectangle.setChang(10);
        rectangle.setKuan(6);
        System.out.println(rectangle); // 重写了toString方法，打印的话就是调用toString方法，而不是打印地址
    }
}

class Rectangle {
    private double chang;
    private double kuan;

    public double getChang() {
        return chang;
    }

    public void setChang(double chang) {
        this.chang = chang;
    }

    public double getKuan() {
        return kuan;
    }

    public void setKuan(double kuan) {
        this.kuan = kuan;
    }

    public double getArea() {
        return chang * kuan;
    }

    public double getPerimeter() {
        return (chang + kuan) * 2;
    }

    @Override
    public String toString() {
        return "Rectangle{" +
                "chang=" + chang +
                ", kuan=" + kuan +
                "},面积：" + chang * kuan +
                ",周长：" + (chang*2 + kuan*2);
    }
}
