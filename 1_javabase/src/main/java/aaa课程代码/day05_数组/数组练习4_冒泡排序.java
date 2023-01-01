package aaa课程代码.day05_数组;


public class 数组练习4_冒泡排序 {
    public static void main(String[] args) {
        // 【4】、冒泡排序
        int[] arr = {1, 3, 7, 9, 2, 8, 6, 2, 1, 0};
        for (int i = 0; i < arr.length - 1; i++) {
            for (int j = 0; j < arr.length - i - 1; j++) {
                if (arr[j] > arr[j + 1]) {
                    int temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                }
            }
        }
        System.out.println("---------------------冒泡-------------------------------" + arr.length);
        for (int i : arr) {
            System.out.println(i);
        }
    }
}
