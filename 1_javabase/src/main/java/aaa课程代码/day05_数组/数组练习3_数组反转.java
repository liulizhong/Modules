package aaa课程代码.day05_数组;


public class 数组练习3_数组反转 {
    public static void main(String[] args) {
        // 【3】、数组反转：{1,2,3,4,5} -> {5,4,3,2,1}
        int[] nums = {1, 2, 3, 4, 5, 6, 7, 8, 9};
        for (int i = 0; i <= nums.length / 2 - 1; i++) {
            int temp = nums[i];
            nums[i] = nums[nums.length - 1 - i];
            nums[nums.length - 1 - i] = temp;
        }
        for (int num : nums) {
            System.out.println(num);
        }
    }
}
