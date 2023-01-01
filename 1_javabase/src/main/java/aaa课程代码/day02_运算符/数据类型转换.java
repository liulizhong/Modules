package aaa课程代码.day02_运算符;

/**
 数据类型的转换：
 1、小->大：自动类型转换
 byte,short,char->int->long->float->double
 （1）当小的类型的值或变量赋值给大的类型的变量时，会发生自动类型转换
 （2）如果是byte与byte，short与short，char与char，byte,short,char之间的混合，自动升级为int
 （3）所有类型与String字符串进行“+”都会变成字符串

 2、大->小
 double->float->long->int->byte,short,char
 （1）当大的类型的值或变量赋值给小的类型的变量时，需要强制类型转换()
 （2）强制类型转换有风险的：可能会溢出或者损失精度
 （3）把某个类型的变量或值，强制升级为某个大的类型，也需要强制类型转换符()
 （4）String类型不能强制转为基本数据类型

 注意：boolean不参与

 了解：整型系列与浮点型系列底层存储有点不一样，虽然都是二进制
 float、double是存储（符号位、指数部分、尾数部分），比整型系列的存储的值要大
 float、double转成二进制时，又可能乘不尽，所以会出现截取，这样不精确
 */
public class 数据类型转换 {
    public static void main(String[] args) {
        //变量的类型是int
        //值的类型是：char
        int a = 'a';
        System.out.println("a = " + a); // 输出：“a = 97” //char 转化为 int，变成具体的值

        int num = 100;
        //左边d变量的类型是double
        //右边num的类型是int
        double d = num;
        System.out.println("d = " + d); //输出：d = 100.0

        byte b1 = 1;
        byte b2 = 2;
//        byte b3 = b1 + b2;  // 会报错，相加后结果是int类型
//        System.out.println("b3 = " + b3);

        char c1 = 'a';
        char c2 = 'b';
        //	char c3 = c1 + c2;
        System.out.println(c1 + c2);            //输出：195
        System.out.println("结果：" + c1 + c2); //输出："结果：ab"

        int letter = 97;
        //左边zi变量是char
        //右边的letter是int
        //把int的赋值给char，把大给小的
        char zi = (char)letter;
        System.out.println("zi = " + zi);  //输出："zi = a"

        double dou = 1.4;
        int zheng = (int)dou;
        System.out.println("zheng = " + zheng); //输出："zheng = 1"

        long length = 123456789101111L;
        int sh = (int)length;
        System.out.println("sh = " + sh); //输出："sh = -2045822409" // 因为int装不下，把结果进行截取了，不只是损失精度

        int x = 1;
        int y = 2;
        //整数/整数，结果只会保留整数部分
        //把x强制升级为double
        //然后y就会自动升级为double
        double shang = (double)x/y;
        System.out.println("商：" + shang); //输出："商：0.5"

        String str = "123";
        //int iNum = (int)str;//错误

        char f1 = 'a';
        char f2 = 'b';
        char f3 = (char)(f1 + f2);
        System.out.println(f3);// 输出：“Ã” // 控制键，控制符号
    }
}
