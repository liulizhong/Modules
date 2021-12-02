package 算法java;

import org.junit.jupiter.api.Test;

import java.util.Scanner;

public class 查找_顺序查找 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("请输入要查找的字符串:");

        String name = sc.nextLine();

        String[] array = new String[]{"123","abc","kfc","456"};

        findFunc02(array,name);


    }

    @Test

    //顺序查找方式1：
    public static void findFunc01(String[] array,String name){

        for (int i = 0;i<array.length;i++){
            if (name.equals(array[i])){
                System.out.println("找到了下标为:" + i + " " + "内容为:" + array[i] );
                break;
            }else if (i == array.length -1){
                System.out.println("没有找到");
            }
        }

    }

    @Test
    //顺序查找方式2:
    public static void findFunc02(String[] array,String name){
        int index = -1;
        for (int i = 0;i<array.length;i++){
            if (name.equals(array[i])){
                index = i;
                break;
            }
        }
        if (index!=-1){
            System.out.println("找到了下标为:" + index + " " + "内容为:" + array[index] );
        }
        else {
            System.out.println("没有找到");
        }
    }
}
