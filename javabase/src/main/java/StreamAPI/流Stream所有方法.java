package StreamAPI;

import org.apache.poi.ss.formula.functions.T;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.*;

/*
    一、创建Stream
            Arrays.stream(T[] array);             //数组形式创建
            arrayList.stream();                   //集合形式创建
            Stream.of("hello","world","java");    //返回数据流(of里边值)
            Stream.iterate()                      //创建无线流。如：Stream.iterate(1, t -> t+2);
            Stream.generate()                     //创建无限流。如：Stream.generate(Math::random);
    二、中间操作
            filter(Predicate p)                  //接收Lambda?，?从流中排除某些元素
            distinct()                            //筛，通过流所生成元素的HashCode()?和Equals()?去除重复元素
            limit(long maxSize)                  //截断流，使其元素不超过给定数量
            skip(long n)                         //跳过元素，返回一个扔掉了前n个元素的流。若流中元素不足n个，则返回一个空流。与Limit(n)?互补
            map(Function f)                      //接收一个函数作为参数，该函数会被应用到每个元素上，并将其映射成一个新的元素。
            mapToDouble(ToDoubleFunction f)      //接收一个函数作为参数，该函数会被应用到每个元素上，产生一个新的DoubleStream。
            mapToInt(ToIntFunction f)            //接收一个函数作为参数，该函数会被应用到每个元素上，产生一个新的IntStream。
            mapToLong(ToLongFunction f)          //接收一个函数作为参数，该函数会被应用到每个元素上，产生一个新的LongStream。
            flatMap(Function f)                  //接收一个函数作为参数，将流中的每个值都换成另一个流，然后把所有流连接成一个流
            sorted()                                //产生一个新流，其中按自然顺序排序
            sorted(Comparator com)                 //产生一个新流，其中按比较器顺序排序
    三、终止操作
            allMatch(Predicate p)               //检查是否匹配所有元素
            anyMatch(Predicate p)               //检查是否至少匹配一个元素
            noneMatch(Predicate  p)             //检查是否没匹配所有元素
            findFirst()                         //返回第一个元素
            findAny()                           //返回当前流中的任意一个元素，如果流是一个稳定的流，那就和findFirst()一样
            count()                             //返回流中元素总数
            max(Comparator c)                   //返回流中最大值
            min(Comparator c)                   //返回流中最小值
            forEach(Consumer c)                 //内部迭代(使用 Collection 接口需要用户去做迭代，称为外部迭代。相反，Stream API 使用内部迭代——它帮你把迭代做了)
            reduce(T iden, BinaryOperator b)    //可以将流中元素反复结合起来，得到一个值。返回燭
            reduce(BinaryOperator b)              //可以将流中元素反复结合起来，得到一个值。返回燨ptional<T>
            collect(Collector c)                  //将流转换为其他形式。接收一个燙ollector接口的实现，用于给Stream中元素做汇总的方法
                方法                  返回值类型               作用描述
                toList                List<T>                把流中元素收集到List
                toSet                Set<T>                把流中元素收集到Set
                toCollection                Collection<T>                把流中元素收集到创建的集合
                counting                Long                计算流中元素的个数
                summingInt                Integer                对流中元素的整数属性求和
                averagingInt                Double                计算流中元素Integer属性的平均值
                summarizingInt                IntSummaryStatistics                收集流中Integer属性的统计值。如：平均值
                joining                String                连接流中每个字符串
                maxBy                Optional<T>                根据比较器择最大值
                minBy                Optional<T>                根据比较器择最小值
                reducing                归约产生的类型                从一个作为累加器的初始值开始，利用BinaryOperator与流中元素逐个结合，从而归约成单个值
                collectingAndThen                转换函数返回的类型                包裹另一个收集器，对其结果转换函数
                groupingBy                Map<K, List<T>>                根据某属性值对流分组，属性为K，结果为V
                partitioningBy                Map<Boolean, List<T>>                //根据true或false进行分区
    四、Optional
        * （1）empty()                            //包装空对象
        * （2）ofNullable()                       //包装可能为空的对象
        * （3）of()                               //包装非空对象，如果对象为空，报异常
        * （1）get()                              //要求只能取出非空对象，如果包装的对象是null，会抛出一个异常NoSuchElementException
        * （2）orElse（xx）                       //如果包装的对象非空，那么就返回锁包装的对象，如果包装的对象是null，那么就用xx代替
        * （3）orElseGet(Supplier)                                 //如果包装的对象非空，那么就返回锁包装的对象，如果包装的对象是null，那么就用Supplier的get方法的返回值代替
        * （4）orElseThrow(Supplier)                               //如果包装的对象非空，那么就返回锁包装的对象，如果包装的对象是null，那么就抛出Supplier的get方法所返回的异常对象
        * （2）ifPresent(Consumer)                                 //如果存在就执行Consumer的accept()的代码，如果不存在，什么也不干
        * （1）Optional<T> filter(Predicate<? super T> predicate)  //当Optional中包装的对象，符号predicate的条件判断，那么就保留，否则就不保留。
        * （1）<U> Optional<U> map(Function<? super T,? extends U> mapper)
        * （2）<U> Optional<U> flatMap(Function<? super T,Optional<U>> mapper)
 */
