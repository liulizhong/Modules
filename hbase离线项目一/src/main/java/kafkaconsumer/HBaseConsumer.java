package kafkaconsumer;

import utils.HBaseDAO;
import utils.PropertiesUtil;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.Collections;

/**
 * @author lizhong.liu
 * @version TODO
 * @class ??
 * @CalssName HBaseConsumer
 * @create 2020-08-10 17:40
 * @Des TODO
 */
public class HBaseConsumer {
    public static void main(String[] args) {
        KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<String, String>(PropertiesUtil.properties);
        kafkaConsumer.subscribe(Collections.singletonList(PropertiesUtil.getProperty("kafka.topic")));
        HBaseDAO hBaseDAO = new HBaseDAO();
        while (true) {
            ConsumerRecords<String, String> records = kafkaConsumer.poll(100);
            for (ConsumerRecord cr : records) {
                System.out.println(cr.value());
                hBaseDAO.puts(cr.value().toString());
            }
        }
    }
}
