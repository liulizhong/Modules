package aaa课程代码.day06_数组;

import java.util.Arrays;

/**
 * 4、使用二维数组打印一个 10 行杨辉三角.
 * 1
 * 1 1
 * 1 2 1
 * 1 3 3  1
 * 1 4 6  4  1
 * 1 5 10 10 5 1
 * ....
 * <p>
 * 【提示】
 * 1. 第一行有 1 个元素, 第 n 行有 n 个元素
 * 2. 每一行的第一个元素和最后一个元素都是 1
 * 3. 从第三行开始, 对于非第一个元素和最后一个元素的元素.
 * yanghui[i][j] = yanghui[i-1][j-1] + yanghui[i-1][j];
 * yanghui[2][1] = yanghui[1][0] + yanghui[1][1] = 1+1 =2
 * yanghui[3][1] = yanghui[2][0] + yanghui[2][1] = 1+2 =3
 * yanghui[3][2] = yanghui[2][1] + yanghui[2][2] = 2+1 =3
 * <p>
 * 用二维数组时，要思考这么几个问题？
 * （1）元素的类型是什么？  存什么
 * （2）有几行	 ？
 * （3）每一行有几列？
 * （4）每一个元素的值是什么？
 * <p>
 * （5）如何表示总行数、每一行的列数、每一个元素怎么表示
 */
public class 数组练习9_二维数组杨辉三角 {
    public static void main(String[] args) {
        int[][] arrs = new int[10][];
        arrs[0] = new int[]{1};
        for (int i = 0; i < arrs.length; i++) {
            arrs[i] = new int[i + 1];
            for (int j = 0; j < arrs[i].length; j++) {
                if (j == 0 || j == arrs[i].length - 1) {
                    arrs[i][j] = 1;
                } else {
                    arrs[i][j] = arrs[i - 1][j - 1] + arrs[i - 1][j];
                }
            }
        }
        for (int[] arr : arrs) {
            System.out.println(Arrays.toString(arr));
        }
    }
}
