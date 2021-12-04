package project.online

import java.text.SimpleDateFormat
import java.util
import java.util.Date
import project.xbean.KafkaMessage
import project.xutil.{MyKafkaUtil, RedisUtil}
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.spark.SparkConf
import org.apache.spark.broadcast.Broadcast
import org.apache.spark.streaming.dstream.{DStream, InputDStream}
import org.apache.spark.streaming.{Seconds, StreamingContext}
import redis.clients.jedis.Jedis

// 广告点击量实时统计：每天各地区各城市各广告的点击流量实时统计。
object Two_DateAreaCityAdvCountApplication {
  def main(args: Array[String]): Unit = {
    // 【1】、接收Kafka数据，并封装为DStream
    val topic = "ads_log180925";
    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("DateAreaCityAdvCountApplication")
    val streamingContext = new StreamingContext(sparkConf, Seconds(5))
    val dStream: InputDStream[ConsumerRecord[String, String]] = MyKafkaUtil.getKafkaStream(topic, streamingContext)
    val messageDStream: DStream[KafkaMessage] = dStream.map(record => {
      val datas = record.value().split(" ")
      KafkaMessage(datas(0), datas(1), datas(2), datas(3), datas(4))
    })

    // 4.1 将kafka获取的数据进行拆分（date:area:city:ads, 1L）
    val dateAreaCityAdsDStream: DStream[(String, Long)] = messageDStream.map(message => {
      // 2019010110:10:10000 1L
      // 2019010110:10  1L
      // 2019010110 1L
      // 20190101 1L
      val date = new SimpleDateFormat("yyyy-MM-dd").format(new Date(message.timestamp.toLong))
      val key = date + ":" + message.area + ":" + message.city + ":" + message.adid
      (key, 1L)
    })

    // 第一种实现方式 , 推荐使用
    /*
    val dateAreaCityAdsReduceDStream: DStream[(String, Long)] = dateAreaCityAdsDStream.reduceByKey(_+_)
    dateAreaCityAdsReduceDStream.foreachRDD(rdd=>{
        rdd.foreachPartition(datas=>{
            val jedisClient: Jedis = RedisUtil.getJedisClient
            for ((key, sum) <- datas) {
                jedisClient.hincrBy("date:area:city:ads", key, sum)
            }
            jedisClient.close()
        })
    })
    */

    // 第二种实现方式
    streamingContext.sparkContext.setCheckpointDir("cp")
    // 4.2 将拆分数据进行聚合统计（date:area:city:ads， sum）
    val totalClickDStream: DStream[(String, Long)] = dateAreaCityAdsDStream.updateStateByKey {
      case (seq, cache) => {
        val sum = cache.getOrElse(0L) + seq.sum
        Option(sum)
      }
    }
    // 4.3 将聚合的结果保存到检查点（有状态）中, 保存到Redis中
    totalClickDStream.foreachRDD(rdd => {
      rdd.foreachPartition(datas => {
        val jedisClient: Jedis = RedisUtil.getJedisClient
        for ((key, sum) <- datas) {
          jedisClient.hset("date:area:city:ads", key, sum.toString)
        }
        jedisClient.close()
      })
    })

    streamingContext.start()
    streamingContext.awaitTermination()
  }
}