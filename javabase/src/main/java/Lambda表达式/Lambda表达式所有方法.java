package Lambda表达式;

import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;

/*
    一、Lambda表达式语法：
        (形参列表) -> {Lambda体}
        方法总的格式：
        【修饰符】 返回值类型  方法名（【形参列表】【throws 异常列表】{
                方法体
        }

        Lamda表达式是匿名的函数，它总的格式：
        （【形参列表】) -> {lambda体，即方法体}

        说明：->中间不要空格
    二、说明：
        （1）关于形参列表
            （A）形参列表的类型可以省略；
            （B）如果只一个形参，那么如果类型省略时，()也可以省略，如果类型没省略，那么()不能省略；
            （C）如果不止一个形参，那么不管类型是否省略，()都不可以省略；
            （D）形参名不可以省略，但是形参名可以和函数式接口的抽象方法的形参名不一样
        （2）关于Lambda体
            （A）如果函数式接口的抽象方法有返回值类型，那么{Lambda体}中必须return 返回值语句；
            （B）如果{Lambda体}只一个语句时，那么{;}可以省略
            （C）如果{Lambda体}只一个语句时，而且这是一个return 返回值语句时，那么{return ;}都可以省略
    三、Lambda表达式的四种形式
            Lamda表达式是一个匿名的函数，代表是函数式接口的那个抽象方法，Lambda体就是这个抽象方法的方法体。
             方法：
                 1、Consumer<T>消费型接口：void accept(T t)
                         *  BiConsumer<T,U>：抽象方法  void accept(T t,U u)
                         *  DoubleConsumer；抽象方法   void accept(double value)	//只消费Double类型的
                         *  IntConsumer：抽象方法  void accept(int value)		//只消费Int类型的
                         *  ObjLongConsumer<T>：抽象方法  void accept(T t，Long value）
                2、Supplier<T>供给型接口：T get()
                         *  IntSupplier：抽象方法  int getAsInt()
                         *  DoubleSupplier：抽象方法  double getAsDouble()
                         *  BooleanSupplier：抽象方法  Boolean getAsBoolean()
                3、Function<T, R>函数型接口：R apply(T t)  把lambda体的语句应用到t参数上，并且返回一个R类型的结果
                         *   BiFunction<T,U,R>：抽象方法  R apply(T t, U u)  把lambda体的语句应用到t和u参数上，并且返回一个R类型的结果
                         *   DoubleFunction<R>：抽象方法 R apply(double value)
                         *   DoubleToIntFunction：抽象方法  int applyAsInt(double value)
                         *   IntToDoubleFunction：抽象方法  double applyAsDouble(int value)
                4、Predicate<T>断定型接口：boolean test(T t)
                         *   BiPredicate<T,U>：抽象方法  boolean test(T t, U u)
                         *   DoublePredicate：抽象方法  boolean test(double value)
                         *   IntPredicate：抽象方法  boolean test(int value)
                         *   LongPredicate：抽象方法  boolean test(long value)
    四、方法、构造器引用
           方法引用与构造器引用的作用：对部分Lambda表达式再次简化
           方法引用的语法格式：
           	  （1）实例对象 :: 非静态实例方法名
           	  （2）类名 :: 静态方法名
           	  （3）类名 :: 非静态实例方法名
           	  （4）类名 :: new    我们称为构造器引用
           	  （5）数组类型 :: new

              （1）和（2）的适用场景：
                     ①当发现Lambda体是通过调用一个对象的实例方法或一个类的静态方法来完成的，
                     ②而且这个方法与函数式接口的抽象方法的“形参列表”与“返回值类型”正好一致。
           	  （3）的适用场景：
                    当发现Lambda体是通过调用一个对象的方法完成的，而且这个对象是函数式接口抽象方法的“第一个形参”
                    而且这个方法“形参列表”与函数式接口的抽象方法的“剩下的形参列表”一致，“返回值类型”也一致。
           	  （4）的适用场景：
                    当发现Lambda体是通过new一个对象来完成的，
                    而且new这个对象的构造器的“形参列表”与函数式接口的抽象方法的“形参列表”一致
              例子：Optional<T> 设计这个类的目的是Java8想要解决之前的空指针异常的问题
                   Optional一个容器，这个容器装一个对象
                   T orElseGet(Supplier<? extends T> other) ：当Optional容器中的对象是null时候，用Supplier（供给型它提供的对象代替。
             （5）的适用场景：
                   当发现Lambda体是通过new一个数组对象来完成的，
                   而且new这个数组对象需要的长度正好是函数式接口的抽象方法的“形参列表”一致
                  例如：Function<T,R> R apply(T t)
 */
public class Lambda表达式所有方法 {

