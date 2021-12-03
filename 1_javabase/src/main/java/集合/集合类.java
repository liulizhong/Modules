package 集合;

import java.io.IOException;
import java.util.*;

/*
    一、Collection：根接口方法
        （1）add(obj)：添加一个元素到当前集合中（若添加一个集合也算是一个元素，总元素个数只加1）
        （2）addAll(Collection c)：添加多个元素，把集合c中的每个元素都添加到当前集合中
        （3）remove(obj)：从当前集合中删除一个元素，若有想要删除的重复元素，只删除第一个
        （4）removeAll(Collection c)：从当前集合中把集合c中存在的元素都删除，即删除它俩的交集
        （5）contains(obj)：是否包含obj元素
        （6）containsAll(Collection c)：判断c是否是当前集合的子集，就是当前集合是否全部包含c集合的所有元素
        （7）retainAll(Collection c)：只保留交集部分
        （8）size()：获取当前集合的元素个数
        （9）isEmpty()：判断当前集合是否为空集合
        （10）clear()：情况当前集合的所有元素
        （11）iterator()：获取当前集合的一个迭代器对象，用于遍历当前集合用
        （12）toArray()：把当前集合中的元素放到一个数组中，并返回
        （一）遍历1：  for(Object object : jihe) {System.out.println(object);}
        （二）遍历2：  jihe.toArray().foreach.sout;
        （三）遍历3：  Iterator b = jihe.iterator();  while(b.hasNext()) {System.out.println(b.next());}
    二、List：有序的，添加顺序的，可重复，在Collection增加了许多和索引增加的方法，遍历的方法，可以通过下标获取了。
        （1）Vector：动态数组
                相对ArrayList来说比较旧一点，线程安全的
                初始化时数组的长度为10，不够了可以扩容，要么照你指定的capacityIncrement进行扩容，默认是照2倍扩容(10-20-40-80)
                Vector遍历除了支持新的foreach和Iterator迭代方式外，还支持旧版的迭代器Enumration
        （2)ArrayList：动态数组（没有元素进出的顺序需求或者需要[index]一般都选ArrayList）
                相对Vector来说比较新一点，线程不安全的
                空参构造创建ArrayList对象时，JDK1.6默认长度为10。1，7以后数组初始化为一个空数组常量，第一次添加元素时，初始化为10，后期默认都照1.5倍扩容(10-15-22-33-49)
                ArrayList只支持foreach和Iterator迭代
        （3）LinkedList：双向链表（要是有元素进出需求一般选择LinkedList，其他的类的方法他都有）
                JDK1.6之后同时也是双端队列支持栈、对列、双端队列。队列（先进先出，offer，poll）、（既可以从头移除元素，也可以从队尾移除元素）
                底层的物理结构：链表，不建议调用和（index）相关的方法。但是在删除、插入时效率高，省空间
        （4）Stack：是Vector的子类，特征：后进先出（LIFO），或者说先进后出。		//代表方法：对象.pop()（把对象弹出）	对象.push压入	（和add效果一样）
        （5）新增方法：
                1）add(index,obj)：指定在index位置，添加一个元素到当前集合中
                2）addAll(index,Collection c)：指定在index位置，添加多个元素，把集合c中的所有元素都添加到当前集合中
                3）remove(index)：指定删除index位置的元素
                4）get(int index)：获取index位置的元素
                6）indexOf(Object o)：获取o元素在当前集合中的第一次出现的索引，若没有返回-1
                7）lastIndexOf(Object o)：获取o元素在当前集合中的最后一次出现的索引，若没有返回-1
                8）listIterator()：获取当前集合的一个迭代器，这个迭代器支持从前往后，也支持从后往前迭代，并且支持在迭代的同时添加，替换元素等。
                9）set(index,obj)：替换当前集合index位置的元素
                10）subList(int fromIndex, int toIndex) ：截取当前集合[fromIndex,toIndex)部分的元素
                11）retainAll(Collection c)：只保留交集部分
                12）contains、remove、retainAll是调用了对象的equals方法，若返回值相同便认为是同一对象(重写对象的hashcode和equals方法)
    三、set：无序的，不保证添加的顺序，不可重复（Set里的方法都是Collection里的方法，没有另外添加的）
        （1）HashSet：
                1、通过元素的hashCode()和equals()方法，保证无序，不可重复，hashCode值决定它散列存储，equals最终确定是否重复，可以通过重写hashCode()和equals()方法来选择对象的价格不可重复
                2、String重写了equals方法，一样的String内容就被认为是一样的添加不了重复的，但是new Student没有重写equals方法，那么就认为不同，可以添加new的内容一致的元素，重写后可以实现“不重复”
        （2）TreeSet（构造器用得着的有两种，一是无参。二是参数为比较器）
                大小排序规则：1、要么添加的元素类实现了java.lang.Comparable接口（自然排序规则，默认排序规则，重写int compareTo(T t)），要是返回值相等那就认为是同一个对象，那么就不能重复添加，
                              2、要么为TreeSet指定一个定制比较器对象（new TreeSet（new 比较器名称）），对象是实现java.util.Comparator接口（定制比较规则，重写int compare(T t1, T t2)方法）
                一般重写了compareto方法了都重写equals方法。因为compareto方法认为相同的话equals比较像等那就比较怪，所以一般都一起重写
        （3）LinkedHashSet：
                它是HashSet的子类，比HashSet多维护了元素之间添加的前后顺序。添加和删除效率比较低，因为要维护元素的顺序关系，凡是你想要一个集合，既想不可重复又想维护添加顺序可以选它
        （4）Set的内部实现都是Map
                HashSet &#45;&#45;》 HashMap  &#45;&#45;》 key：存储到Set中的元素，value：Object类型的常量对象PRESENT
                TreeSet &ndash;&gt; TreeMap   &#45;&#45;》 key：存储到Set中的元素，value：Object类型的常量对象PRESENT
                LinkedHashSet &ndash;&gt;  LinkedHashMap  &#45;&#45;》 key：存储到Set中的元素，value：Object类型的常量对象PRESENT
    四、Map常用方法：kay是不可重复的，value可以重复。key和value可以是任意数据类型。Map数组初始容量16，是2倍扩容，底层元素是Entry类型。
        （1）put(key,value)：添加一对映射关系到当前map集合中
        （2）putAll(Map m)：把m集合的所有映射关系添加到当前map中
        （3）remove(key)：根据key移除一对映射关系，并返回刚删除的value值，可接收可不接收
        （4）clear()：清空所有
        （5）containsKey(key)：是否包含某个key
        （6）containsValue(value)：是否包含某个value
        （7）V get(key)：根据key获取value
        （8）size()：获取效元素个数
        （9）isEmpty()：是否是空集合
    五、Map的实现类
        （1）Hashtable：基于哈希表（散列表），是旧版的。线程安全的。key,value都不允许为null
        （2）HashMap：基于哈希表（散列表），相对Hashtable新一点。线程不安全的。key,value允许为null。HashMap底层的链表是单向的。
        （3）LinkedHashMap：是HashMap的子类，维护了所有的映射关系的添加的顺序，也就是说添加和删除时比HashMap多维护了元素关系，效率就更低了。LinkedHashMap底层的链表是双向的。
        （4）TreeMap：所的映射关系是照key的大小顺序排列（其他四个Map类的kay不能重复是因为has值和equals，这个是因为compareto和compare）。
                      存储到TreeMap中的key要么实现java.lang.Comparable接口，要么为TreeMap指定为key设计的定制比较器对象java.util.Comparator对象
        （5）Properties：是Hashtable的子类，它的key和value的类型都是String类型，一般用来存储属性配置信息
                         对象.setProperty添加数据，对象.getProperty得到数据，是自己的方法
    六、Collections 集合工具类
        （1）public static <T> boolean addAll(Collection<? super T> c,T... elements)
        （2）public static <T> int binarySearch(List<? extends Comparable<? super T>> list,T key)
        （3）public static <T> void copy(List<? super T> dest, List<? extends T> src)
        （4）public static <T extends Object & Comparable<? super T>> T min(Collection<? extends T> coll)
                        泛型可能被擦除，T都是照上限处理的，没没指定上限，那么默认是Object，如果指定了上限，那么照第一个上限处理；
                        因为这里返回值，T，如果擦除后，这里要求必须是Object处理，因为版本的问题，之前的版本（没泛型的版本这里是Object处理。
        （5）public static <T> void sort(List<T> list, Comparator<? super T> c)
             public static <T extends Comparable<? super T>> void sort(List<T> list)
        （6）synchronized开头的方法：表示可以把原来线程不安全的集合转化为线程安全的集合，例如：ArrayList，HashMap等
        （7）unmodifiable开头的方法：表示把一个可变的集合变成一个只读的集合
 */
