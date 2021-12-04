package streaming

import org.apache.spark.streaming.dstream.{DStream, ReceiverInputDStream}
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.SparkConf

/**
  * @class Spark-Streaming 入门级别WordCount
  * @CalssName _1_简单的WordCount
  * @author lizhong.liu 
  * @create 2020-07-02 10:24
  * @Des TODO
  * @version TODO
  */
object _1_滑动窗口和有状态的WordCount {
  def main(args: Array[String]): Unit = {
    //1.初始化Spark配置信息、初始化SparkStreamingContext(流式处理流批处理时间间隔为 3秒)
    val sparkConf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("StreamWordCount")
    val ssc: StreamingContext = new StreamingContext(sparkConf, Seconds(3))

    // 2、设置检查点（有状态和滑动窗口均有需要检查点）
    ssc.sparkContext.setCheckpointDir("checkpoint")

    // 3、socketTextStream：通过监听端口创建DStream，读进来的数据为一行行
    val lineStreams: ReceiverInputDStream[String] = ssc.socketTextStream("192.168.1.241", 9999)

    // 4、将每一行数据做切分，形成一个个单词
    val wordStreams: DStream[String] = lineStreams.flatMap(_.split(" "))

    // 5、map算子：将单词映射成元组（word,1）
    val wordAndOneStreams: DStream[(String, Int)] = wordStreams.map((_, 1))

    // 6、reduceByKey算子：将相同的单词次数做统计
    val wordAndCountStreams: DStream[(String, Int)] = wordAndOneStreams.reduceByKey(_ + _)
    wordAndCountStreams.print()

    // 7、窗口函数：reduceByKeyAndWindow
    val wordAndCountStreamsWindow: DStream[(String, Int)] = wordAndOneStreams.reduceByKeyAndWindow((x: Int, y: Int) => x + y, Seconds(12), Seconds(6))
    wordAndCountStreamsWindow.print()
//保存到Hbase
    import org.apache.phoenix.spark._
//    objectDstream.foreachRDD{
//      rdd=>{
//        rdd.saveToPhoenix("GMALL0421_BASE_CATEGORY3",
//          Seq("ID", "NAME", "CATEGORY2_ID" )
//          ,new Configuration,Some("hadoop202,hadoop203,hadoop204:2181"))
//        OffsetManagerUtil.saveOffset(topic,groupId, offsetRanges)
//      }
//    }
    // 8、有状态转换：updateStateByKey
    val wordAndCountStreamsStatus: DStream[(String, Int)] = wordAndOneStreams.updateStateByKey((seq: Seq[Int], option: Option[Int]) => {
      val totalcnt: Int = seq.sum + option.getOrElse(0)
      Option(totalcnt)
    })
    wordAndCountStreamsStatus.print()

    // 9、启动SparkStreamingContext流式处理，awaitTermination是流式处理的等待数据意思
    ssc.start()
    ssc.awaitTermination()
  }
}