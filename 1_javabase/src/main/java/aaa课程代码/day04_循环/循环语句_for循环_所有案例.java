package aaa课程代码.day04_循环;


public class 循环语句_for循环_所有案例 {
    public static void main(String[] args) {

        // 【案例1】、打印1-100的偶数，并求出所有偶数的累加和
        int sum = 0;
        for (int i = 0; i <= 100; i++) {
            if (i % 2 == 0) {
                System.out.println(i + "是偶数");
                sum += i;
            }
        }
        System.out.println("1~100的偶数求和为：" + sum);

        // 【案例2】、输出所有的水仙花数，所谓水仙花数是指一个3位数，其各个位上数字立方和等于其本身。
        //    例如： 153 = 1*1*1 + 5*5*5 + 3*3*3
        //
        //	3位数:[100,999]
        //
        //	判断某个数是否是水仙花数，这个需要重复，
        //	需要重复[100,999]
        for (int i = 100; i <= 999; i++) {
            int bai = i/100;
            int shi = i%100/10;
            int ge  = i%10;
            if (i == bai * bai * bai + shi * shi * shi + ge * ge * ge) {
                System.out.println("数字" + i + "是水仙花数字！！");
            }
        }

        // 【案例3】、9*9乘法表
        for (int i = 1; i <= 9; i++) {
            for (int j = 1; j <= i; j++) {
                System.out.print(j+"*"+i+"="+j*i+"\t");
            }
            System.out.println();
        }

        // 【案例4】、编写程序FooBizBaz.java，从1循环到150并在每行打印一个值，
        //    要求：另外在每个3的倍数行上打印出“foo”,在每个5的倍数行上打印“biz”,在每个7的倍数行上打印输出“baz”。
        for (int i = 1; i <= 150; i++) {
            System.out.print(i);
            if (i % 3 == 0) System.out.print("   foo");
            if (i % 5 == 0)  System.out.print("   biz");
            if (i % 7 == 0) System.out.print("   baz");
            System.out.println();
        }
    }
}
