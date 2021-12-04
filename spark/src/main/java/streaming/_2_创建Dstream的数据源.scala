package streaming

import java.io
import java.io.{BufferedReader, InputStreamReader}
import java.net.Socket

import kafka.serializer.StringDecoder
import org.apache.kafka.clients.consumer.{ConsumerConfig, ConsumerRecord}
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming.dstream.{DStream, InputDStream, ReceiverInputDStream}
import org.apache.spark.streaming.kafka010.{ConsumerStrategies, KafkaUtils, LocationStrategies}
import org.apache.spark.streaming.receiver.Receiver
import org.apache.spark.streaming.{Seconds, StreamingContext}

import scala.collection.mutable

/**
  * @class Spark-Streaming 接收器对接的数据源创建的所有方式
  * @CalssName _2_创建Dstream的数据源
  * @author lizhong.liu 
  * @create 2020-07-02 11:32
  * @Des TODO
  * @version TODO
  */
object _2_创建Dstream的数据源 {
  def main(args: Array[String]): Unit = {
    //1.初始化SparkStreamingContext
    val sparkConf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("StreamWordCount")
    val ssc: StreamingContext = new StreamingContext(sparkConf, Seconds(5))

    // 2、文件数据源：
    //          1）文件数据格式统一 2）文件进入目录需通过移动或重命名方式实现 3）文件一旦进入布恩那个再修改，及时修改也读取不到数据
    val hdfsDS: DStream[String] = ssc.textFileStream("input")
    hdfsDS.flatMap(_.split(" ")).map((_, 1)).reduceByKey(_ + _).print()

    // 3、RDD队列数据源
    val inputDS: InputDStream[Int] = ssc.queueStream(new mutable.Queue[RDD[Int]](), oneAtATime = false)
    inputDS.map((_, 1)).reduceByKey(_ + _).print()

    // 4、自定义数据输入源应用
    //    val lineStreams: ReceiverInputDStream[String] = ssc.receiverStream(new CustomerReceiver("192.168.1.241",9999))
    val lineStreams: ReceiverInputDStream[String] = ssc.socketTextStream("192.168.1.241", 9999)
    lineStreams.flatMap(_.split(" ")).map((_, 1)).reduceByKey(_ + _).print()

    // 5、对接Kafka数据源，消费Kafka数据并作简单处理，并打印在控制台
    //       1）将kafka参数映射为map
    var kafkaParam: mutable.Map[String, io.Serializable] = collection.mutable.Map(
      "bootstrap.servers" -> "hadoop102:9092,hadoop103:9092,hadoop104:9092", //用于初始化链接到集群的地址
      "key.deserializer" -> classOf[StringDeserializer],
      "value.deserializer" -> classOf[StringDeserializer],
      "group.id" -> "spark",    //用于标识这个消费者属于哪个消费团体
      "auto.offset.reset" -> "latest",    //latest自动重置偏移量为最新的偏移量
      //如果是true，则这个消费者的偏移量会在后台自动提交,但是kafka宕机容易丢失数据
      //如果是false，会需要手动维护kafka偏移量
      "enable.auto.commit" -> (false: java.lang.Boolean)
    )
    //       2）通过KafkaUtil创建kafkaDSteam
    val kafkaDSteam: InputDStream[ConsumerRecord[String, String]] = KafkaUtils.createDirectStream[String, String](
      ssc,
      LocationStrategies.PreferConsistent,
      ConsumerStrategies.Subscribe[String, String](Array("topic_spark"), kafkaParam)
    )
    //       3）对kafkaDSteam做计算（WordCount）
    kafkaDSteam.flatMap(_.value().split(" ")).map((_, 1)).reduceByKey(_ + _).print

    // 6、启动SparkStreamingContext流式处理，awaitTermination是流式处理的等待数据意思
    ssc.start()
    ssc.awaitTermination()
  }
}

// 自定义接收器，接收spark的数据源数据（string泛型为接受数据类型，MEMORY_ONLY 为数据存储级别：只内存）
class CustomerReceiver(host: String, port: Int) extends Receiver[String](StorageLevel.MEMORY_ONLY) {
  // 用来关闭线程的标志
  var runflg = true
  // 接收数据方法
  def receive(): Unit = {
    // 1、创建socket
    val socket: Socket = new Socket(host, port)
    // 2、创建缓冲读取流
    val bufferedReader: BufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream, "utf-8"))
    // 3、定义接收的数据
    var input: String = null
    input = bufferedReader.readLine()
    // 4、当接收的数据是“END”时退出循环
    while (!isStopped() && input != null && "END".equals(input)) {
      store(input)
      input = bufferedReader.readLine()
    }
    // 5、跳出循环则关闭资源
    bufferedReader.close()
    socket.close()
    // 6、重启任务
    restart("restart")
  }

  // 一、接收数据
  override def onStart(): Unit = {
    new Thread("Soket Receiver") {
      override def run(): Unit = {
        while (runflg) {
          receive()
        }
      }
    }.start()
  }

  // 二、停止，若直接用stop，那么会导致数据出错，线程直接关闭
  override def onStop(): Unit = {
    runflg = false
  }
}
