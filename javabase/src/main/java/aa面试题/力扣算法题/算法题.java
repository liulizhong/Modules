package aa面试题.力扣算法题;

import java.util.HashMap;

// 力扣算法题
public class 算法题 {

    // 1.两数之和
    public static int[] leetcode1(int[] nums, int target) {
        HashMap hashMap = new HashMap<Integer, Integer>();
        for (int i = 0; i < nums.length; i++) {
            if (hashMap.containsKey(target - nums[i])) {
                return new int[]{(Integer) hashMap.get(target - nums[i]), i};
            } else {
                hashMap.put(nums[i], i);
            }
        }
        return null;
    }

    // 2. 两数相加
    public ListNode leetcode2(ListNode l1, ListNode l2) {
        ListNode result = new ListNode(-1);
        ListNode pro = result;
        int t = 0;
        while (l1 != null || l2 != null || t != 0) {
            if (l1 != null) {
                t += l1.val;
                l1 = l1.next;
            }
            if (l2 != null) {
                t += l2.val;
                l2 = l2.next;
            }
            pro.next = new ListNode(t % 10);
            pro = pro.next;
            t = t / 10;
        }
        return result.next;
    }

    // 3. 无重复字符的最长子串
    public int leetcode3(String s) {
        int chang = 0;
        String str = "";
        for (int i = 0; i < (s.length()); i++) {
            String cha = s.charAt(i) + "";
            if (!str.contains(cha)) {
                str += cha;
                System.out.println("不包含str：" + str);
            } else {
                while (str.contains(cha)) {
                    str = str.substring(1);
                    System.out.println("包含-循环str：" + str);
                }
                str += cha;
                System.out.println("包含str：" + str);
            }
            if (chang < str.length()) {
                chang = str.length();
            }
        }
        return chang;
    }

    // 4. 寻找两个正序数组的中位数
    public double leetcode4(int[] nums1, int[] nums2) {
        int length = nums1.length + nums2.length;
        int[] result = new int[length];
        int index1 = 0;
        int index2 = 0;
        int indexResult = 0;
        while (index1 < nums1.length || index2 < nums2.length) {
            if (index1 >= nums1.length) {
                result[indexResult++] = nums2[index2++];
                continue;
            }
            if (index2 >= nums2.length) {
                result[indexResult++] = nums1[index1++];
                continue;
            }
            if (nums1[index1] < nums2[index2]) {
                result[indexResult++] = nums1[index1++];
            } else {
                result[indexResult++] = nums2[index2++];
            }
        }
        if (length % 2 == 0) {
            return (result[length / 2] + result[length / 2 - 1]) / 2.0;
        } else {
            return result[length / 2];
        }
    }

    // 5. 最长回文子串
    public String leetcode5(String s) {
        int len = s.length();
        int maxl = 0;       // 记录最大回文串长度。
        String maxStr = ""; // 记录最大回文字符串。
        int index = 0;      // 下标开始位置
        while (index < len) {
            int left = index;
            int right = index;
            while (index < len - 1 && s.charAt(index) == s.charAt(index + 1)) {
                index++;
                right++;
            }
            while (right < len && left >= 0 && s.charAt(right) == s.charAt(left)) {
                right++;
                left--;
            }
            right--;
            left++;
            if (right - left + 1 > maxl) {
                maxl = right - left + 1;
                maxStr = s.substring(left, right + 1);
            }
            index++;
        }
        return maxStr;
    }

    // 6. Z 字形变换
    public String leetcode6(String s, int numRows) {
        String[] temp = new String[numRows];
        for (int i = 0; i < temp.length; i++) {
            temp[i] = "";
        }
        String result = "";
        if ("".equals(s) || numRows < 1) return result;
        if (numRows == 1) return s;
        for (int i = 0; i < s.length(); i++) {
            int yushu = i % (numRows - 1);
            int beishu = i / (numRows - 1);
            if (beishu % 2 == 0) {
                temp[yushu] += s.charAt(i);
            } else {
                temp[numRows - 1 - yushu] += s.charAt(i);
            }
        }
        for (int i = 0; i < temp.length; i++) {
            if (temp[i] != "") {
                result += temp[i];
            }
        }
        return result;
    }

    // 7. 整数反转
    public int leetcode7(int x) {
        long l = 0;
        while (x != 0) {
            int wei = x % 10;
            l = l * 10 + wei;
            x = x / 10;
        }
        return (int) l == l ? (int) l : 0;
    }

    // 8. 字符串转换整数 (atoi)
    public int leetcode8(String s) {
        return 0;
    }
}
