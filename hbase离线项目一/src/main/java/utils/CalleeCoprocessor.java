package utils;

import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.coprocessor.BaseRegionObserver;
import org.apache.hadoop.hbase.coprocessor.ObserverContext;
import org.apache.hadoop.hbase.coprocessor.RegionCoprocessorEnvironment;
import org.apache.hadoop.hbase.regionserver.wal.WALEdit;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;

// 协处理器作用：把原本插入的数据修改样式再插入一份(rowkey中flag改为1表示主被叫反过来，value值是主被叫电话号调换过来)
// 协处理器注册位置：创建表在表描述器时候注册协处理器。
public class CalleeCoprocessor extends BaseRegionObserver {

    //获取分区数
    private int regions = Integer.parseInt(PropertiesUtil.getProperty("hbase.regions"));

    @Override
    public void postPut(ObserverContext<RegionCoprocessorEnvironment> e, Put put, WALEdit edit, Durability durability) throws IOException {

        //获取环境中的表名(当前操作的表)
        String currentTable = e.getEnvironment().getRegionInfo().getTable().getNameAsString();

        //获取配置文件中的表
        String tableName = PropertiesUtil.getProperty("hbase.table.name");

        //判断当前操作的表和配置中的表是否一致
        if (!currentTable.equals(tableName)) {
            return;
        }

        //获取主叫数据的rowkey:X_14473548449_2019-07-12 04:27:18_15133295266_0_1298
        String oldRowKey = Bytes.toString(put.getRow());

        //截取字段
        String[] split = oldRowKey.split("_");

        //判断，如果此时是被叫数据，则直接返回
        if ("1".equals(split[4])) {
            return;
        }

        String caller = split[1];//主叫
        String buildTime = split[2];//通话建立时间
        String callee = split[3];//被叫
        String duration = split[5];//通话时长

        //获取分区号
        String splitNum = HBaseDAO.getSplitNum(callee, buildTime, regions);

        //获取rowkey
        String calleeRowKey = HBaseDAO.getRowKey(splitNum, callee, buildTime, caller, "1", duration);

        //创建Put对象
        Put calleePut = new Put(Bytes.toBytes(calleeRowKey));

        //给Put对象添加数据
        calleePut.addColumn(Bytes.toBytes(PropertiesUtil.getProperty("hbase.columnFamily")), Bytes.toBytes("call1"), Bytes.toBytes(callee));
        calleePut.addColumn(Bytes.toBytes(PropertiesUtil.getProperty("hbase.columnFamily")), Bytes.toBytes("buildTime"), Bytes.toBytes(buildTime));
        calleePut.addColumn(Bytes.toBytes(PropertiesUtil.getProperty("hbase.columnFamily")), Bytes.toBytes("call2"), Bytes.toBytes(caller));
        calleePut.addColumn(Bytes.toBytes(PropertiesUtil.getProperty("hbase.columnFamily")), Bytes.toBytes("duration"), Bytes.toBytes(duration));

        //获取连接&Table对象
        Connection connection = ConnectionFactory.createConnection(Contant.CONFIGURATION);
        Table table = connection.getTable(TableName.valueOf(tableName));

        //执行被叫数据的插入操作
        table.put(calleePut);

        //关闭资源
        table.close();
        connection.close();
    }
}
