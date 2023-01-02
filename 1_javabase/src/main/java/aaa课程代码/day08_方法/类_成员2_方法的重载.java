package aaa课程代码.day08_方法;

/**
 方法的重载（Overload）：
     在同一个类中，出现了方法名称相同，但是形参列表不同的两个或多个方法，称为方法的重载。
     和返回值类型无关。
 */
public class 类_成员2_方法的重载 {
    public static void main(String[] args){
        MyMath my = new MyMath();

        System.out.println(my.max(4,8));        // 8
        System.out.println(my.max(4,8,2));  // 8
        System.out.println(my.max(4.8,2.5));    // 4.8
        System.out.println(my.max(4.8,2));      // 4.8
        System.out.println(my.max(2,4.9));      // 4.8

        //System.out.println(my.max(4.8,2.5,9.5)); // 报错
    }
}

class MyMath{
    //声明这个的一个方法：可以求两个整数中的最大值的方法
    //参数：求两个整数的最大值
    //返回值：最大值
    public int max(int a, int b){
        //return a > b ? a : b;
        System.out.println("两个整数的最大值");

        if(a > b){
            return a;
        }else{
            return b;
        }
    }

    //声明这个的一个方法：可以求三个整数中的最大值的方法
    public int max(int a, int b, int c){
        System.out.println("三个整数的最大值");
        int max = a>b ? a : b;
        max = max > c ? max : c;
        return max;
    }

    //声明这个的一个方法：可以求两个小数中的最大值的方法
    public double max(double a, double b){
        System.out.println("两个小数的最大值");
        return a > b ? a : b;
    }
}