package producer;

import org.apache.kafka.clients.producer.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * @author lizhong.liu
 * @version TODO
 * @class 回调函数生产者
 * @CalssName CallBackProducer
 * @create 2020-03-31 16:27
 * @Des TODO
 */
public class ProducerCallBack {
    public static void main(String[] args) throws Exception {
        //读取配置文件pro
        Properties pro = new Properties();
        try {
            pro.load(new FileInputStream(new File("src\\main\\java\\alltool\\Kafka\\producer\\producer.properties")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        pro.put("group.id", "test");
        System.out.println(pro);
        //配置自定义分区类
        pro.put("partitioner.class", "alltool.Kafka.producer.ProducerPartitioner");
        //配置拦截器类
        pro.put("interceptor.classes", "alltool.Kafka.producer.InterceptoraddTimestap");
        KafkaProducer<String, String> producer = new KafkaProducer<String, String>(pro);
        //模拟生产数据
        for (int i = 0; i < 10; i++) {
            System.out.println("1====>");
            //发送数据：最少参数(topic, [partition], [key], value)
            producer.send(
            //分区指定顺序：send(partition=1) >  自定义分区类 > 默认的hashcode分区方法
                    new ProducerRecord<String, String>("rangetest", i + "", "测试数据" + (i + 1)),
                    //第二个参数Callback回调函数（可不传，表示不带回调函数）
                    new Callback() {
                        //默认实现方法：就是生产者发送数据到Kafka集群后，集群把Record的元数据信息返回，弱没收到数据则返回Exception
                        public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                            System.out.println(recordMetadata.topic());     //返回主题名
                            System.out.println(recordMetadata.offset());    //返回偏移量
                            System.out.println(recordMetadata.partition()); //返回分区
                            System.out.println(recordMetadata.timestamp()); //返回时间戳
                        }
                    });
        }
        Thread.sleep(3000);
        System.out.println("3====>");
        producer.close();
    }
}
