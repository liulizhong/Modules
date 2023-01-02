package aaa课程代码.day08_方法;

/**
 定义一个图形工具类：GraphicTools【求三角形的面积】
 （1）double getTriangleArea(double base, double height)   方法1-参数底和高
 （2）double getTriangleArea(double a, double b, double c) 方法2-参数三个边长度


 break：用于switch和循环
 */
public class 方法练习题4_方法重载 {
    public static void main(String[] args){
        GraphicTools tools = new GraphicTools();

        double area = tools.getTriangleArea(3,5);
        System.out.println("面积：" + area);

        double area2 = tools.getTriangleArea(1,4,5);
        System.out.println("面积：" + area2);
    }
}
class GraphicTools{
    public double getTriangleArea(double base, double height){
        if(base<=0 || height<=0){
            //报错，因为没学异常
            System.out.println("底边或高不能为负数");
            //break;//错误
            //System.exit(0);//太过了
            return 0.0;
        }
        return base *  height / 2.0;
    }

    public double getTriangleArea(double a, double b, double c){
        if(a<0 || b<0 || c<0){
            System.out.println("三角形边长不能为负数！");
            return 0.0;
        }
        if(a+b<=c || b+c<=a || a+c<=b){
            System.out.println(a + "," + b +"," + c +"不能构成三角形");
            return 0.0;
        }

        //海伦公式
        double p = (a + b + c) / 2.0;
        return Math.sqrt(p * (p-a) * (p-b) * (p-c));
    }
}
