package kafkaconsumer;

import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import utils.Contant;
import utils.HBaseScanUtil;
import utils.PropertiesUtil;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

/**
 * @author lizhong.liu
 * @version TODO
 * @class ??
 * @CalssName HbaseScananalyze
 * @create 2020-08-12 10:08
 * @Des TODO
 */
public class HbaseScananAlyze {
    public static void main(String[] args) throws IOException, ParseException {
        //获取连接&表对象
        Connection connection = ConnectionFactory.createConnection(Contant.CONFIGURATION);
        Table table = connection.getTable(TableName.valueOf(PropertiesUtil.getProperty("hbase.table.name")));
        //获取startStopRow
        List<String[]> startStopRow = HBaseScanUtil.getStartStopRow("15064972307", "2019-06", "2019-11");
        System.out.println("******************************");
        for (String[] strings : startStopRow) {
            System.out.println(strings[0] + ", " + strings[1]);
        }
        System.out.println("******************************");
        //遍历startStopRow，封装扫描对象
        for (String[] rows : startStopRow) {
            //获取扫描对象
            Scan scan = new Scan(Bytes.toBytes(rows[0]), Bytes.toBytes(rows[1]));
            //扫描表
            ResultScanner results = table.getScanner(scan);
            //遍历结果
            for (Result result : results) {
                System.out.println(Bytes.toString(result.getRow()));
            }
        }
        //关闭资源
        table.close();
        connection.close();
    }
}
