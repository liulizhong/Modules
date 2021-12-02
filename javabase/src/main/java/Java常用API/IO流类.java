package Java常用API;

import java.io.*;

/**
 1、 常见的IO流：
     一、字节输入流：InputStream
         （1）int read()：一次读一个字节，返回的读取的字节的值，如果到达数据末尾没有数据了就返回-1
         （2）int read(byte[] data)：一次读取多个字节，读取的数据存到data字节数组中，从[0]下标开始存储，返回实际读取的字节的个数，最多读data.length个，如果到达数据末尾没有数据了就返回-1
         （3）int read(byte[] data, int offset, int count)：：一次读取多个字节，读取的数据存到data字节数组中，从[offset]下标开始存储，返回实际读取的字节的个数，最多读取count个，没数据返回-1
         （4）void close():关闭IO流
     二、字节输出流：OutputStream
         （1）void write(int)：一次写一个字节
         （2）void write(byte[] data)：一次写整个字节数组
         （3）void write(byte[] data, int offset, int count)：一次写多个字节，从data[offset]开始，count个字节
         （4）void close():关闭IO流
         （5）void flush（）：刷新-从缓冲区立即写出
     三、字符输入流：Reader
         （1）int read()：一次读一个字符，返回的读取的字符的Unicode编码值，这个方法，如果流中没数据了，返回-1
         （2）int read(char[] data)：一次读取多个字符，读取的数据存到data字符数组中，从[0]下标开始存储，返回实际读取的字符的个数，最多读data.length个，这个方法，如果流中没数据了，返回-1
         （3）int read(char[] data, int offset, int count)：：一次读取多个字符，读取的数据存到data字符数组中，从[offset]下标开始存储，返回实际读取的字符的个数，最多读取count个，没数据返回-1
         （4）void close():关闭IO流
     四、字符输出流：Writer
         （1）void write(int)：一次写一个字符
         （2）void write(char[] data)：一次写整个字符数组
         （3）void write(char[] data, int offset, int count)：一次写多个字符，从data[offset]开始，count个字符
         （4）void write(String str)：字符串写出去
         （5）void write(String str, int offset, int count)：从offset写count这么长
         （6）void close():关闭IO流
         （7）void flush（）：刷新-从缓冲区立即写出
     五、常见IO
         （1）文件IO流
             * FileInputStream：以字节的方式读取文件内容
             * FileOutputStream：以字节的方式输出，保存文件内容
             * FileReader：以字符的方式读取纯文本文件内容
             * FileWriter：以字符的方式输出纯文本数据到纯文本文件中。
         （2）字节数组IO流
             * ByteArrayInputStream：从字节数组中读取数据
             * ByteArrayOutputStream：把数据保存到字节数组中
         （3）字符数组IO流
             * CharArrayReader：从字符数组中读取数据
             * CharArrayWriter：把数据保存到字符数组中
             * StringReader：从字符串中读取数据
             * StringWriter：把数据保存到字符串中
         （4）解码和编码IO流
             * OutputStreamWrite：//数据从程序 &ndash;&gt; osw（字符流) &ndash;&gt;按照编码&ndash;&gt;fos（字节流)&ndash;&gt;文件
             * InputStreamReader：//数据流向： "utf-8.txt" &ndash;&gt; fis（字节流) &ndash;&gt; isr（字符流))&ndash;&gt;输出
         （5）缓冲IO流：
             * BufferedInputStream：给InputStream系列的IO流增加“缓冲”功能
             * BufferedOutputStream：给OutputStream系列的IO流增加“缓冲”功能
             * BufferedReader：给Reader系列的IO流增加“缓冲”功能
             * BufferedWriter：给Writer系列的IO流增加“缓冲”功能
         （6）对象IO流
             * ObjectInputStream：一次输入一个对象
             * ObjectOutputStream：一次输出一个对象
         （7）基本数据IO流
             * DataInputStream：一次输入一个Java的基本数据类型的值
             * DataOutputStream：一次输出一个Java的基本数据类型的值
         （8）打印流
             * PrintStream：例如：System.out
             * PrintWriter：例如：web中,response.getWriter() 给客户端输出数据用的
     六、DataInputStream / DataOutpuStream
          writeUTF(xx)	//String类型
          writeInt(xx)
          writeDouble(xx)
          writeChar(xx)
          writeBoolean(xx)

 2、如何实现对象的序列化？（问对象序列化的过程？）
     1、首先这个对象的类型应该实现java.io.Serializable和java.io.Externalizable接口
     2、如果这个类型中引用数据类型的属性参与序列化，那么这个类也必须实现java.io.Serializable和java.io.Externalizable接口
     3、应该给他加一个序列化版本ID：serialVersionUID
     4、哪些属性不参与序列化
    如果实现java.io.Serializable接口：static和transient修饰和父类未序列化的属性
    如果实现java.io.Externalizable接口：只要在writeExternal和readExternal方法中不处理该属性即可
 */