public class 集合类 {
    public static void main(String[] args) throws IOException {

        //// 【1】、List分类[Collection]
        Vector vectors = new Vector<String>();                        // 动态数组。相对 ArrayList 线程安全。默认是照 2 倍扩容(10-20-40-80)。多线程共享变量情况建议用。
        ArrayList arrayLists = new ArrayList<String>();               // 动态数组。相对  Vector 线程不安全。默认是照1.5倍扩容(10-15-22-33)。无元素进出顺序或需要index建议用。
        LinkedList linkedLists = new LinkedList();                    // 双向链表。队列(先进先出offer/poll)，底层链表结构，删除插入效率高。无index，有元素进出顺序需要建议用。
        Stack stacks = new Stack();                                   // Vector的子类。增加了添加顺序，队列（后进先出pop/push）。需要添加顺序倒序情况建议用。

        //// 【2】、Set分类[Collection]
        HashSet hashSet = new HashSet();                        // 通过元素的hashCode()和equals()方法，保证无序，不可重复。可重写equals方法实现“自定义规则不重复”
        TreeSet treeSet = new TreeSet();                        // 不重复且有序。要求元素要么实现Comparable接口，要么传入Comparator比较器规则
        LinkedHashSet linkedHashSet = new LinkedHashSet();      // HashSet的子类，多维护了元素之间添加的前后顺序。相对添加和删除效率也会较低

        //// 【3】、Collection常用方法
        arrayLists.add("a");                       // Collection     // 添加一个元素
        arrayLists.addAll(vectors);                // Collection     // 添加一个集合里的所有元素
        arrayLists.remove("b");                // Collection     // 删除一个元素
        arrayLists.removeAll(linkedLists);         // Collection     // 删除一个集合里存在的所有元素
        arrayLists.contains(3);                    // Collection     // 是否包含元素 数字3
        arrayLists.containsAll(stacks);            // Collection     // 断c是否是当前集合的子集，就是当前集合是否全部包含c集合的所有元素
        arrayLists.retainAll(linkedLists);         // Collection     // arrayLists只保留两个集合的交集部分
        arrayLists.size();                         // Collection     // 返回集合的元素个数
        arrayLists.isEmpty();                      // Collection     // 判断集合是不是空集合
        arrayLists.clear();                        // Collection     // 清空当前集合的所有元素
        arrayLists.iterator();                     // Collection     // 获取当前集合的一个迭代器对象，用于遍历当前集合用
        arrayLists.toArray();                      // Collection     // 把当前集合中的元素放到一个数组中，并返回
//        arrayLists.set(0,"c");                     // List特有      // 替换当前集合index位置的元素
//        arrayLists.get(1);                         // List特有       // 获取index位置的元素
//        arrayLists.indexOf("a");                   // List特有      // 获取“a”元素在当前集合中的第一次出现的索引，若没有返回-1
//        arrayLists.lastIndexOf("a");               // List特有      // 获取“a”元素在当前集合中的最后次出现的索引，若没有返回-1
//        arrayLists.subList(2,4);                   // List特有      // ：截取当前集合[fromIndex,toIndex)部分的元素。返回一个List

        //// 【4】、Collection集合的三种遍历
        for (Object arrayList : arrayLists) {                           // 遍历一：foreach遍历
            System.out.println("foreach：" + arrayList);
        }
        Iterator iterator = arrayLists.iterator();                      // 遍历二：迭代器：Iterator
        while (iterator.hasNext()) {
            System.out.println("迭代器：" + iterator.next());
        }
        Object[] array = arrayLists.toArray();                          //遍历三：toArray()
        for (int i = 0; i < array.length; i++) {
            System.out.println("toArray：" + array[i]);
        }

        //// 【5】、Map分类：kay是不可重复
        Hashtable hashtable = new Hashtable();                      // 基于哈希表（散列表），是旧版的。线程安全的。key,value都不允许为null。
        HashMap hashMap = new HashMap();                            // 基于哈希表（散列表），相新版的。线程不安全。key,value允许为null。底层的链表是单向的。
        LinkedHashMap linkedHashMap = new LinkedHashMap();          // HashMap的子类，维护了添加的顺序，添加和删除时比效率就更低了。LinkedHashMap底层的链表是双向的。
        TreeMap treeMap = new TreeMap();                            // 按照key的大小顺序排列，要求元素要么实现Comparable接口，要么传入Comparator比较器规则（其他四个Map类kay不能重复是因为has值和equals，这个是因为compareto和compare）。
        Properties properties = new Properties();                   // Hashtable的子类，它的key和value的类型都是String类型，一般用来存储属性配置信息。对象.setProperty添加数据，对象.getProperty得到数据，是自己的方法

        //// 【6】、Map常用方法
        hashMap.put(1, "a");                                         // 添加一对映射关系到当前map集合中
        hashMap.put(2, "b");                                         // 添加一对映射关系到当前map集合中
        hashMap.putAll(hashtable);                                  // 把集合的所有映射关系添加到当前map中
        hashMap.remove(1);                                     // 根据key移除一对映射关系，并返回刚删除的value值，可接收可不接收
        hashMap.clear();                                             // 清空所有
        hashMap.containsKey(2);                                      // 是否包含某个key
        hashMap.containsValue("b");                                 // 是否包含某个value
        hashMap.get(1);                                              // 根据key获取value
        hashMap.size();                                              // 获取有效元素个数
        hashMap.isEmpty();                                           // 是否是空集合
        hashMap.keySet();                                            // 获取所有key返回到Set集合中
        hashMap.values();                                            // 获取所有value返回Collection集合
        hashMap.entrySet();                                          // 获取所有键值对返回到set集合
        properties.load(ClassLoader.getSystemResourceAsStream("condition.properties"));     // Properties独有  // 加载配置文件
        properties.setProperty("ip", "192.168.1.101");                                              // Properties独有  // 配置文件增加一个键值对
        properties.getProperty("ip", "无");                                       // Properties独有  // 获取key的value值，若没有给默认值“无”

        //// 【7】、Map集合的遍历
        Set<Map.Entry<Integer, String>> entries = hashMap.entrySet();
        for (Map.Entry<Integer, String> entry : entries) {
            System.out.println("Key:" + entry.getKey() + " Value:" + entry.getValue());
        }
//        entries.forEach(entrie -> System.out.println(entrie));    // Lambda表达式的遍历
    }
}
