package 算法java;

public class 排序_插入排序 {
    public static void main(String[] args) {
        int[] array = {66,87,25,34,88};
        insertSort(array);
    }

    public static void insertSort(int[] array){
        int[] tempArray = {};
        for (int i = 1;i< array.length;i++){
            int instertValue = array[i];
            int instertIndex = i - 1;
            while (instertIndex >=0 && array[instertIndex] < instertValue){
                array[instertIndex + 1] = array[instertIndex];
                instertIndex --;
            }
            if (instertIndex + 1 !=i){
                array[instertIndex +1] = instertValue;
            }
        }
        for (int i = 0;i<array.length ;i++){
            System.out.println(array[i]);
        }
    }
}
