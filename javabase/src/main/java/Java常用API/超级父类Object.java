package Java常用API;

/*
    （1） public boolean equals(Object obj)：做比较。区别：==和equals
            如果子类没重写equals()方法，那么Object默认的equals方法的实现，和“==”是一样的。
            如果子类希望equals()比较的不是对象的地址，而是其他信息，例如：属性的值等，那么就要重写equals方法。例如：String等都是重写了equals方法
    （2） public int hashCode()：返回哈希值，例子用int接受的，equals和hashCode方法一般形影不离，总是同时被重写
    （3） public Class getClass()：返回某个对象的运行时类型（个人理解包名.属性/类）
    （4） protected void finalize()：方法名object子类：这个方法是由GC（垃圾回收程序）调用，不是由程序员调用。
            System.gc();//这个通知垃圾回收器过来回收，但是实际什么时候来，还要看他自己的安排
    （5） public String toString()：   //打印输出方法sout调用
    （6） <clint>         //类静态加载方法
    （7） <init>          //类初始化方法
        以下见《Java常用API/比较类Comparable和Comparator.java》类
    （8）Comparable<T>   //自然比较规则、默认比较规则，一般类继承它，然后重写 comparaTo() 即可完成定制比较
    （9）comparaTo()     //Comparable的比较规则
    （10） Comparator<T>  //定制比较器，抽象方法compare
    （11） compare        //Comparator的比较规则
 */
public class 超级父类Object {
    public static void main(String[] args) {
        //（1）public boolean equals(Object obj)：做比较
        //      区别：==和equals
        //      如果子类没重写equals()方法，那么Object默认的equals方法的实现，和“==”是一样的。
        //      如果子类希望equals()比较的不是对象的地址，而是其他信息，例如：属性的值等，那么就要重写equals方法。
        //      例如：String等都是重写了equals方法
        boolean equboolean = "哈哈".equals("哈哈");

        //（2）public int hashCode()：返回哈希值，例子用int接受的
        //      hashCode()返回当前对象的哈希值，这个值只是在当把对象放到类似于“哈希表”等容器中时才用，其他时候没用。
        //      后面会学习把对象放到“Hashtable，HashMap，HashSet等集合容器”中时，才会用到。
        //      equals和hashCode方法形影不离，总是同时被重写。而且参与equals比较的属性，一定要参与hashCode值的计算。
        int hashCode = "哈哈".hashCode();

        // （3）public Class getClass()：返回某个对象的运行时类型（个人理解包名.属性/类）
        Class<? extends String> aClass = "哈哈".getClass();

        // （4）protected void finalize()：方法名object子类：这个方法是由GC（垃圾回收程序调用，不是由程序员调用。
        //      什么是垃圾？MyData my = new MyData();//for中的局部变量，每循环一次，上一次的my就失去生命了，右边的对象就是垃圾了
        //      Java的垃圾回收器的特点：它什么时候来回收，是不确定的。一般（1）内存吃紧了（2）通知它来回收（3）System.gc();
        //      这个通知垃圾回收器过来回收，但是实际什么时候来，还要看他自己的安排
        //      什么时候调用？什么样的代码会写在这个方法中？一般是资源对象会重写这个方法，然后会在这个方法中写彻底释放资源的代码。资源对象：IO流，数据库连接
        //      这个方法一个特点？如果在finalize()方法中，使得这个对象“复活”了，（即在方法中，使得另一个有效变量指向当前对象this，那么当前对象this就“复活”，
        //				一旦“复活”垃圾回收器就不能回收他了，等他下次再次变成垃圾后，垃圾回收器，就不再第二次调用这个对象的finalize方法，直接回收。
        //				即每一个对象的finalize()只能被调用一次。
        System.gc();

        // （5）public String toString()：
        "哈哈".toString();
    }
}
