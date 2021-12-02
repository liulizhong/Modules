package hbase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lizhong.liu
 * @version TODO
 * @class HBase-API 常用方法
 * @CalssName HbaseAPI
 * @create 2020-05-07 17:11
 * @Des TODO
 */
public class HbaseAPI {
    private static Configuration conf;
    private static HBaseAdmin admin;

    static {
        conf = HBaseConfiguration.create();
        // 配置zookeeper集群
        conf.set("hbase.zookeeper.quorum", "192.168.1.241");
        conf.set("hbase.zookeeper.property.clientPort", "2181");
//        conf.set("hbase.rootdir", "hdfs://192.168.1.241:8020/apps/hbase/data");
//        conf.set("zookeeper.znode.parent", "/hbase-unsecure");
        try {
            //创建HBase连接和admin
            Connection connection = ConnectionFactory.createConnection(conf);
            admin = (HBaseAdmin) connection.getAdmin();
            System.out.println("connection::" + connection);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 1、判断变名是否存在
     *
     * @param tableName
     * @return 是否存在的Boolean
     * @throws IOException
     */
    public static boolean isTableExist(String tableName) throws IOException {
        return admin.tableExists(tableName);
    }

    /**
     * 2、创建表，返回创建结果
     *
     * @param tableName
     * @param columnFamilys
     * @return
     * @throws IOException
     */
    public static boolean createTable(String tableName, String... columnFamilys) throws IOException {
        // 判断表是否已经存在
        if (admin.tableExists(tableName)) {
            System.out.println("表" + tableName + "已经存在！！！");
            return false;
        } else {
            // 创建表样例-后续直接 admin.createTable 创建表
            HTableDescriptor hTableDescriptor = new HTableDescriptor(tableName);
            for (String columnFamily : columnFamilys) {
                hTableDescriptor.addFamily(new HColumnDescriptor(columnFamily));
            }
            admin.createTable(hTableDescriptor);
            System.out.println("表" + tableName + "创建成功！！！");
            return admin.tableExists(tableName);
        }
    }

    /**
     * 3、删除表，返回是否删除成功
     *
     * @param tableName
     * @return
     * @throws MasterNotRunningException
     * @throws ZooKeeperConnectionException
     * @throws IOException
     */
    public static boolean dropTable(String tableName) throws MasterNotRunningException, ZooKeeperConnectionException, IOException {
        // 判断表是否存在
        if (isTableExist(tableName)) {
            // 标记表未删除后删除表
            admin.disableTable(tableName);
            admin.deleteTable(tableName);
            System.out.println("表" + tableName + "删除成功！");
            return true;
        } else {
            System.out.println("表" + tableName + "不存在！");
            return false;
        }
    }

    /**
     * 4、向hbase种插入数据
     *
     * @param tableName
     * @param rowKey
     * @param columnFamily
     * @param column
     * @param value
     * @throws IOException
     */
    public static void addRowData(String tableName, String rowKey, String columnFamily, String column, String value) throws IOException {
        // 创建HTable对象
        HTable hTable = new HTable(conf, tableName);
        // 向表中插入数据
        Put put = new Put(Bytes.toBytes(rowKey));
        // 向hbase.client.Put对象中组装数据（也可通过Cell封装）
        put.add(Bytes.toBytes(columnFamily), Bytes.toBytes(column), Bytes.toBytes(value));
        hTable.put(put);
        hTable.close();
        System.out.println("插入数据成功");
    }

    /**
     * 5、删除多行数据
     *
     * @param tableName
     * @param rows
     * @throws IOException
     */
    public static void deleteMultiRow(String tableName, String... rows) throws IOException {
        HTable hTable = new HTable(conf, tableName);
        List<Delete> deleteList = new ArrayList<Delete>();
        for (String row : rows) {
            Delete delete = new Delete(Bytes.toBytes(row));
            deleteList.add(delete);
        }
        hTable.delete(deleteList);
        hTable.close();
    }

    /**
     * 6、获取某个表所有数据
     *
     * @param tableName
     * @throws IOException
     */
    public static ResultScanner getAllRows(String tableName) throws IOException {
        HTable hTable = new HTable(conf, tableName);
        //得到用于扫描region的对象
        Scan scan = new Scan();
        //使用HTable得到resultcanner实现类的对象
        ResultScanner resultScanner = hTable.getScanner(scan);
        /*for (Result result : resultScanner) {
            Cell[] cells = result.rawCells();
            for (Cell cell : cells) {
                //得到rowkey、列族、列名、列值
                System.out.println("行键:" + Bytes.toString(CellUtil.cloneRow(cell)));
                System.out.println("列族" + Bytes.toString(CellUtil.cloneFamily(cell)));
                System.out.println("列:" + Bytes.toString(CellUtil.cloneQualifier(cell)));
                System.out.println("值:" + Bytes.toString(CellUtil.cloneValue(cell)));
            }
        }*/
        return resultScanner;
    }

    /**
     * 7、获取某个表指定rowkey的单行数据
     *
     * @param tableName
     * @param rowKey
     * @return
     * @throws IOException
     */
    public static Result getRow(String tableName, String rowKey) throws IOException {
        HTable table = new HTable(conf, tableName);
        Get get = new Get(Bytes.toBytes(rowKey));
        //get.setMaxVersions();显示所有版本
        //get.setTimeStamp();显示指定时间戳的版本
        Result result = table.get(get);
        /* for(Cell cell : result.rawCells()){
            System.out.println("行键:" + Bytes.toString(result.getRow()));
            System.out.println("列族" + Bytes.toString(CellUtil.cloneFamily(cell)));
            System.out.println("列:" + Bytes.toString(CellUtil.cloneQualifier(cell)));
            System.out.println("值:" + Bytes.toString(CellUtil.cloneValue(cell)));
            System.out.println("时间戳:" + cell.getTimestamp());
        } */
        return result;
    }

    /**
     * 8、获取指定表名、rowkey、列族、列名的数据
     *
     * @param tableName
     * @param rowKey
     * @param family
     * @param qualifier
     * @return
     * @throws IOException
     */
    public static Result getRowQualifier(String tableName, String rowKey, String family, String qualifier) throws IOException {
        HTable table = new HTable(conf, tableName);
        Get get = new Get(Bytes.toBytes(rowKey));
        get.addColumn(Bytes.toBytes(family), Bytes.toBytes(qualifier));
        Result result = table.get(get);
        /* for(Cell cell : result.rawCells()){
            System.out.println("行键:" + Bytes.toString(result.getRow()));
            System.out.println("列族" + Bytes.toString(CellUtil.cloneFamily(cell)));
            System.out.println("列:" + Bytes.toString(CellUtil.cloneQualifier(cell)));
            System.out.println("值:" + Bytes.toString(CellUtil.cloneValue(cell)));
        } */
        return result;
    }
}
