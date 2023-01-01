package aaa课程代码.day06_数组;

import java.util.Arrays;

/**
 （1）Arrays.binarySearch(一维数组名，要查找的值value)：返回是下标，要求被查找的一维数组必须是有序的，否则结果是错误的,返回值说明返回num在arr数组中下标，如果num不存在，返回 (-(插入点) - 1)。
 （2）Arrays.copyOf(原一维数组名，新数组的长度)：得到一个新的数组，需要接收，新数组的长度可以<,>,=原数组的长度，从原数组的[0]开始复制
 （3）Arrays.copyOfRange（原一维数组名, from ,to）：得到一个新的数组，需要接收，新数组的长度可以<,>,=原数组的长度，从原数组的[from]开始复制
                from的值必须在原数组的下标范围内，to的值可以在原数组的下标范围外，新数组的长度是to - from，右边不包含！
 （4）Arrays.sort(一维数组名)：实现从小到大排序，内部用快速排序
 （5）Arrays.toString(一维数组名)：把一维数组的元素，拼接为一个字符串，[元素1，元素2，...]
 （6）Arrays.fill(一维数组名，填充的值value)：把一维数组的每一个元素都填充为value，之前有的元素也替换，和sort一样单列一行就行，不用接收
 （7）Arrays.fill（一维数组名，from，to， 填充的值value）：把一维数组的[from,to)的元素都填充为value，右边边界不包含。左右都不能越界
 （8）System.arraycopy(一维数组名，复制的起始位置，目标一维数组名，粘贴过来的起始位置，复制长度)，删除下标index元素：System.arraycopy(team,index+1,team,index, length-index-1);
 （9）Arrays.asList(1, 2, "hello", "world", "java"); 返回一个List集合  List<? extends Serializable>  ，只读的，不可更改了
 */
public class 数组练习8_工具类Arrays {
    public static void main(String[] args) {
        // （1）Arrays.binarySearch(一维数组名，要查找的值value)
        int[] arr = {1,2,3,4,5};
        int num = 3;
        int index = Arrays.binarySearch(arr,num);
        System.out.println("index = " + index);
        // （2）Arrays.copyOf(原一维数组名，新数组的长度)
        System.out.println("-------------------copyOf-----------------------");
        int[] another = Arrays.copyOf(arr, arr.length*2);
        for(int i=0; i<another.length; i++){
            System.out.println(another[i]);
        }
        // （3）Arrays.copyOfRange（原一维数组名, from ,to）：得到一个新的数组，需要接收，新数组的长度可以<,>,=原数组的长度，从原数组的[from]开始复制
        System.out.println("-------------------copyOfRange-----------------------");
        //                from的值必须在原数组的下标范围内，to的值可以在原数组的下标范围外，新数组的长度是to - from，右边不包含！
        int[] arr2 = {1,2,3,4,5,6,7,8,9};
        int[] another2 = Arrays.copyOfRange(arr2,3,10+1);//如果要包含[6]下标，那么需要6+1
        for(int i=0; i<another2.length; i++){
            System.out.println(another2[i]);
        }
        // （4）Arrays.sort(一维数组名)：实现从小到大排序
        System.out.println("-------------------sort-----------------------");
        int[] arr3 = {4,2,0,7,1,9};
        Arrays.sort(arr3);
        for(int i=0; i<arr3.length; i++){
            System.out.println(arr3[i]);
        }
        // （5）Arrays.toString(一维数组名)：把一维数组的元素，拼接为一个字符串，[元素1，元素2，...]
        System.out.println("-------------------toString-----------------------");
        System.out.println(Arrays.toString(arr3));
        // （6）Arrays.fill(一维数组名，填充的值value)：把一维数组的每一个元素都填充为value，之前有的元素也替换，和sort一样单列一行就行，不用接收
        // （7）Arrays.fill（一维数组名，from，to， 填充的值value）：把一维数组的[from,to)的元素都填充为value，右边边界不包含。左右都不能越界
        // （8）System.arraycopy(一维数组名，复制的起始位置，目标一维数组名，粘贴过来的起始位置，复制长度)，删除下标index元素：System.arraycopy(team,index+1,team,index, length-index-1);
        // （9）Arrays.asList(1, 2, "hello", "world", "java"); 返回一个List集合  List<? extends Serializable>  ，只读的，不可更改了
    }
}
