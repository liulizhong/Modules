package aaa课程代码.day04_循环;

/**
     循环结构：
         1、普通的for
         2、while
         3、do...while
         4、增加for,foreach  JDK1.5之后引入

     foreach：只能用于遍历数组或集合等容器。
         语法格式：
             for(元素的数据类型 元素名称 : 数组名称){
             元素名称 代表元素
             }
     元素名自己取，是一个临时变量名而已。

 for和foreach的选择：
 （1）如果是显示数组的元素，在数组中查找，那么两个皆可以
 （2）如果在操作的过程中需要index下标的信息，那么只能选择for
 （3）如果想要修改数组的元素（即赋值），那么只能选择for

 结论：foreach可以用于遍历数组，但是实际开发中，foreach更多的是用于集合，
 那么数组更多的选择的是for
 */
public class 循环语句_foreach循环 {
    public static void main(String[] args) {

        // 【1】、for和foreach简单实现
        int[] arr = {34,5,6,2,1};
        //遍历数组
        //普通for
        for(int i=0; i<arr.length; i++){
            System.out.print(arr[i] + "\t");
        }
        System.out.println();
        System.out.println("-------------------分割线------------------");
        for(int num : arr){
            System.out.print(num + "\t");
        }

        // 【2】、for和foreach 对比：修改数组元素，只能用for，便利数组用foreach更高效
        String[] weeks = {"monday","tuesday","wednesday","thursday","friday","saturday","sunday"};
        for(int i = 0; i<weeks.length; i++){
            weeks[i] = weeks[i].toUpperCase();
        }
//        for(String week : weeks){  // 此种方法不会生效，week只是临时代表元素，并不是元素本身
//            week = week.toUpperCase();
//            //System.out.println(week);
//        }
        //遍历数组weeks
        for(String week : weeks){
            System.out.println(week);
        }
    }
}
