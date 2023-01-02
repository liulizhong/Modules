package aaa课程代码.day08_方法;

/**
 可变参数：
 某个方法调用时，实参的个数是可变的

 原来：实参的个数、类型、顺序必须与形参一一对应

 如何声明可变参数？
 要求：
 （1）一个方法只能有一个可变参数
 （2）可变参数必须是最后一个

 格式：
 数据类型... 参数名

 如何给可变参数传值呢？
 可变参数对应的实参，个数可以是0~n个，但是类型必须对应
 特殊的情况：可变参数对应的实参，可以是一个对应类型的数组
 */
public class 类_成员2_方法的可变参数 {
    public static void main(String[] args){
        MyMaths my = new MyMaths();

        System.out.println(my.getSum());

        System.out.println(my.getSum(1,2,3,4,5));

        GraphicToolss tools = new GraphicToolss();
        tools.print(3,5,'*','@','+');

        tools.print(5,10,'*','@','+','&','$');

        int[] arr = {3,4,2,1,7};
        System.out.println(my.getSum(arr));//arr给num传值
    }
}
//数学工具类
class MyMaths{
    //方法：求任意个整数的和
    //任意个：0~n
    public long getSum(int... num){
        long sum = 0;
        //把所有的num的值加起来
        for(int i=0; i<num.length; i++){
            sum += num[i];
        }
        return sum;
    }
}
class GraphicToolss{
    //方法：打印一个n行，m列的矩形，每一行的符号由调用者指定
    //3,5, *,@,+
	/*
	*****
	@@@@@
	+++++
	*/
    public void print(int line, int column, char... sign){
        for(int i=0; i<line; i++){
            for(int j=0; j<column; j++){
                System.out.print(sign[i]);
            }
            System.out.println();
        }
    }

}