public class IO流类 {
    public static void main(String[] args) throws IOException, ClassNotFoundException {

        //// 【1】、File类
        File file = new File("../../tmp\\1.json");      // 创建File类对象
        file.getName();                                                // 1.json              //传的路径名字拿走就留下文件名 “1.jpg”
        file.getPath();                                                // ..\..\tmp\1.json    // File中的参数全部。不会把相对路径转化为绝对路径，即参数原样，../会保留
        file.getCanonicalPath();                                       // C:\tmp\1.json       // 绝对路径。会解析..等表示方式（哪个盘下的全路径加文件名）
        file.getParent();                                              //  ..\..\tmp          // 参数减去文件名。不会把相对路径转化为绝对路径，即参数原样减去文件名，../会保留
        file.length();                                                 // 95                  // 单位字节。不能直接获取目录的大小，如果要获取，可以用递归
        file.lastModified();                                           // 1615364017349       // 获取文件最后修改时间。单位毫秒
        file.isFile();                                                 // 是否是文件
        file.isDirectory();                                            // 是否是目录
        file.isHidden();                                               // 是否是隐藏的
        file.exists();                                                 // 是否存在
        file.canRead();                                                // 是否有读权限
        file.canWrite();                                               // 是否有写权限
        file.list();                                                   // 获取一个目录的下一级目录。返回String[]。file是文件的话会报错
        file.listFiles();                                              // 获取目录的下一级File[]。
        file.createNewFile();                               //文件操作 // 创建文件，若父目录不存在则没反应
        file.delete();                                      //文件操作 // 删除
        file.renameTo(new File("2.json"));    //文件操作 // 重命名。可移动
        file.mkdir();                                       //目录操作 // 创建。失败不报异常，如果父目录不存在，什么也不做
        file.mkdirs();                                      //目录操作 // 创建。如果父目录不存在，一并创建
        file.delete();                                      //目录操作 // 删除。删除的是最目录的最底层（而且只能删除空目录)，如果删除非空目录可以用递归
        file.renameTo(new File("../"));        //目录操作 // 重命名。可以更换位置，而且里边文件跟着移动，不能创建目录

        //// 【2】、文件IO流。【超级父类=> 节输入流：InputStream  字节输出流：OutputStream   字符输入流：Reader   字符输出流：Writer】
        FileInputStream fis = new FileInputStream("C:\\tmp\\1.txt");     //文件字节输入流
        FileOutputStream fos = new FileOutputStream("C:\\tmp\\2.txt");   //文件字节输出流
        FileReader fr = new FileReader("C:\\tmp\\1.txt");             //文件字符输入流
        FileWriter fw = new FileWriter("C:\\tmp\\1.txt");             //文件字符输出流
        fis.read();                                          // 一次读一个字节，返回的读取的字节的int值，如果到达数据末尾没有数据了就返回-1
//        fis.read(new byte[1024], 99, 1024);      // 一次读取多个字节数据存到字节数组中，从[99]下标开始存储，返回实际读取的字节的个数，最多读1024个，默认data.length个，如果到达数据末尾没有数据了就返回-1
        fis.close();                                         // 关闭IO流
        fos.write(75);                                   // 一次写一个字节
//        fos.write(new byte[1024], 99, 1024);     // 一次写整个字节数组。：一次写多个字节，从data[99]开始，1024个字节
        fos.flush();                                         // 刷新-从缓冲区立即写出
//        fw.write("写数据", 9, 111);         // 字符串写数据。从offset=9 写count这么长

        //// 【3】、缓冲IO流。
        BufferedInputStream bfis = new BufferedInputStream(fis);                // 字节输入缓冲流
        BufferedOutputStream bfos = new BufferedOutputStream(fos);              // 字节输出缓冲流
        BufferedReader bfr = new BufferedReader(fr);                            // 字符输入缓冲流
        BufferedWriter bfw = new BufferedWriter(fw);                            // 字符输出缓冲流
        String lineStr = bfr.readLine();                                        // 新增方法每次读取一行数据【if(line == null){break; }】
        bfw.newLine();                                                          // 新增方法每次写一行数据 // 写入换行符

        //// 【4】、编码解吗IO流。【一般用于网络传输】
        InputStreamReader isr = new InputStreamReader(fis, "GBK");        // isr是字符流。  //数据流向： "utf-8.txt" --> fis（字节流) --> isr（字符流))-->输出
        OutputStreamWriter osw = new OutputStreamWriter(fos, "utf-8");       // osw也是字符流。//数据从程序 --> osw（字符流) -->按照编码-->fos（字节流)-->文件
        BufferedReader bfri = new BufferedReader(isr);                                      // 结合字符缓冲流使用
        BufferedWriter bfwi = new BufferedWriter(osw);                                      // 结合字符缓冲流使用

        //// 【5】、基本数据IO流。【一般用于网络传输】
        DataOutputStream dos = new DataOutputStream(new FileOutputStream("C:\\tmp\\dataOut.txt"));  // 建基本数据类型读流
        dos.writeUTF("刘某人");                                                                         // 写String数据
        dos.writeByte(7);                                                                                 // 写byte数据
        dos.writeInt(29);                                                                                 // 写int数据
        dos.writeChar('男');                                                                              // 写char数据
        dos.writeDouble(9999.9);                                                                           // 写Double数据
        dos.writeBoolean(false);                                                                          // 写Boolean数据
        DataInputStream dis = new DataInputStream(new FileInputStream("C:\\tmp\\dataOut.txt"));  // 建基本数据类型写流
        String naime1 = dis.readUTF();                                                                     // 读String数据
        byte id1 = dis.readByte();                                                                         // 读byte数据
        int age1 = dis.readInt();                                                                          // 读int数据
        char gander1 = dis.readChar();                                                                     // 读char数据
        double price1 = dis.readDouble();                                                                  // 读Double数据
        boolean hun1 = dis.readBoolean();                                                                  // 读写Boolean数据

        //// 【6】、对象IO流。【一般用于开发的网络传输bean对象】
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("C:\\tmp\\object.txt"));       // 创建对象输出流
        oos.writeObject(new Student("刘某人", 29, new Game()));                                          // 将对象写入文件
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream("C:\\tmp\\object.txt"));          // 创建对象输入流
        Student s = (Student)ois.readObject();                                                                          // 读取文件里的对象内容
    }
}

// 对象IO流使用
class Student implements Serializable, Externalizable {
    private static final long serialVersionUID = 361883723364547858L;
    private String name;
    private int age;
    private Game game;

    public Student() {
    }

    public Student(String name, int age, Game game) {
        this.name = name;
        this.age = age;
        this.game = game;
    }

    @Override
    public String toString() {
        return "Student2{" + "name='" + name + '\'' + ", age=" + age + ", game=" + game + '}';
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeUTF(name);
        out.writeInt(age);
        out.writeObject(game);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        this.name = in.readUTF();
        this.age = in.readInt();
        this.game = (Game) in.readObject();
    }
}

// 对象IO传输时内部属性还有Game引用数据类型对象
class Game implements Serializable, Externalizable {
    private static final long serialVersionUID = 361883723364547857L;

    public Game() {
    }

    @Override
    public String toString() {
        return "Game{过家家!}";
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
    }
}
