package 算法java;

import org.junit.jupiter.api.Test;

public class 排序_冒泡 {
    public static void main(String[] args) {
        int[] array = new int[] {28,36,25,29,21};
        bubbleSort(array);
    }

    //冒泡排序算法
    @Test
    public static void bubbleSort(int[] array){
        for (int i = 0;i<array.length -1;i++){
            for (int j = 0;j<array.length -1 -i;j++){
                int temp = 0;
                if (array[j] < array[j+1]){
                    temp = array[j];
                    array[j] = array[j+1];
                    array[j+1] = temp;
                }
            }
            System.out.println(array[i]);
        }
        
    }


}
