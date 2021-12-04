package consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
//import org.influxdb.dto.Point;

import java.io.*;
import java.util.Arrays;
import java.util.Properties;

/**
 * @author lizhong.liu
 * @version TODO
 * @class 高阶API消费者,消费Kafka数据写如本地文件
 * @CalssName ConsumerHigh
 * @create 2020-04-01 13:39
 * @Des TODO
 */
public class ConsumerHigh {
    public static void main(String[] args) throws IOException {
        Properties pro = new Properties();
        try {
            pro.load(new FileInputStream(new File("src\\main\\java\\alltool\\Kafka\\consumer\\consumer.properties")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        //消费者
        KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(pro);
        //发布订阅主题(可多主题)
        consumer.subscribe(Arrays.asList("test"));
        while (true) {
            //消费数据
            ConsumerRecords<String, String> records = consumer.poll(1000);
            for (ConsumerRecord<String, String> record : records) {
/*  // 【1】获取值方式
                //获取主题、偏移量、分区、key、value
                String topic = record.topic();
                long offset = record.offset();
                String key = record.key();
                String value = record.value();
                int partition = record.partition();
                System.out.println("topic:" + topic + "\tpartition:" + partition + "\toffset:" + offset + "\tkey:" + key + "\tvalue:" + value);
*/
                // 【2】写入本地文件
                File file = new File("F:\\opckafka\\opcdata\\" + record.value().split("---")[4].substring(0, 7) + "\\" + record.value().split("---")[4].substring(0, 10) + ".txt");
                File fileParents = new File("F:\\opckafka\\opcdata\\" + record.value().split("---")[4].substring(0, 7));
                //System.out.println(file.getAbsolutePath());
                if (!fileParents.exists()) {
                    fileParents.mkdirs();
                }
                if (!file.exists()) {
                    file.createNewFile();
                }
                BufferedWriter bw = new BufferedWriter(new FileWriter(file, true));
                String[] split_record = record.value().split("---");
//                String recordString = "opcall,opcname=" + split_record[1] + " status=" + split_record[3] + ",equipmenttime=" + split_record[0].replaceAll(" ", "-") + ",opcvalue=" + split_record[2].toString() + " " + dateFormat.parse(split_record[4]).getTime();
                String recordString = split_record[1] + ","
                        + split_record[3] + ","
                        + split_record[0].replaceAll(" ", "-") + ","
                        + split_record[2] + ","
                        + split_record[4].replaceAll(" ", "-");
                bw.write(recordString + "\r\n");
                bw.flush();

/*              // 【3】写如Influxdb数据库
                influxDB.setDatabase("tashanopc");
                influxDB.write(Point.measurement("opcall")
                        .time(dateFormat.parse(split_record[4]).getTime(), TimeUnit.MILLISECONDS)
                        .tag("opcname", split_record[1])
                        .addField("status", split_record[3])
                        .addField("equipmenttime", split_record[0])
                        .addField("opcvalue", split_record[2].toString())
                        .build());
*/
            }
        }
    }
}
