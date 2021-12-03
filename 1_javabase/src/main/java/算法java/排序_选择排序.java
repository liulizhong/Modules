package 算法java;

public class 排序_选择排序 {
    public static void main(String[] args) {
        int[] array = {98,102,35,28,66};
        selectSort(array);
    }
    
    public static void selectSort(int[] array){
        for (int j = 0;j<array.length -1;j++){
            int max = array[j];
            int maxIndex = j;
            int temp = 0;
            for (int i = j+1;i<array.length;i++){
                if (max<array[i]){
                    max = array[i];
                    maxIndex = i;
                }
            }
            if (maxIndex !=j){
                temp = array[j];
                array[j] = array[maxIndex];
                array[maxIndex] = temp;
            }
        }
        for (int i = 0;i<array.length-1;i++){
            System.out.println(array[i]);
        }
    }
}
