package aaa课程代码.day06_数组;


import java.util.Scanner;

public class 数组练习6_二分查找 {
    public static void main(String[] args) {
        // 当这个数组中的元素是有序的，那么我们可以使用二分查找。
        //    好处：效率高
        int[] arr = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        int left = 0;
        int right = arr.length - 1;
        int index = -1; // 结果的下标
        Scanner scanner = new Scanner(System.in);
        System.out.println("输入要找的数字：");
        int find = scanner.nextInt(); // 要查找的数字
        while (left < right) {
//            System.out.println("循环标记,left=" + left + ",right=" + right + ",z=" + (left + right) / 2);
            if (find > arr[left] && find < arr[(left + right) / 2]) {
                right = (left + right) / 2;
                left++;
            } else if (find > arr[(left + right) / 2] && find < arr[right]) {
                left = (left + right) / 2;
                right--;
            } else if (find == arr[left]) {
                index = left;
                break;
            } else if (find == arr[right]) {
                index = right;
                break;
            } else if (find == arr[(left + right) / 2]) {
                index = (left + right) / 2;
                break;
            } else {
                break;
            }
        }
        if (index == -1) {
            System.out.println(find + "不存在");
        } else {
            System.out.println(find + "的下标是" + index);
        }
    }
}
