package aaa课程代码.day01_数据类型;


public class 数据类型测试 {
    public static void main(String[] args) {
        // 1、测试Boolean类型
        boolean isMarry = false;
        if(isMarry == true){
            System.out.println("已婚");
        }else{
            System.out.println("未婚");
        }

        // 2、测试小数类型
        float d = 1.1111111111111111111f; // 小数默认类型是double，float类型的话必须指定 F或f
        double d2 = 1123456789123456.789123;
        System.out.println("d=" + d);   // 输出：d=1.1111112
        System.out.println("d2=" + d2); // 输出：d2=1.1234567891234568E15【会转化为科学记数法】

        byte b1 = 12;
        byte b2 = 12;
        System.out.println("b1= " + b1);  // 输出：b1= 12
        System.out.println("b2= " + b2);  // 输出：b2= 12 //因byte最大就是12

        char sex = '男';
        System.out.println("sex=" + sex); // 输出：sex=男

        System.out.println("姓名\t年龄"); // 输出：“姓名	年龄” // 相当于tab键
        System.out.println("张三\t18");   // 输出：“张三	18”

        char a = 'x';
        char tab = '\t';
        char c = 'y';
        System.out.println(a + tab + c);   // 输出：250  //char相加就是求和
        System.out.println("结果：" + a + tab + c);   // 输出：“结果：x	y” // 会转化为String类型

        char num1 = '0';//0字符
        char num2 = '1';
        System.out.println(num1 + num2);    // 输出：97
        System.out.println("柴\t林\t燕");   // 输出：“柴	林	燕”

        char shang = '\u5c1a';
        System.out.println(shang);   // 输出：尚

        long bigNum = 123456789012345678L;
        System.out.println("bigNum=" + bigNum);   // 输出：bigNum=123456789012345678
//        int g = 2999999999;                // 超出最大范围会报错
//        System.out.println("g = " + g);    // 打印时会按照最大值打印
    }
}
