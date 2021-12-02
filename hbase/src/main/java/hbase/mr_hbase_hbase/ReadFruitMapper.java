package hbase.mr_hbase_hbase;

import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableMapper;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;


/*
    hbase已经指定了输入的key和value，咱们需要指定输出的key和value就行。key相同，mapper的value是Put类型
 */
public class ReadFruitMapper extends TableMapper<ImmutableBytesWritable, Put> {
    @Override
    protected void map(ImmutableBytesWritable key, Result value, Context context) throws IOException, InterruptedException {
        Put put = new Put(key.get());
        for (Cell cell : value.rawCells()) {
            byte[] columnFamily = CellUtil.cloneFamily(cell);
            byte[] column = CellUtil.cloneQualifier(cell);
            byte[] val = CellUtil.cloneValue(cell);

            //如果有字段名字不一样的可以这样处理
            String colString = Bytes.toString(column);
            if (colString.equals("name")) {
                column = Bytes.toBytes("fname");
            }

            put.addColumn(columnFamily, column, val);
        }
        context.write(key, put);
    }
}

