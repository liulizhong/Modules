package utils;

import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.NamespaceDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;

/**
 * 1.创建命名空间
 * 2.判断表是否存在
 * 3.创建表
 * 4.创建预分区健
 */
public class HBaseUtil {
    // 1、创建命名空间
    public static void createNS(String ns) {
        try {
            //获取连接
            Connection connection = ConnectionFactory.createConnection(Contant.CONFIGURATION);
            //获取Admin对象
            Admin admin = connection.getAdmin();
            //创建命名空间描述器
            NamespaceDescriptor namespaceDescriptor = NamespaceDescriptor.create(ns).build();
            //创建命名空间操作
            try {
                admin.createNamespace(namespaceDescriptor);
            } catch (IOException e) {
                System.out.println(ns + "命名空间已存在！！！");
                admin.close();
                connection.close();
                return;
            }
            //关闭资源
            admin.close();
            connection.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 2、判断表是否存在
    private static boolean isTableExist(String tableName) {
        try {
            //获取连接
            Connection connection = ConnectionFactory.createConnection(Contant.CONFIGURATION);
            //获取Admin对象
            Admin admin = connection.getAdmin();
            //判断
            boolean exists = admin.tableExists(TableName.valueOf(tableName));
            //资源关闭
            admin.close();
            connection.close();
            //返回结果
            return exists;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 3、创建表
    public static void createTable(String tableName, String... cfs) {
        //判断列族是否存在
        if (cfs.length <= 0) {
            System.out.println("请设置列族信息！！！");
            return;
        }
        //判断表是否存在
        if (isTableExist(tableName)) {
            System.out.println(tableName + "表已存在！！！");
            return;
        }
        try {
            //获取连接
            Connection connection = ConnectionFactory.createConnection(Contant.CONFIGURATION);
            //获取Admin对象
            Admin admin = connection.getAdmin();
            //创建表描述器
            HTableDescriptor hTableDescriptor = new HTableDescriptor(TableName.valueOf(tableName));
            //循环添加列族描述器
            for (String cf : cfs) {
                hTableDescriptor.addFamily(new HColumnDescriptor(cf));
            }
            //获取分区数
            int regions = Integer.parseInt(PropertiesUtil.getProperty("hbase.regions"));
            //注册协处理器
            hTableDescriptor.addCoprocessor("com.atguigu.coprocessor.CalleeCoprocessor");
            //执行创建表的操作
            admin.createTable(hTableDescriptor, getSplits(regions));
            //关闭资源
            admin.close();
            connection.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //获取分区健[0|,1|,2|...]
    private static byte[][] getSplits(int regions) {
        //创建一个二维字节数组
        byte[][] splits = new byte[regions][];
        //给二维字节数组赋值
        for (int i = 0; i < regions; i++) {
            splits[i] = Bytes.toBytes(i + "|");
        }
        return splits;
    }
}
