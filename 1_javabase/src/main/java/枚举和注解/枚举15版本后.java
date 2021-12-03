package 枚举和注解;

/*
     JDK1.5之后实现枚举
     1、语法格式
     【修饰符】 enum 枚举名{
     		常量对象列表
     }
     【修饰符】 enum 枚举名{
     		常量对象列表;
     		其他的成员
     }
     说明：
     常量对象列表必须在枚举类型的首行
     枚举只是一个特殊的类：
     （1）对象有限个
     （2）构造器私化

     例如：星期Week

     回忆：声明类型的关键字
     class --》类
     interface--》接口
     enum--》枚举

     回忆：首行
     （1）package 包名;  声明包，在源文件的首行
     （2）this()或this(实参列表);  必须在构造器首行
     （3）super()或super(实参列表); 必须在子类构造器首行
     （4）枚举常量对象列表; 必须在枚举类中的首行
 */
public class 枚举15版本后 {
    public static void main(String[] args) {
//		Week w = new Week();//错误，因为Week的构造器默认就被私化

        Week w1 = Week.FRIDAY;
        System.out.println(w1);

        Week w2 = Week.MONDAY;
        System.out.println(w2);
    }
}

enum Week{
    MONDAY,TUESDAY,WEDNESDAY,THURSDAY,FRIDAY("星期五"),SATURDAY,SUNDAY;
    private String des;

    private Week(String des) {
        this.des = des;
    }

    private Week() {
    }

    public String toString(){
        return "今天是：" + des;
    }

}