public class 流Stream所有方法 {

    @Test   //  一、创建Stream
    public void test1() {
        // 1、数组形式创建
        //            arrayList.stream();                   //
        //            Stream.of("hello","world","java");    //
        //            Stream.iterate()                      //创建无线流。如：Stream.iterate(1, t -> t+2);
        //            Stream.generate()                     //创建无限流。如：Stream.generate(Math::random);
        String[] arr = {"hello", "world", "java", "stream"};
        Stream<String> stream1 = Arrays.stream(arr);//这个流中的数据都是String，它的来源是arr数组
        stream1.forEach(System.out::println);
        // 2、集合形式创建
        ArrayList<String> list = new ArrayList<String>();
        list.add("hello");
        list.add("world");
        Stream<String> stream2 = list.stream();
        stream2.forEach(System.out::println);
        // 3、返回数据流(of里边值)
        Stream<String> stream3 = Stream.of("hello", "world", "java", "stream");
        stream3.forEach(System.out::println);
        // 4、创建无线流方式一Stream.iterate
        Stream<Double> stream4 = Stream.generate(Math::random);
        stream4.forEach(System.out::println);
        // 5、创建无限流。如：方式二Stream.generate
        Stream<Integer> stream = Stream.iterate(1, t -> t + 2);
        stream = stream.limit(100); // 让无限数据流有限制
        stream.forEach(System.out::println);
    }

