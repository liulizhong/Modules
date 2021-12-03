package aa面试题.力扣算法题;

/**
 * @author lizhong.liu
 * @version TODO
 * @class ??
 * @CalssName 验算算法
 * @create 2020-10-30 17:45
 * @Des TODO
 */
public class 验算算法 {
    public static void main(String[] args) {
        算法题 sft = new 算法题();
//        System.out.println(sft.leetcode4("a"));
//        System.out.println(fbnq(11));
//        System.out.println(-123/10 + ":" + -123%10);
//        System.out.println(123/10 + ":" + 123%10);
        System.out.println((int)1534236469);
        System.out.println((int)2147483647);
        System.out.println((int)2147483648L);
        System.out.println(Integer.MAX_VALUE);
        System.out.println(Integer.MIN_VALUE);
    }

    public static int fbnq(int num) {
        if (num == 0) {
            return 0;
        } else if (num == 1) {
            return 1;
        } else if (num >= 3) {
            int result = 0;
            int tmp1 = 0;
            int tmp2 = 1;
            for (int i = 2; i < num; i++) {
                result = tmp1 + tmp2;
                tmp1 = tmp2;
                tmp2 = result;
            }
            return result;
        }
        return 0;
    }
}
