package aaa课程代码.day08_方法;

/**
 原因：String类型比较特殊，它的对象有不可变性，所有的改变都会产生新的String对象

 类似这样的对象，还有包装类对象等
 */
public class 方法练习题3_形参实参_String类型不可变性 {
    public static void main(String[] args){
        String s = "hello";
        change(s);
        System.out.println("s = " + s);
    }

    public static void change(String str){
        	str = "world";  // 相当于：str = new String("world");
        str += "world";     // 相当于：str = new String("helloworld");
    }
}