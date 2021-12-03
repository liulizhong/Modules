package Java常用API;

import java.io.UnsupportedEncodingException;

/*
java.lang.String：字符串
    （1）String 类代表字符串。Java 程序中的所字符串字面值（如 "abc" 都作为此类的实例实现。
    （2）字符串是常量；它们的值在创建之后不能更改。   换句话说，一旦修改就是新对象
        因为 String 对象是不可变的，所以可以共享。
         字符串常量是存储在字符串常量池中。凡是new出来在堆中。
         常量池在哪里？
             JDK1.6：方法区
             JDK1.7：字符串常量池从方法区挪到了堆中的特殊位置。
             JDK1.8：字符串常量池又从堆中挪走，挪到“元空间”
    （3）字符串底层是用char[]数组存储字符内容的
            但是JDK1.9用byte[]
    （4）String类型和Integer类型一样都是final，表示不能被继承。
字符串常用方法：
    一、字符串操作
        (1) length()：求字符串的长度			//String s = null； s.length();//空指针异常
        (2) trim()：去掉前后的所有空格（包括Tab键空白符），中间的不会去掉，后期常用
        (3) equals：比较字符串的字符内容，严格区分大小写，==是比较对象地址
        (4) equalsIgnoreCase：比较字符串的字符内容，忽略大小写
        (5) isEmpty()：是否是空字符串
                 str == null  或  "".equals(str)  或  str.isEmpty()	//红色的方法最安全，因为不会报空指针异常，要是能确认非空就都可以用
        (6) toUpperCase()：转大写	//字符串一旦改变就换了对象了，应该拿一个变量接收一下，或者赋给自己，那他就变成大写了
        (7) toLowerCase()；转小写
        (8) s1.concat(s2)：和 s1+s2 相同都是拼接字符串，还涉及空指针问题，其他都一样
    二、字符串与字符字节相关
        (1) new String(char[] arr , 【int offset】, 【int count】)		//用arr的字符数组构建字符串，从下表offset开始一共count个,可不加后俩参数
        (2) char[] toCharArray()：   //把字符串转成字符数组		例 char[] arr = str.toCharArray()；
        (3) char charAt(index)： //获取字符串index位置的字符	例 char = input.next().chatAt(0);
        (4) byte[] getBytes(【Charset charset】或【String charsetName】)：  //byte[] bytes = str.getBytes("UTF-8");
        (5) String(byte[] bytes, 【int offset】, 【int length】, 【String charsetName】)：//指定的字符集解码的byte构造一新String 	//String string = new String(arr,0,4,"UTF-8");
    三、字符串操作
        (1) boolean endsWith(String suffix)： //测试此字符串是否以指定的后缀结束。 变量.endsWith("要查找的字符")
            boolean startsWith(String prefix, 【int toffset】)： //测试此字符串从[指定索引]开始的子字符串是否以指定前缀开始。
        (2) boolean contains(CharSequence s)： //当且仅当此字符串包含指定的 char 值序列时，返回 true。
        (3) int indexOf(int ch, 【int fromIndex】)： //返回在此字符串中第一次出现指定字符处的索引，[从指定的索引开始搜索]。无则但会‘-1’
            int indexOf(String str, 【int fromIndex】)： //返回指定子字符串在此字符串中第一次出现处的索引，[从指定的索引开始]。
            int lastIndexOf(int ch, 【int fromIndex】)： //返回指定字符在此字符串中最后一次出现处的索引，[从指定的索引处开始进行反向搜索]。
            int lastIndexOf(String str, 【int fromIndex】)： //返回指定子字符串在此字符串中最后一次出现处的索引，[从指定的索引开始反向搜索]。
        (4) String substring(int beginIndex, 【int endIndex】)： //返回一个新字符串，它是此字符串从beginIndex开始截取到【endIndex/最后】(不包含)的一个子字符串。
        (5) boolean matches(String regex)： //告知此字符串是否匹配给定的正则表达式。
        (6) String replace(char oldChar, char newChar)：  //（不支持正则）返回一个新的字符串，它是通过用 newChar 替换此字符串中出现的所有 oldChar 。
            String replaceAll(String regex, String replacement)：  //（支持正则）使用给定的 replacement 替换此字符串所匹配给定的正则表达式的子字符串。
            String replaceFirst(String regex, String replacement)：  //（支持正则）使用给定的 replacement 替换此字符串匹配给定的正则表达式的第一个子字符串。
        (7)String[] split(String regex, 【int limit】)：  //根据匹配给定的正则表达式来拆分此字符串，【最多不超过limit个，如果超过了，剩下的全部都放到最后一个元素中】。
    四、可变字符串
        (1) StringBuffer(【int size】/【String str】)：   //构造指定【容量/内容】的字符串缓冲区
        (2) StringBuffer append(xx)： //提供了很多的append()方法，用于进行字符串拼接，“对象。append（“扩展内容”）”自动把原对象字符串串加一起，不用在接收了，要是想赋值给其他变量可以接受
        (3) StringBuffer delete(int start,int end)： //删除指定位置的内容
        (4) StringBuffer deleteCharAt(int index) ： //删除指定位置的字符
        (5) StringBuffer insert(int offset, xx)： //在指定位置插入xx
        (6) StringBuffer replace(int start, int end, String str)： //把[start,end)位置替换为str
        (7) StringBuffer reverse() ： //把当前字符序列逆转
        (8) void setCharAt(int index, char ch) ： //替换index位置的字符，该方法不支持方法链，因为方法的返回值类型是void
 */
