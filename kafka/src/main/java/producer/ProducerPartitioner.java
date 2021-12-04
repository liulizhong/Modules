package producer;

import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;

import java.util.Map;

/**
 * @author lizhong.liu
 * @version TODO
 * @class 自定义分区类，在producer.properties里加入或者生产者类中pro.put（**,**）加入
 * @CalssName ProducerPartitioner
 * @create 2020-04-01 11:40
 * @Des TODO
 */
public class ProducerPartitioner implements Partitioner {

    /**
     * 方法：返回分区号，计算分区之前调用
     *
     * @param s       topic
     * @param o       key（序列化之前）
     * @param bytes   keyBytes（序列化之后）
     * @param o1      value
     * @param bytes1  valueBytes
     * @param cluster cluster
     * @return 分区号
     */
    public int partition(String s, Object o, byte[] bytes, Object o1, byte[] bytes1, Cluster cluster) {
        return s == null ? o.hashCode() / 3 : s.hashCode() / 3;
    }

    /**
     * 关闭资源方法
     */
    public void close() {

    }

    /**
     * @param map 相同于kafka.properties ( 此处可获取和设置配置文件内容)，再解析配置之前调用
     */
    public void configure(Map<String, ?> map) {
    }
}
