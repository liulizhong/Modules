package 算法java;

public class 排序_快速排序 {
    public static void main(String[] args) {

        int[] array = {-97,-2,-2,26,-365,88};
        int[] arr = fastSort(0,array,array.length-1);
        for (int  i = 0;i<arr.length;i++){
            System.out.println(arr[i]);
        }
    }

    public static int[] fastSort(int left,int[] array,int right){
        int l = left;
        int r = right;
        //先找到中轴
        int pivotValue = array[(left + right)/2];
        int temp = 0;
        //如果左边的index>右边的index的时候退出
        while (l < r){
            //从左边找到一个大于pivotValue的值
            while (array[l] <pivotValue){
                l++;
            }
            //从右边找到一个小于pivotValue的值
            while (array[r] >pivotValue){
                r--;
            }
            //优化如果l>=r表明左右分解完成
            if (l >= r ){
                break;
            }
            //开始交换
            temp = array[l];
            array[l] = array[r];
            array[r] = temp;
            //这两句代码的意思是 交换后的值 如果和中轴的值相同，那此次不用在比较了，肯定是要么比之前大，要么比之前小 直接比较下一次的值
            if (array[l] == pivotValue){
                r--;
            }
            if (array[r] == pivotValue){
                l++;
            }
        }
        if (l == r){
            l++;
            r--;
        }
        //左递归
        if (left < r){
            fastSort(left,array,r);
        }
        //右递归
        if (right > l){
            fastSort(l,array,right);
        }
        return array;

    }
}
