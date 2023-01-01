package aaa课程代码.day06_数组;

import java.util.Arrays;

/**
 * 用二维数组存储，并显示
 * 1
 * 2 2
 * 3 3 3
 * 4 4 4 4
 * 5 5 5 5 5
 * <p>
 * 有5行，
 * 第一行：1列
 * 第二行：2列
 * 第三行：3列
 * 第四行：4列
 * 第五行：5列
 */
public class 数组练习9_二维数组三角阵 {
    public static void main(String[] args) {
        int[][] arrs = new int[5][];
        for (int i = 0; i < arrs.length; i++) {
            arrs[i] = new int[i + 1];
            for (int j = 0; j < arrs[i].length; j++) {
                arrs[i][j] = i + 1;
            }
        }
        for (int[] arr : arrs) {
            System.out.println(Arrays.toString(arr));
        }
    }
}
