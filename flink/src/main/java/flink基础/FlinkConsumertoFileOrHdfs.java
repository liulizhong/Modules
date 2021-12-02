package flink基础;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.core.fs.FileSystem;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer010;

import java.util.Calendar;
import java.util.Properties;

/**
 * @author lizhong.liu
 * @version TODO
 * @class flink消费Kafka数据，【1】是写道本地文件【2】是上传HDFS
 * @CalssName FlinkConsumertoFileOrHdfs
 * @create 2020-06-09 15:15
 * @Des TODO
 */
public class FlinkConsumertoFileOrHdfs {
    public static void main(String[] args) throws Exception {
        // 1、创建流处理环境
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        // 2、根据日历动态创建文件夹
        Calendar calendar = Calendar.getInstance();

        // 3、设置并行度
        env.setParallelism(1);
        // 4、设置检查点检查点
        env.enableCheckpointing(10000);
        // 5、从调用时刻开始给env创建的每一个stream追加时间特征
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
        // 6、配置kafka信息
        Properties props = new Properties();
        props.setProperty("bootstrap.servers", "10.238.255.151:9092,10.238.255.152:9092,10.238.255.153:9092");
        props.setProperty("group.id", "kafkato189filetemp");
        // 7、读取数据
        FlinkKafkaConsumer010<String> consumer = new FlinkKafkaConsumer010<String>("test", new SimpleStringSchema(), props);
        // 8、设置只读取最新数据
        consumer.setStartFromLatest();
        // 9、添加kafka为数据源
        DataStream<String> stream = env.addSource(consumer);
        stream = stream.map(new MapFunction<String, String>() {
            public String map(String str) {
                String[] strings = str.split("---");
                return strings[0] + "," + strings[1] + "," + strings[2] + "," + strings[3];
            }
        });

        // 【1】、输出到189本地File
        stream.writeAsText(
                "F:\\opckafkaflink\\opcdata\\" +
                        calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.DAY_OF_MONTH)
                        + "\\" + calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.DAY_OF_MONTH) + ".txt",
//                "D:\\" + calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.DAY_OF_MONTH)
//                        + "\\" + calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.DAY_OF_MONTH) + ".txt",
                FileSystem.WriteMode.NO_OVERWRITE
        );

/*
        // 【2】、输出到245HDFS
        // 10、定义输出sink
        BucketingSink<String> hdfs_sink = new BucketingSink<String>("hdfs://192.168.10.10:8020///opc/" + calendar.get(Calendar.YEAR) + "/" + (calendar.get(Calendar.MONTH) + 1) + "/");
        // 11、文件达到50G或该文件最后一条数据是2小时之前，写入另一个文件
        hdfs_sink.setBatchSize(1024 * 1024 * 1024 * 50L);
        hdfs_sink.setBatchRolloverInterval(120 * 60 * 1000L);
        hdfs_sink.setBucketer(new DateTimeBucketer<String>("yyyy-MM-dd", ZoneId.of("Asia/Shanghai")));
        hdfs_sink.setBucketer(new BasePathBucketer<>());
        // hdfs_sink.setPartPrefix("part");
        hdfs_sink.setPartSuffix(".txt");
        // 12、存到hdfs
        stream.addSink(hdfs_sink);
*/

        // 13、启动executor，执行任务
        env.execute("Kafka-to-189file");
    }
}
