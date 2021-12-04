package project.online

import java.text.SimpleDateFormat
import java.util.Date
import project.xbean.KafkaMessage
import project.xutil.{MyKafkaUtil, RedisUtil}
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.spark.SparkConf
import org.apache.spark.streaming.dstream.{DStream, InputDStream}
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.json4s.jackson.JsonMethods
import redis.clients.jedis.Jedis

// 每天各地区 top3 热门广告
object Three_DateAreaAdvCountTop3Application {
    def main(args: Array[String]): Unit = {
        // 【1】、接收Kafka数据，并封装为DStream
        val topic = "ads_log180925";
        val sparkConf = new SparkConf().setMaster("local[*]").setAppName("DateAreaAdvCountTop3Application")
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

        streamingContext.sparkContext.setCheckpointDir("cp")
        val totalClickDStream: DStream[(String, Long)] = dateAreaCityAdsDStream.updateStateByKey {
            case (seq, cache) => {
                val sum = cache.getOrElse(0L) + seq.sum
                Option(sum)
            }
        }

        totalClickDStream.foreachRDD(rdd=>{
            rdd.foreachPartition(datas=>{
                val jedisClient: Jedis = RedisUtil.getJedisClient
                for ((key, sum) <- datas) {
                    jedisClient.hset("date:area:city:ads", key, sum.toString)
                }
                jedisClient.close()
            })
        })

        // 需求六
        // 4.1 从需求五获取统计结果（ date:area:city:ads, sum ）
        // 4.2 将数据进行结构转换（date:area:ads, sum1），（date:area:ads, sum2）
        val dateAreaAdsSumDStream: DStream[(String, Long)] = totalClickDStream.map {
            case (key, sum) => {
                val ks = key.split(":")
                val newKey = ks(0) + ":" + ks(1) + ":" + ks(3)
                (newKey, sum)
            }
        }
        // 4.3 将转换后的数据进行聚合统计（date:area:ads, sumTotal）
        val dateAreaAdsReduceSumDStream: DStream[(String, Long)] = dateAreaAdsSumDStream.reduceByKey(_+_)
        // 4.4 转换结构：（（date:area），（ads1, sumTotal）），（（date:area），（ads2, sumTotal））
        val dateAreaToAdsSumDStream: DStream[(String, (String, Long))] = dateAreaAdsReduceSumDStream.map {
            case (key, sum) => {
                val ks = key.split(":")
                val newKey = ks(0) + ":" + ks(1)
                val newVal = (ks(2), sum)
                (newKey, newVal)
            }
        }

        // 4.4.1 分组
        val dateAreaGroupAdsSumDStream: DStream[(String, Iterable[(String, Long)])] = dateAreaToAdsSumDStream.groupByKey()

        // 4.5 将转换后的结构进行排序（降序）获取前三条
        // List[(String, Long)] => json => "[{},{},{}]"
        // Map[(K,V)]           => json => "{'xxx':10}"
        val resultDStream: DStream[(String, List[(String, Long)])] = dateAreaGroupAdsSumDStream.mapValues(datas => {
            datas.toList.sortWith {
                case (left, right) => {
                    left._2 > right._2
                }
            }.take(3)
        })

        val resultMapDStream: DStream[(String, Map[String, Long])] = resultDStream.map {
            case (key, list) => {
                (key, list.toMap)
            }
        }

        import org.json4s.JsonDSL._

        // 4.6 将最终的数据保存到Redis中
        resultMapDStream.foreachRDD(rdd=>{
            rdd.foreachPartition(datas=>{
                val jedisClient: Jedis = RedisUtil.getJedisClient
                for ((key, map) <- datas) {
                    val ks = key.split(":")
                    // map ==> json String
                    val value = JsonMethods.compact(JsonMethods.render(map))
                    jedisClient.hset("top3_ads_per_day:"+ks(0), ks(1), value)
                }
                jedisClient.close()
            })
        })

        streamingContext.start()
        streamingContext.awaitTermination()
    }
}
