//package 项目二实时项目.gmall2020-realtime.src.main.scala.xuexi.dim

import com.alibaba.fastjson.JSON
import xuexi.bean.ProvinceInfo
import xuexi.utils.{MyKafkaUtil, OffsetManagerUtil}
import org.apache.hadoop.conf.Configuration
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.common.TopicPartition
import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import org.apache.spark.streaming.dstream.{DStream, InputDStream}
import org.apache.spark.streaming.kafka010.{HasOffsetRanges, OffsetRange}
import org.apache.spark.streaming.{Seconds, StreamingContext}

/**
  * Author: lizhong.liu
  * Date: 2020/9/18
  * Desc: 从Kafka主题中读取省份维度数据保存到Hbase中
  */
object ProvinceInfoApp {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setAppName("BaseDBCanalApp").setMaster("local[4]")
    val ssc: StreamingContext = new StreamingContext(conf,Seconds(5))

    var topic = "ods_base_province"
    var groupId = "gmall_province_group"
    //==========1.从kafka中读取数据==========
    //1.1 从Redis中获取偏移量
    val offsetMap: Map[TopicPartition, Long] = OffsetManagerUtil.getOffset(topic,groupId)

    //1.2 根据偏移量从Kafka中消费数据
    var recordDStream: InputDStream[ConsumerRecord[String, String]] = null
    if(offsetMap!=null && offsetMap.size > 0 ){
      recordDStream = MyKafkaUtil.getKafkaStream(topic,ssc,offsetMap,groupId)
    }else{
      recordDStream = MyKafkaUtil.getKafkaStream(topic,ssc,groupId)
    }

    //1.3 获取当前批次中数据的偏移量
    var offsetRangs: Array[OffsetRange] = Array.empty[OffsetRange]
    val offsetDStream: DStream[ConsumerRecord[String, String]] = recordDStream.transform {
      rdd => {
        offsetRangs = rdd.asInstanceOf[HasOffsetRanges].offsetRanges
        rdd
      }
    }

    //==========2.将数据保存到Hbase表中==========
    import org.apache.phoenix.spark._
    offsetDStream.foreachRDD{
      rdd=>{
        val provinceInfoRDD = rdd.map {
          record => {
            val jsonStr: String = record.value()
            val provinceInfo: ProvinceInfo = JSON.parseObject(jsonStr, classOf[ProvinceInfo])
            provinceInfo
          }
        }
        provinceInfoRDD.saveToPhoenix(
          "GMALL0421_PROVINCE_INFO",
          Seq("ID","NAME","AREA_CODE","ISO_CODE"),
          new Configuration,
          Some("hadoop202,hadoop203,hadoop204:2181")
        )

        //修改偏移量
        OffsetManagerUtil.saveOffset(topic,groupId,offsetRangs)
      }
    }


    ssc.start()
    ssc.awaitTermination()
  }
}