public class 字符串 {
    public static void main(String[] args) throws UnsupportedEncodingException {
        //// 【1】、创建和比较
        String str1 = "1";   // 字符串常量是存储在字符串常量池中。凡是new出来在堆中。
        String str2 = "1";
        String str3 = new String("1");
        String str4 = new String("1");
        String str5 = "11";
        String str6 = "1" + "1";
        String str7 = str1 + str2;
        String str8 = str1 + "1";
        final String str9 = "1";
        final String str10 = "1";
        String str11 = str9 + str10;
        String str12 = new String("1") + new String("1");//指向堆中的常量
        String str13 = (new String("1") + new String("1")).intern();
        System.out.println(str1 == str2);                //true		一个对象 [在常量池]
        System.out.println(str1 == str3);                // false     str指向堆中对象两个对象
        System.out.println(str3 == str4);                // false
        System.out.println(str5 == str6);                // true    常量 + 常量=》常量池
        System.out.println(str5 == str7);                // false   变量 + 变量=》堆中
        System.out.println(str5 == str8);                // false   常量 + 变量=》堆中
        System.out.println(str5 == str11);                // true   常量 + 变量=》堆中  // 加final都是在常量池中，只是指向常量池的常量和常量结果相同（str1、str2、"1"都是常量）
        System.out.println(str5 == str12);                // false   指向堆中的常量和字符串常量池不同
        System.out.println(str5 == str13);                // true   String方法(s1 + s2).intern();	是将结果储存到字符串常量池

        //// 【2】、String的常用方法
        "abcd".length();                                         // 求字符串的长度  //String s = null; s.length();//空指针异常
        " ab cd ".trim();                                        // 去掉前后的所有空格（包括Tab键空白符），中间的不会去掉，后期常用
        "abcd".equals(" abcd");                                 // 比较字符串的字符内容，严格区分大小写，==是比较对象地址
        "abcd".equalsIgnoreCase("AbcD");         //比较字符串的字符内容，忽大小写
        "".equals("abcd");
        "abcd".isEmpty();                                       // 是否是空字符串 同"abcd" == null;   第一个方法最安全，因为不会报空指针异常，要是能确认非空就都可以用
        "abcd".toUpperCase();                                    // 转大写，拿一个变量接收一下
        "abcd".toLowerCase();                                    //转小写

        //// 【3】、[字节、字符、字符串]  的转化
        "ab".concat("cd");                                      // 同"ab"+"cd"; 拼接字符串，小心空指针异常
        char[] arr = "abcd".toCharArray();                      // 把字符串转成字符数组
        char c = "abcd".charAt(2);                              // 获取字符串指定下标 的‘字符’
        String s = new String(arr, 1, 2);        // 将字符数组arr中索引2~3的元素生成新字符串
        byte[] utf8s = "abcd".getBytes("utf8");  // 字符串转化为字节数组
        String str = new String(utf8s);                           // 字节数组转化成字符串

        //// 【4】、字符穿的开头、结尾、查找、截取、正则、替换等
        "abcd".endsWith("d");                                   // 字符串是否以指定的后缀结束。
        "abcd".startsWith("b", 1);              // 字符串从指定索引开始的子字符串是否以指定前缀开始。
        "abcd".contains("bc");                                  // 字符串包含指定的 char 值序列时，返回 true。
        int cha1 = "abcd".indexOf('b', 3);     // 返回在此字符串中第一次出现指定字符处的索引，从指定的索引开始搜索。未找到返回-1
        int cha2 = "abcd".lastIndexOf('b', 3); // 返回指定子字符串在此字符串中最后一次出现处的索引，从指定的索引开始反向搜索。未找到返回-1
        String substring = "abcdef".substring(2, 4);             // 返回一个新字符串，它是此字符串从beginIndex开始截取到endIndex(不包含)的一个子字符串。
        boolean matches = "abcd".matches("\\d+");       // 告知此字符串是否匹配给定的正则表达式。
        "abcd".replaceAll("ab", "b");      // 返回替换后的子串，全部替换，支持正则
        "abcd".replaceFirst("ab", "b");    // 返回替换后的子串，首个替换，支持正则
        "a:b:cd:ef".split(":", 3);                 // 根据匹配给定的正则表达式来拆分此字符串，最多不超过limit个，如果超过了，剩下的全部都放到最后一个元素中。
        // 正则表达式详见

        //// 【5】、可变字符串
        StringBuffer sf = new StringBuffer();                              // 线程安全的，效率较低（但远比string拼接效率高得多）
        StringBuilder sb = new StringBuilder("hello world java");       // 线程不安全，效率较高（但远比string拼接效率高得多）对于单线程来说，建议使用它
        sf.append("hello").append("world").append("java");
        sb.append("hello world java!").delete(2, 4).deleteCharAt(2)     // 删除下标[2, 4)的元素，再删除下标是2的元素，此时 sb="he world java!"
                .insert(2, "llo")                             // 下标2的元素位置插入 "llo" ，此时还原了
                .replace(7, 10, "orl").reverse()         // 下标[7, 10)的元素替换为"llo"，并反转
                .setCharAt(8, '？');                          // 下标[2, 4)的字符替换为‘？’
        String result = new String(sb);                                    // 转化为字符串
        String s1 = sb.toString();                                         // 转化为字符串
    }
}
