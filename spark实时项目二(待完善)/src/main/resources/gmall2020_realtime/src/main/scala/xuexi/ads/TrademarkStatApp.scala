import java.text.SimpleDateFormat
import java.util.Date

import com.alibaba.fastjson.JSON
import xuexi.bean.OrderWide
import xuexi.utils.{MyKafkaUtil, OffsetManagerM}
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.common.TopicPartition
import org.apache.spark.SparkConf
import org.apache.spark.streaming.dstream.{DStream, InputDStream}
import org.apache.spark.streaming.kafka010.{HasOffsetRanges, OffsetRange}
import org.apache.spark.streaming.{Seconds, StreamingContext}
import scalikejdbc.config.DBs
import scalikejdbc.{DB, SQL}

import scala.collection.mutable.ListBuffer

/**
  * Author: lizhong.liu
  * Date: 2020/9/23
  * Desc:  从Kafka中读取dwd层数据，进行处理
  */
object TrademarkStatApp {
  def main(args: Array[String]): Unit = {
    // 加载流 //手动偏移量
    val sparkConf: SparkConf = new SparkConf().setMaster("local[4]").setAppName("TrademarkStatApp")
    val ssc = new StreamingContext(sparkConf, Seconds(5))
    val groupId = "ads_trademark_stat_group"
    val topic = "dws_order_wide";

    //从Mysql中读取偏移量
    val offsetMapForKafka: Map[TopicPartition, Long] = OffsetManagerM.getOffset(topic, groupId)

    //把偏移量传递给kafka ，加载数据流
    var recordInputDstream: InputDStream[ConsumerRecord[String, String]] = null
    if (offsetMapForKafka != null && offsetMapForKafka.size > 0) { //根据是否能取到偏移量来决定如何加载kafka 流
      recordInputDstream = MyKafkaUtil.getKafkaStream(topic, ssc, offsetMapForKafka, groupId)
    } else {
      recordInputDstream = MyKafkaUtil.getKafkaStream(topic, ssc, groupId)
    }

    //从流中获得本批次的 偏移量结束点
    var offsetRanges: Array[OffsetRange] = null
    val inputGetOffsetDstream: DStream[ConsumerRecord[String, String]] = recordInputDstream.transform {
      rdd => {
        offsetRanges = rdd.asInstanceOf[HasOffsetRanges].offsetRanges
        rdd
      }
    }

    //提取数据
    val orderWideDstream: DStream[OrderWide] = inputGetOffsetDstream.map {
      record => {
        val jsonString: String = record.value()
        //订单处理  脱敏  换成特殊字符  直接去掉   转换成更方便操作的专用样例类
        val orderWide: OrderWide = JSON.parseObject(jsonString, classOf[OrderWide])
        orderWide
      }
    }
    // 聚合
    val trademarkAmountDstream: DStream[(String, Double)] = orderWideDstream.map {
      orderWide => {
        (orderWide.tm_id + "_" + orderWide.tm_name, orderWide.final_detail_amount)
      }
    }

    val tradermarkSumDstream: DStream[(String, Double)] = trademarkAmountDstream.reduceByKey(_ + _)
    //tradermarkSumDstream.print(1000)

    //存储数据以及偏移量到MySQL中，为了保证精准消费   我们将使用事务对存储数据和修改偏移量进行控制
    /*//方式1：单条插入
    tradermarkSumDstream.foreachRDD {
      rdd =>{
        // 为了避免分布式事务，把ex的数据提取到driver中;因为做了聚合，所以可以直接将Executor的数据聚合到Driver端
        val tmSumArr: Array[(String, Double)] = rdd.collect()
        if (tmSumArr !=null && tmSumArr.size > 0) {
          DBs.setup()
          DB.localTx {
            implicit session =>{
              // 写入计算结果数据
              val formator = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
              for ((tm, amount) <- tmSumArr) {
                val statTime: String = formator.format(new Date())
                val tmArr: Array[String] = tm.split("_")
                val tmId = tmArr(0)
                val tmName = tmArr(1)
                val amountRound: Double = Math.round(amount * 100D) / 100D
                println("数据写入执行")
                SQL("insert into trademark_amount_stat(stat_time,trademark_id,trademark_name,amount) values(?,?,?,?)")
                  .bind(statTime,tmId,tmName,amountRound).update().apply()
              }
              //throw new RuntimeException("测试异常")
              // 写入偏移量
              for (offsetRange <- offsetRanges) {
                val partitionId: Int = offsetRange.partition
                val untilOffset: Long = offsetRange.untilOffset
                println("偏移量提交执行")
                SQL("replace into offset_0421  values(?,?,?,?)").bind(groupId, topic, partitionId, untilOffset).update().apply()
              }
            }
          }
        }
      }
    }*/

    //方式2：批量插入
    tradermarkSumDstream.foreachRDD {
      rdd => {
        // 为了避免分布式事务，把ex的数据提取到driver中;因为做了聚合，所以可以直接将Executor的数据聚合到Driver端
        val tmSumArr: Array[(String, Double)] = rdd.collect()
        if (tmSumArr != null && tmSumArr.size > 0) {
          DBs.setup() //读取配置文件（开启事务固定写法）
          DB.localTx { //开启事务，结合上一步读取配置文件，此为MySQL的事务型操作
            implicit session => {
              // 写入计算结果数据
              val formator = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
              val dateTime: String = formator.format(new Date())
              val batchParamsList: ListBuffer[Seq[Any]] = ListBuffer[Seq[Any]]()
              for ((tm, amount) <- tmSumArr) {
                val amountRound: Double = Math.round(amount * 100D) / 100D
                val tmArr: Array[String] = tm.split("_")
                val tmId = tmArr(0)
                val tmName = tmArr(1)
                batchParamsList.append(Seq(dateTime, tmId, tmName, amountRound))
              }
              //val params: Seq[Seq[Any]] = Seq(Seq("2020-08-01 10:10:10","101","品牌1",2000.00),
              // Seq("2020-08-01 10:10:10","102","品牌2",3000.00))
              //数据集合作为多个可变参数 的方法 的参数的时候 要加:_*
              SQL("insert into trademark_amount_stat(stat_time,trademark_id,trademark_name,amount) values(?,?,?,?)") // 事务DB.localTx里执行sql语句的固定写法
                .batch(batchParamsList.toSeq: _*).apply()
              //throw new RuntimeException("测试异常")

              // 写入偏移量
              for (offsetRange <- offsetRanges) {
                val partitionId: Int = offsetRange.partition
                val untilOffset: Long = offsetRange.untilOffset
                SQL("replace into offset_0421 values(?,?,?,?)").bind(groupId, topic, partitionId, untilOffset).update().apply()
              }
            }
          }
        }
      }
    }

    ssc.start()
    ssc.awaitTermination()
  }
}
