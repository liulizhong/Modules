package consumer;

import kafka.api.FetchRequestBuilder;
import kafka.javaapi.*;
import kafka.javaapi.consumer.SimpleConsumer;
import kafka.javaapi.message.ByteBufferMessageSet;
import kafka.message.Message;
import kafka.message.MessageAndOffset;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author lizhong.liu
 * @version TODO
 * @class 低阶API消费者
 * @CalssName ConsumerLow
 * @create 2020-04-01 13:39
 * @Des TODO
 */
public class ConsumerLow {
    public static void main(String[] args) {
        //1、brokers列表
        ArrayList<String> brokers = new ArrayList<String>();
        brokers.add("10.238.255.151");
        brokers.add("10.238.255.152");
        brokers.add("10.238.255.153");
        //2、主题
        String topic = "rangetest";
        //3、分区
        int partition = 0;
        //4、开始消费的offset位置
        Long offset = 10L;
        //5、获取leader
        String leader = getLeader(brokers, topic, partition);
        //6、消费数据
        getData(leader, partition, topic, offset);
    }

    /**
     * 获取指定分区的leader.host
     *
     * @param brokers
     * @param topic
     * @param partition
     * @return leader
     */
    private static String getLeader(ArrayList<String> brokers, String topic, int partition) {
        for (String broker : brokers) {
            //1、创建SimpleConsumer
            SimpleConsumer simpleConsumer = new SimpleConsumer(broker, 9092, 3000, 1024 * 1024, "getLeader");
            //2、封装获取leader的请求
            TopicMetadataRequest topicMetadataRequest = new TopicMetadataRequest(Arrays.asList(topic));
            //3、发送请求，获取响应
            TopicMetadataResponse topicMetadataResponse = simpleConsumer.send(topicMetadataRequest);
            //4、解析响应集
            List<TopicMetadata> topicsMetadata = topicMetadataResponse.topicsMetadata();
            //5、遍历获取每个响应
            for (TopicMetadata topicMetadata : topicsMetadata) {
                //6、解析每个响应获取分区响应集
                List<PartitionMetadata> partitionsMetadata = topicMetadata.partitionsMetadata();
                //7、遍历取分区响应集
                for (PartitionMetadata partitionMetadata : partitionsMetadata) {
                    //8、获取指定分区leader(也可获取所有分区的leader)
                    if (partitionMetadata.partitionId() == partition) {
                        return partitionMetadata.leader().host();
                    }
                }
            }
        }
        return null;
    }

    /**
     * 消费指定 topic / partition / offset 的数据
     *
     * @param leader
     * @param partition
     * @param topic
     * @param offset
     */
    private static void getData(String leader, int partition, String topic, Long offset) {
        //1.创建SimpleConsumer
        SimpleConsumer consumer = new SimpleConsumer(leader, 9092, 3000, 1024 * 1024, "消费数据");
        //2.封装获取数据的请求
        FetchRequestBuilder fetchRequestBuilder = new FetchRequestBuilder();
        kafka.api.FetchRequest fetchRequest = fetchRequestBuilder.addFetch(topic, partition, offset, 1024 * 1024).build();
        //3.发送请求，获取响应
        FetchResponse fetchResponse = consumer.fetch(fetchRequest);
        //4.获取指定topic和partition的响应集
        ByteBufferMessageSet messageAndOffsets = fetchResponse.messageSet(topic, partition);
        //5.遍历messageAndOffsets
        for (MessageAndOffset messageAndOffset : messageAndOffsets) {
            //6、获取offset
            long newOffset = messageAndOffset.offset();
            //7、获取message
            Message message = messageAndOffset.message();
            ByteBuffer payload = message.payload();
            byte[] bytes = new byte[payload.limit()];
            payload.get(bytes);
            String newmessage = new String(bytes);
            System.out.println("value:" + newmessage + "\toffset:" + offset);
        }
    }
}
