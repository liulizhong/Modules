package aaa课程代码.day04_循环;

/**
 * 二、while循环
 * 语法结构：
 * while(循环条件){
 * 循环体：需要重复执行的代码块
 * }
 * <p>
 * 执行过程：
 * （1）判断循环条件
 * （2）如果成立，那么就执行循环体，然后回到（1）
 * 如果不成立，直接结束while
 * <p>
 * 执行特点：
 * （1）while循环可能一次都不执行
 * （2）一般适用于循环次数不明显的
 * <p>
 * 从键盘输入一个整数，依次输出它的各个位数上的值
 * 例如：
 * 输入：123456
 * 输出：
 * 6
 * 5
 * 4
 * 3
 * 2
 * 1
 * <p>
 * 输入：456
 * 6
 * 5
 * 4
 */
public class 循环语句_while循环 {
    public static void main(String[] args) {
        // 语法案例：输入1234567890，按照0、9、8、7、6、5、4、3、2、1换行输出
        int i = 1234567890;
        while (i != 0) {
            System.out.println(i % 10);
            i = i / 10;
        }
        /*
            (1)while先判断条件  12345!=0  成立
            （2）wei = 5;  System.out.println(wei);  num = 1234;
            （3）while先判断条件  1234!=0  成立
            （4）wei = 4;  System.out.println(wei);  num = 123;
            （5）while先判断条件  123!=0  成立
            （6）wei = 3;  System.out.println(wei);  num = 12;
            （7）while先判断条件  12!=0  成立
            （8）wei = 2;  System.out.println(wei);  num = 1;
            （9）while先判断条件  1!=0  成立
            （10）wei = 1;  System.out.println(wei);  num = 0;
            (11)while先判断条件  0!=0  不成立
		*/
    }
}