    @Test   //  二、中间操作
    public void test2() {
        Stream<Integer> stream = Stream.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1);
        // 1、filter(Predicate p)：先筛选出偶数，再选出大于5的数据
        stream = stream.filter(num -> num % 2 == 0);//filter的形参是一个断定型函数式接口，抽象方法，boolean test(T t)
        stream = stream.filter(num -> num > 2);
        // 2、distinct()：去重处理，通过流所生成元素的HashCode()?和Equals()?去除重复元素
        stream = stream.distinct();
        // 3、limit(long maxSize)：取前4个。截断流，使其元素不超过给定数量
        stream = stream.limit(8);
        // 4、skip(long n)：跳过前五个，返回一个扔掉了前n个元素的流。若流中元素不足n个，则返回一个空流。与Limit(n)?互补
        stream = stream.skip(1);//跳过前5个
        // 5、sorted()：自然排序
//		stream = stream.sorted();
        // 6、sorted((s1, s2) -> s2.compareToIgnoreCase(s1))：定制排序（1）不区分大小写，（2）倒序
        Stream<String> streamstr = Stream.of("hello", "word", "Hello", "WORD", "java", "JAVA");
        streamstr = streamstr.sorted((s1, s2) -> s2.compareToIgnoreCase(s1));//Comparator<T>  int compare(T t1, T t2)
        // 7、map(Function f)：接收一个函数作为参数，该函数会被应用到每个元素上，并将其映射成一个新的元素。
        stream = stream.map((a) -> 2 * a);
        // 8、flatMap(Function f)                  //接收一个函数作为参数，将流中的每个值都换成另一个流，然后把所有流连接成一个流
        Stream<String> stringStream1 = streamstr.flatMap(str -> Stream.of(str.split("|")));
//        stringStream1.forEach(System.out::println);
        // 9、mapToDouble(ToDoubleFunction f)：接收一个函数作为参数(入参可以是任意数据类型结果是Double就行)，该函数会被应用到每个元素上，产生一个新的DoubleStream。
        Stream<String> stringStream = Stream.of("2", "6", "3", "1", "4");
        DoubleStream doubleStream1 = stringStream.mapToDouble(str -> Integer.parseInt(str));
        // 10、mapToInt(ToIntFunction f)            //接收一个函数作为参数，该函数会被应用到每个元素上，产生一个新的IntStream。
//        stream.forEach(System.out::println);
        IntStream intStream = stream.mapToInt(num -> (int) Math.pow(num, 2));
//        intStream.forEach(System.out::println);
        // 11、mapToLong(ToLongFunction f)          //接收一个函数作为参数，该函数会被应用到每个元素上，产生一个新的LongStream。
        LongStream longStream = intStream.mapToLong(num -> (long) Math.pow(num, 2));
        longStream.forEach(System.out::println);
    }

    @Test   //  二、终止操作 [ 流进行了终止操作后，不能再次使用 ]。终端操作会从流的流水线生成结果。其结果可以是任何 不是流的值，例如：List、Integer，甚至是 void 。
    public void test3() {
        // 1、allMatch(Predicate p)               //检查是否匹配所有元素
        Stream<Integer> stream1 = Stream.of(1, 2, 3, 4, 5);
        boolean flag = stream1.allMatch(num -> num % 2 == 0); //判断流中的数据是否都是偶数 //形参Predicate断定型函数式接口  boolean test()
        System.out.println(flag);
        // 2、anyMatch(Predicate p)               //检查是否至少匹配一个元素
        // 3、noneMatch(Predicate  p)             //检查是否没匹配所有元素
        // 4、findFirst()                         //返回第一个元素
        Stream<Integer> stream4 = Stream.of(1, 2, 3, 4, 5);
        Optional<Integer> first = stream4.findFirst();
        System.out.println(first);
        // 5、findAny()                           //返回当前流中的任意一个元素，如果流是一个稳定的流，那就和findFirst()一样
        // 6、count()                             //返回流中元素总数
        Stream<Integer> stream6 = Stream.of(1, 2, 3, 4, 5, 6);
        stream6 = stream6.filter(num -> num > 5);
        long count = stream6.count();//统计大于5的数一共有几个
        System.out.println(count);
        // 7、max(Comparator c)                   //返回流中最大值
        Stream<Integer> stream7 = Stream.of(8, 4, 9, 1, 2, 3, 4, 5, 6);
        stream7 = stream7.filter(num -> num % 2 == 0);
        Optional<Integer> opt = stream7.max((t1, t2) -> t1 - t2);//找偶数中的最大值 //Comparator<T> int compare(T t1, T t2)
        System.out.println(opt);
        // 8、min(Comparator c)                   //返回流中最小值
        // 9、forEach(Consumer c)                 //内部迭代(使用 Collection 接口需要用户去做迭代，称为外部迭代。相反，Stream API 使用内部迭代——它帮你把迭代做了)
        // 10、reduce(T iden, BinaryOperator b)    //可以将流中元素反复结合起来，得到一个值。返回燭
        // 11、reduce(BinaryOperator b)              //可以将流中元素反复结合起来，得到一个值。返回燨ptional<T>
        Stream<Integer> stream11 = Stream.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        stream11 = stream11.limit(5); //把流中的前5个数的和累加起来
        Optional<Integer> sum = stream11.reduce((t1, t2) -> t1 + t2);          //形参BinaryOperator<T>，继承了BiFunction<T,T,T>   T apply(T t1, T t2)
        //Optional<Integer> max = stream11.reduce((t1,t2) -> t1>t2?t1:t2);    //找最大值
        System.out.println(sum);
        // 12、collect(Collector c)                  //将流转换为其他形式。接收一个collector接口的实现，用于给Stream中元素做汇总的方法
        Stream<Integer> stream12 = Stream.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        stream12 = stream12.filter(num -> num % 2 == 0);
        List<Integer> list = stream12.collect(Collectors.toList());
        list.forEach(System.out::print);
        // 13、方法                  返回值类型               作用描述
        // 14、toList                List<T>                把流中元素收集到List
        // 15、toSet                Set<T>                把流中元素收集到Set
        // 16、toCollection                Collection<T>                把流中元素收集到创建的集合
        // 17、counting                Long                计算流中元素的个数
        // 18、summingInt                Integer                对流中元素的整数属性求和
        // 19、averagingInt                Double                计算流中元素Integer属性的平均值
        // 20、summarizingInt                IntSummaryStatistics                收集流中Integer属性的统计值。如：平均值
        // 21、joining                String                连接流中每个字符串
        // 22、maxBy                Optional<T>                根据比较器择最大值
        // 23、minBy                Optional<T>                根据比较器择最小值
        // 24、reducing                归约产生的类型                从一个作为累加器的初始值开始，利用BinaryOperator与流中元素逐个结合，从而归约成单个值
        // 25、collectingAndThen                转换函数返回的类型                包裹另一个收集器，对其结果转换函数
        // 26、groupingBy                Map<K, List<T>>                根据某属性值对流分组，属性为K，结果为V
        // 27、partitioningBy                Map<Boolean, java.util.List<T>>                //根据true或false进行分区
    }

    @Test   //  二、中间操作 [ 流进行了终止操作后，不能再次使用 ]。终端操作会从流的流水线生成结果。其结果可以是任何 不是流的值，例如：List、Integer，甚至是 void 。
    public void test4() throws Exception {
        //(1) empty()：包装一个空对象
        Optional<Object> opt1 = Optional.empty();
        //(2) ofNullable(String)：包装一个可能为空的对象，可以为null
        Optional<String> opt2 = Optional.ofNullable("sht");
        //(3) Optional.of(String)：包装一个必须是非空的对象，null的话会报错
        Optional<String> opt3 = Optional.of("sht");
        //(4) get()：只能用于取出的非空对象
        String string = opt2.get(); //如果里面的对象是null，那么报java.util.NoSuchElementException: No value present
        //(5) orElse("shanghai")：用于取出所包装的对象，该对象可能为空 [对象非空，取对象，对象是null，则用orElse(xx)中的xx来代替返回]
        String address = opt2.orElse("shanghai");
        //(6) orElseGet(? extends String)：用于取出所包装的对象，该对象可能为空。如果里面的对象是null，那么用Supplier的供给型函数式接口的get()方法返回的结果代替
        String address2 = opt2.orElseGet(() -> new Scanner(System.in).nextLine());
        //(7) orElseThrow(? extends Throwavse)：用于取出所包装的对象，该对象可能为空。如果里面的对象是null，那么就抛出Supplier的供给型函数式接口的get()方法提供的异常对象代替
        String address3 = opt2.orElseThrow(() -> new Exception("地址没填写"));
        //(8) isPresent()：判断是否存在
        boolean present = opt2.isPresent();
        //(9) ifPresent()：判断是否存在，并且如果存在，就执行xx操作，如果不存在什么也不干
        opt2.ifPresent(t -> System.out.println(t));
        //(10) filter()：判断它是否以"sh"开头，如果是，就保留，如果不是就把所包装的对象清空了
        Optional<String> name = opt2.filter(t -> t.startsWith("sh"));
        //(11) map(R apply(T t))：//对所包装的姓名进行字符串截取操作，取出姓，并转大写
        Optional<String> tou = opt2.map(t -> t.substring(0, 1).toUpperCase());
        //(12) flatMap(R apply(T t))：//对所包装的姓名转成大写
        Optional<String> daxie = opt2.flatMap(t -> Optional.of(t.toUpperCase()));
    }
}
