package aaa课程代码.day08_方法;

/**
 方法的参数传递机制：
     实参-->形参
     实参的类型、个数、顺序与形参一一对应

 形参是基本数据类型，实参给形参传值：数据值，形参的值修改了，不影响实参

 形参是引用数据类型时，实参给形参传值：地址值，通过形参修改了对象的属性，会影响实参对应的对象的属性值

 原因：String类型比较特殊，它的对象有不可变性，所有的改变都会产生新的String对象。若改变传递过去的一个字符串，实参也不变！！！！
 */

public class 方法练习题2_形参实参变化 {
    public static void main(String[] args){
        // 【1】、基本数据类型的参数传递---结论是实参的值不变
        int x = 1;
        int y = 2;
        System.out.println("before：x = " + x + ",y = " + y);
        MyClass m = new MyClass();
        m.swap(x,y);//实参
        System.out.println("after：x = " + x + ",y = " + y);  // 实参x&y都没变化，形参的a&b互换了值

        // 【2】、引用数据类型的参数传递---结论是实参的值也改变，引用的地址对象的值变了
        MyData myData = new MyData();
        myData.a=99;
        System.out.println("a=" + myData.a);
        m.changeDouble(myData);
        System.out.println("a=" + myData.a);   // 实参my和形参myData指向的是同一个对象地址，对象的属性值变了，实参指向的还是那个对象，所以打印时候显示也变了
    }
}
class MyClass{
    // 希望把a和b的值交换-基本数据类型
    public void swap(int a, int b){//形参
        System.out.println("before：a = " + a + ",b = " + b);
        int temp = a;
        a = b;
        b = temp;
        System.out.println("after：a = " + a + ",b = " + b);
    }

    //形参是引用数据类型，是一个类，是MyData类型
    public void changeDouble(MyData my){
//        my = new MyData(); // 如果加上此代码，则实参打印的结果不变，没有 *2 的效果了，因为方法中my指向了另一个对象地址
        my.a *= 2;
    }

}

class MyData{
    int a;
}