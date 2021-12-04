package producer;

import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Map;

/**
 * @author lizhong.liu
 * @version TODO
 * @class Kafka拦截器
 * @CalssName addTimestapInterceptor
 * @create 2020-04-01 17:59
 * @Des TODO
 */
public class InterceptoraddTimestap implements ProducerInterceptor<String, String> {
    private int errorCounter = 0;
    private int successCounter = 0;

    /**
     * 1、在消息被序列化以及计算分区前调用该方法，可在此处修改value值，不建议修改分区及topic
     */
    public ProducerRecord<String, String> onSend(ProducerRecord<String, String> producerRecord) {
        return new ProducerRecord(
                producerRecord.topic(),
                producerRecord.partition(),
                producerRecord.timestamp(),
                producerRecord.key(),
                System.currentTimeMillis() + "-" + producerRecord.value().toString());
    }

    /**
     * 2、该方法会在消息被应答或消息发送失败时调用，并且通常都是在producer回调逻辑触发之前。不要有太重的逻辑，否则会拖慢Kafka效率
     */
    public void onAcknowledgement(RecordMetadata recordMetadata, Exception e) {

        // 统计成功和失败的次数
        if (e == null) {
            successCounter++;
        } else {
            errorCounter++;
        }
    }

    /**
     * 3、关闭资源
     */
    public void close() {
        // 保存结果
        System.out.println("Successful sent: " + successCounter);
        System.out.println("Failed sent: " + errorCounter);
    }

    /**
     * 4、相同于kafka.properties ( 此处可获取和设置配置文件内容)，再解析配置之前调用
     */
    public void configure(Map<String, ?> map) {
    }
}