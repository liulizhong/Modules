package azkaban;

import java.io.FileOutputStream;
import java.io.IOException;

/*
 * @class azkaban测试联系
 */
public class Shell_Java_Hadoop_Hive {
    public static void run() throws IOException {
        // 根据需求编写具体代码
        FileOutputStream fos = new FileOutputStream("/opt/module/azkaban/output.txt");
        fos.write("1001    zhangsan\n".getBytes());
        fos.write("1002    lisi\n".getBytes());
        fos.write("1003    wangwu\n".getBytes());
        fos.write("1004    zhaoliu\n".getBytes());
        fos.close();
    }

    public static void main(String[] args) throws IOException {
        Shell_Java_Hadoop_Hive azkaban = new Shell_Java_Hadoop_Hive();
        Shell_Java_Hadoop_Hive.run();
    }
}
