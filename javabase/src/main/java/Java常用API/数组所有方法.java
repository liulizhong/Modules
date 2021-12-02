package Java常用API;

import java.util.Arrays;
import java.util.List;

/*
    1、数组默认值：
        byte,short,int,long：0
        float,double：0.0
        char：\u0000
        boolean：false
        引用数据类型：例如String默认值都是null
    2、
        (1) Arrays.binarySearch(一维数组名，要查找的值value)：  返回是下标，要求被查找的一维数组必须是有序的，否则结果是错误的
        (2) Arrays.copyOf(原一维数组名，新数组的长度)：  得到一个新的数组，需要接收，新数组的长度可以<,>,=原数组的长度，从原数组的[0]开始复制
        (3) Arrays.copyOfRange(原一维数组名, from ,to)：  得到一个新的数组，需要接收，新数组的长度可以<,>,=原数组的长度，从原数组的[from]开始复制
                from的值必须在原数组的下标范围内，to的值可以在原数组的下标范围外，新数组的长度是to - from，右边不包含！
        (4) Arrays.sort(一维数组名)：  实现从小到大排序
        (5) Arrays.toString(一维数组名)：  把一维数组的元素，拼接为一个字符串，[元素1，元素2，...]
        (6) Arrays.fill(一维数组名，填充的值value)：  把一维数组的每一个元素都填充为value，之前有的元素也替换，和sort一样单列一行就行，不用接收
        (7) Arrays.fill(一维数组名，from，to， 填充的值value)：  把一维数组的[from,to)的元素都填充为value，右边边界不包含。左右都不能越界
        (8) System.arraycopy(一维数组名，复制的起始位置，目标一维数组名，粘贴过来的起始位置，复制长度)，删除下标index元素：System.arraycopy(team,index+1,team,index, length-index-1);
        (9) Arrays.asList(1, 2, "hello", "world", "java")：  返回一个List集合  List<? extends Serializable>  ，只读的，不可更改了
 */

public class 数组所有方法 {
    public static void main(String[] args) {
        // 1、数组静态初始化
        int[] array = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9};
        int array1[] = {9, 8, 7, 6, 5, 4, 3, 2, 1}; // 声明时候可以省略数据类型
        // 2、动态初始化
        int[] array2 = new int[9];  // 元素默认值都是0
        // 3、遍历数组
        for (int i = 0; i < array.length; i++) {  // for循环遍历
            System.out.println(array[i]);
        }
        for (int i : array) {   // froeach遍历
            System.out.println(i);
        }
        // 4、二维数组初始化
        int[][] arrs = new int[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};    // 规则的初始化
        int[] arrs1[] = {{1, 2, 3}};                                     // 另类写法
        int arrs3[][] = new int[][]{{1, 2}, {4, 5, 6}, {7}};            // 每一个元素的数组长度不统一
        // 5、二维数组动态初始化
        int[][] arrs4 = new int[3][4];      // 3行4列的二维数组
        int[][] arrs5 = new int[3][];       // 3行每个元素不确定数组长度
        arrs5[0] = new int[2];              // 初始化二维数组的第一个元素是长度为2的数组
        // 数组方法1：数组长度
        int length1 = arrs5[0].length;
        // 数组方法2：二分查找（2）Arrays.binarySearch(一维数组名，要查找的值value)：返回是下标，要求被查找的一维数组必须是有序的，否则结果是错误的
        int index = Arrays.binarySearch(array, 4);
        // 数组方法3：复制数组（3）Arrays.copyOf(原一维数组名，新数组的长度)：得到一个新的数组，需要接收，新数组的长度可以<,>,=原数组的长度，从原数组的[0]开始复制
        int[] newArray = Arrays.copyOf(array, 11);
        // 数组方法4：复制数组（4）Arrays.copyOfRange（原一维数组名, from ,to）：得到一个新的数组，需要接收，新数组的长度可以<,>,=原数组的长度，从原数组的[from]开始复制
        int[] newArrayFromTo = Arrays.copyOfRange(array, 2, 6); // 得到新数组{4,5,6}
        // 数组方法5：排序数组（5）Arrays.sort(一维数组名)：实现从小到大排序，不用接收
        Arrays.sort(array1);
        // 数组方法6：结果拼接（6）Arrays.toString(一维数组名)：把一维数组的元素，拼接为一个字符串，[元素1，元素2，...]
        String arrayToString = Arrays.toString(array);
        // 数组方法7：填充元素（7）Arrays.fill(一维数组名，填充的值value)：把一维数组的每一个元素都填充为value，之前有的元素也替换，和sort一样单列一行就行，不用接收
        Arrays.fill(array, 99);
        Arrays.fill(array, 3, 5, 55);  // 把一维数组的[from,to)的元素都填充为value，右边边界不包含。左右都不能越界
        // 数组方法8：返回集合（8）Arrays.asList(1, 2, "hello", "world", "java"); 返回一个List集合  List<? extends Serializable>  ，只读的，不可更改了
        List<int[]> arrayAsList = Arrays.asList(array);
        // 数组方法9：复制数组（9）System.arraycopy(一维数组名，复制的起始位置，目标一维数组名，粘贴过来的起始位置，复制长度)，删除下标index元素：System.arraycopy(team,index+1,team,index, length-index-1);
        System.arraycopy(array1,4,array1,3,5); // 复制长度超过数组边界会报错
    }
}
