package aaa课程代码.day06_数组;

import java.util.Arrays;

/**
 多维数组：一维以上的数组都是多维数组，实际开发中，一般就到二维数组。

 例如：存储3个组的学员的成绩
     {89,45,90}
     {99,98,97,90}
     {34,23}

 比喻：一维数组，平房，多个房间
       二维数组，楼房，很多层，每一层有很多个房间

 一般二维数组：多行多列

 以上例子中，表示有3行，每一个有多列，第一行有3列，第二行有4列，第三行有2列

 如何声明二维数组？
    元素的数据类型[][] 数组名;

 二维数组初始化：
     动态初始化：
         （1）数组名 = new 元素的数据类型[行数][列数];  每一行的列数一样的
         （2）数组名 = new 元素的数据类型[行数][];    每一行的列数有可能不一样

 二维数组的长度：即总行数
    数组名.length

 二维数组如何表示一行？
    二维数组名[行下标]  行下标的范围：[0, 数组名.length-1]
 二维数组的行长度，即每一行的列数：
    二维数组名[行下标].length
 二维数组如何表示一个元素？
    二维数组名[行下标][列下标]  列下标的范围[0, 行长度-1]

 二维数组的遍历：
     for(int i=0; i<二维数组名.length; i++){
         for(int j=0; j<二维数组名[i].length; j++){
            System.out.println(二维数组名[i][j]);
         }
     }



 回忆：
     一维数组的声明：
     元素的数据类型[] 数组名;

 一维初始化：
     动态初始化：
     数组名 = new 元素的数据类型[长度];

 一维数组的长度：
    数组名.length

 一维数组的元素：
     数组名[下标]  下标的范围[0,数组的长度-1]

 一维数组的遍历：
     for(int i=0; i<数组名.length; i++){
        System.out.println(数组名[i]);
     }

 一维数组的静态初始化：
     元素的数据类型[] 数组名 = {值列表};
     或
     元素的数据类型[] 数组名;
     数组名 = new 元素的数据类型[] {值列表};

 二维数组静态初始化：
     元素的数据类型[][] 数组名 = {{第一行的值列表},{第二行的值列表}...};
     或：
     元素的数据类型[][] 数组名;
     数组名 = new 元素的数据类型[][] {{第一行的值列表},{第二行的值列表}...};


 理解：可以把二维看成一个特殊的一维数组，把一行看成一个整体，相当于，二维数组中存储了多行
 */
public class 多维数组_语法 {
    public static void main(String[] args) {
        // 【1】、声明：元素的数据类型[][] 数组名;
        //  声明一个二维数组，用来存储成绩
        int[][] scores;

        // 【2】、动态初始化
        //确定行数：数组名 = new 元素的数据类型[行数][];
        scores = new int[3][]; //System.out.println(scores);//二维数组的类型@哈希值，可以看成是地址
        //确定每一行的列数，如{89,45,90}，scores[0]表示第一行
        scores[0] = new int[3];//第一行有3列
        scores[0][0] = 89;//第一行的第一列
        scores[0][1] = 45;
        scores[0][2] = 90;
        scores[1] = new int[3];//第二行有4列
        scores[2] = new int[2];//第三行有2列

        // 【3】、静态初始化：
        // 方法1：静态初始化
        //int[][] arr = {{1},{2,2},{3,3,3},{4,4,4,4},{5,5,5,5,5}};
        // 方法2：动静结合
        int[][] arr = new int[5][];
        arr[0] = new int[]{1};
        arr[1] = new int[]{2,2};
        arr[2] = new int[]{3,3,3};
        arr[3] = new int[]{4,4,4,4};
        arr[4] = new int[]{5,5,5,5,5};

        // 【4】、遍历
//        System.out.println(Arrays.toString(scores)); // 不行的，结果：“[[I@54bedef2, [I@5caf905d, [I@27716f4]”
        // 方法1
        for (int[] score : scores) {
            System.out.println(Arrays.toString(score)); // 可以的，结果[89, 45, 90]，[0, 0, 0]，[0, 0]
        }
        // 方法2
        for (int i = 0; i < scores.length; i++) {
            for (int j = 0; j < scores[i].length; j++) {
                System.out.print(scores[i][j]+" ");
            }
            System.out.println();
        }




    }
}
