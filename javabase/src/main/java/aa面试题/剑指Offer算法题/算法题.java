package aa面试题.剑指Offer算法题;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

public class 算法题 {

    // 1.二维数组中查找
    public boolean 剑指1(int target, int[][] array) {
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[i].length; j++) {
                if (target < array[i][j]) {
                    j = array[i].length + 1;
                } else if (target == array[i][j]) {
                    return true;
                }
            }
        }
        return false;
    }

    // 2.替换空格
    public String replaceSpace(StringBuffer str) {
        return str.toString().replaceAll(" ", "%20");
    }

    // 3.从尾到头打印链表
    public ArrayList<Integer> printListFromTailToHead(ListNode listNode) {
        ArrayList<Integer> arrayList = new ArrayList<>();
        if (listNode != null) {
            if (listNode.next != null) {
                arrayList = printListFromTailToHead(listNode.next);
            }
            arrayList.add(listNode.val);
        }
        return arrayList;
    }

    // 4.重建二叉树
    public TreeNode reConstructBinaryTree(int[] pre, int[] in) {
        TreeNode treeNode = new TreeNode(3);
        return treeNode;
    }
}