    @Test   // （1）无参无返回值
    public void test1() {
        //Thread(Runnable target) 构造器。Runnable接口的抽象方法  public void run()
        // 说明：
        // （1）如果{lambda体}，里面只一条语句，那么{;}可以省略，如果{}没省略，那么;也不能省略。
        // （2）虽然是无参，但是前面的()也不能省略
        new Thread(() -> {
            System.out.println("线程一");
        }).start();
        new Thread(() -> {
            System.out.println("线程二");
        }).start();
    }

    @Test   // （2）无参返回值
    public void test2() {
        // 说明：
        // （1）因为它对应的抽象方法返回值，那么在{Lambda体}中必须"return 返回值;"语句
        // （2）虽然是无参，前面的()也不能省略
        // （3）如果{Lambda体}中只一条语句，那么{return ;}都可以省略
        //		Stream stream = Stream.generate(() -> {return Math.random();});
        Stream stream = Stream.generate(() -> {
            return Math.random();
        });//用Math.random()随机产生小数，形成一个数据流
        stream.forEach((t) -> System.out.println(t));
    }

    @Test   // （3）参无返回值
    public void test3() {
        ArrayList<String> list = new ArrayList<String>();
        list.add("张三");
        list.add("李四");
        list.add("王五");
        // 遍历
//		list.forEach((String element) -> {System.out.println(element);})
//		list.forEach((String element) -> System.out.println(element));
        list.forEach(element -> System.out.println(element));
    }

    @Test   // （4）参返回值
    public void test4() {
        //  说明：
        // （1）如果{lambda体}只一个语句，那么{return ;}可以省略
        // （2）如果(形参列表)只一个形参，那么(数据类型)可以省略
        // （3）如果(形参列表)个数不是一个，但是形参的数据类型是确定的，那么可以省略数据类型，但是()和形参名不能省略
        TreeSet<Teacher> set = new TreeSet<Teacher>((o1, o2) -> o1.id - o2.id);
        set.add(new Teacher(3, "张"));
        set.add(new Teacher(1, "八哥"));
        set.add(new Teacher(2, "老二"));
        set.add(new Teacher(4, "李四"));
        set.forEach(t -> System.out.println(t));
    }

    @Test   // （5）方法、构造器引用
    public void test5() {
        // 1、“实例对象 :: 非静态实例方法名”。这里的Lambda体是通过调用System.out对象的println()方法完成的。
        List<Integer> list = Arrays.asList(1,2,3,4,5,6);
		list.forEach(t -> System.out.println(t));//Consumer<T>  void accept(T t)
        list.forEach(System.out::println);//Consumer<T>  void accept(T t)

        // 2、“类名 :: 静态方法名”。这里的Lambda体是通过调用Math.random()完成的。这个double random() 方法与函数式接口Supplier的抽象方法T get()的“形参列表”与“返回值类型”正好一致。
        Stream<Double> stream = Stream.generate(Math :: random);    //Supplier<T>  T get()
        stream.forEach(System.out :: println);

        // 3、“类名 :: 非静态实例方法名”。Arrays.sort(arr)默认是照字符的Unicode码值排序（严格区分大小写），String类型的自然排序。这里需求是不考虑大小写字典排序用compareToIgnoreCase即可
        String[] arr = {"Hello","hello","abc","world","ABC"};
        //Arrays.sort(arr, (s1,s2)-> s1.compareToIgnoreCase(s2));
        Arrays.sort(arr, String :: compareToIgnoreCase);
        System.out.println(Arrays.toString(arr));

        // 4、“类名 :: new”。我们称为构造器引用
        Optional<Teacher> op = Optional.ofNullable(null);//本类是想装Teacher
        //Optional<Teacher> op = Optional.ofNullable(new Teacher(1, "zhangsan"));
        //Employee emp = op.orElseGet(() -> new Employee());//形参是Supplier，是一个函数式接口，它的抽象方法  T get()
        Teacher emp = op.orElseGet(Teacher::new);//因为这个Lambda体是通过new一个对象完成的，那么我们可以使用构造器引用
        System.out.println(emp);

        // 5、“数组类型 :: new”
        //Object[] arr = MyTools.createArray(len -> new Object[len], 5);
        Object[] objects = MyTools.createArray(Object[]::new, 5);
        System.out.println(objects.length);
    }
}

class Teacher {
    int id;
    String name;
    public Teacher() {
    }
    public Teacher(int id, String name) {
        super();
        this.id = id;
        this.name = name;
    }
    @Override
    public String toString() {
        return "Student [id=" + id + ", name=" + name + "]";
    }
}

class MyTools{
    public static Object[] createArray(Function<Integer,Object[]> fu, int length){
        return fu.apply(length);
    }
}