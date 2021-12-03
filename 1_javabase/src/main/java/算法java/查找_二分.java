package 算法java;

import org.junit.jupiter.api.Test;

import java.util.Scanner;

public class 查找_二分 {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.println("请输入要查找的数字:");

        int number = Integer.parseInt(sc.nextLine());

        int[] array = new int[]{1, 2, 3, 4, 5, 6, 7, 8};
        binarySearch(array, 0, array.length - 1, number);

    }
    //二分查找的数组 必须是有序的
    /*
    二分查找的思路: 比如我们要查找的数是 findVal
    1. arr 是一个有序数组，并且是从小到大排序
    2. 先找到 中间的下标 middle = (leftIndex + rightIndex) / 2, 然后让 中间下标的值和 findVal 进行
    比较
    2.1 如果 arr[middle] > findVal , 就应该向 leftIndex ---- (middle - 1)
    2.2 如果 arr[middle] < findVal , 就应该向 middel+1---- rightIndex
    2.3 如果 arr[middle] == findVal ， 就找到
    2.4 上面的 2.1 2.2 2.3 的逻辑会递归执行

    3. 想一下，怎么样的情况下，就说明找不到[分析出退出递归的条件!!]
    if leftIndex > rightIndex {
        // 找不到..
        return ..
    }
    */

    @Test
    public static void binarySearch(int[] array, int leftIndex, int rightIndex, int findValue) {

        if (leftIndex > rightIndex) {
            System.out.println("没有找到");
        }
        int middleIndex = (leftIndex + rightIndex) / 2;

        if (array[middleIndex] > findValue) {
            binarySearch(array, leftIndex, middleIndex - 1, findValue);
        } else if (array[middleIndex] < findValue) {
            binarySearch(array, middleIndex + 1, rightIndex, findValue);
        } else {
            System.out.println("下标:" + middleIndex + " " + "值:" + array[middleIndex]);
        }
    }
}


