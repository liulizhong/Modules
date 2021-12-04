package utils;

import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 1.封装数据批量操作
 * 2.rowKey设计
 */
public class HBaseDAO {

    //声明相关属性
    private Connection connection; //HBASE连接
    private Table table; //HBase表对象
    private List<Put> putList; //存放批量提交的Put对象的集合
    private String ns;   //命名空间
    private String tableName; //表名
    private String cf;    //列族信息
    private int regions;   //欲分区数

    //初始化相关属性(创建命名空间&表)
    public HBaseDAO() {
        try {
            connection = ConnectionFactory.createConnection(Contant.CONFIGURATION);
            ns = PropertiesUtil.getProperty("hbase.namespace");
            tableName = PropertiesUtil.getProperty("hbase.table.name");
            cf = PropertiesUtil.getProperty("hbase.columnFamily");
            regions = Integer.parseInt(PropertiesUtil.getProperty("hbase.regions"));

            //创建命名空间&表
            HBaseUtil.createNS(ns);
            HBaseUtil.createTable(tableName, cf);

            //获取表对象
            table = connection.getTable(TableName.valueOf(tableName));

            //put集合
            putList = new ArrayList<Put>();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 获取分区号:13412341234,2019-01-01 12:12:12
    public static String getSplitNum(String phoneNum, String buildTime, int regions) {

        //获取手机号后4位
        String last4Num = phoneNum.substring(7);

        //获取年月
        String yearMonth = buildTime.replaceAll("-", "").substring(0, 6);

        int splitNum = (Integer.parseInt(last4Num) ^ Integer.parseInt(yearMonth)) % regions;

        return splitNum + "";
    }

    //获取RowKey(flag标签代表：0-主叫，1-被叫，也就是说一条数据要存两份！)
    public static String getRowKey(String splitNum, String call1, String buildTime, String call2, String flag, String duration) {

        return splitNum + "_" +
                call1 + "_" +
                buildTime + "_" +
                call2 + "_" +
                flag + "_" +
                duration;
    }

    //14473548449,15133295266,2019-07-12 04:27:18,1298
    public void puts(String value) {

        //判断数据是否合法
        String[] splits = value.split(",");
        if (splits.length < 4) {
            return;
        }

        //取出相应字段
        String call1 = splits[0];
        String call2 = splits[1];
        String buildTime = splits[2];
        String duration = splits[3];

        //获取分区号
        String splitNum = getSplitNum(call1, buildTime, regions);

        //获取RowKey:X_14473548449_2019-07-12 04:27:18_15133295266_1298
        String rowKey = getRowKey(splitNum, call1, buildTime, call2, "0", duration);

        //创建Put对象
        Put put = new Put(Bytes.toBytes(rowKey));

        //给Put对象添加数据
        put.addColumn(Bytes.toBytes(cf), Bytes.toBytes("call1"), Bytes.toBytes(call1));
        put.addColumn(Bytes.toBytes(cf), Bytes.toBytes("buildTime"), Bytes.toBytes(buildTime));
        put.addColumn(Bytes.toBytes(cf), Bytes.toBytes("call2"), Bytes.toBytes(call2));
        put.addColumn(Bytes.toBytes(cf), Bytes.toBytes("duration"), Bytes.toBytes(duration));

        //将Put对象存入集合
        putList.add(put);

        //判断集合大小，超过阈值，则写出
        if (putList.size() >= 20) {
            //写出
            try {
                table.put(putList);
                //清空集合
                putList.clear();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}