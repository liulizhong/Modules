//package 项目二实时项目.gmall2020-realtime.src.main.scala.xuexi.ods

import com.alibaba.fastjson.{JSON, JSONArray, JSONObject}
import xuexi.utils.{MyKafkaSink, MyKafkaUtil, OffsetManagerUtil}
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.common.TopicPartition
import org.apache.spark.SparkConf
import org.apache.spark.streaming.dstream.{DStream, InputDStream}
import org.apache.spark.streaming.kafka010.{HasOffsetRanges, OffsetRange}
import org.apache.spark.streaming.{Seconds, StreamingContext}

/**
  * Author: lizhong.liu
  * Date: 2020/9/16
  * Desc: Kafka 精准消费一次
  */
object BaseDBCanalApp {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setAppName("BaseDBCanalApp").setMaster("local[4]")
    val ssc: StreamingContext = new StreamingContext(conf,Seconds(5))
    var topic = "gmall0421_db_c"
    var groupId = "base_db_canal_group"

    //从Redis中获取偏移量
    val offsetMap: Map[TopicPartition, Long] = OffsetManagerUtil.getOffset(topic,groupId)
    var recordDStream: InputDStream[ConsumerRecord[String, String]] = null
    if(offsetMap!=null &&offsetMap.size > 0 ){
      recordDStream = MyKafkaUtil.getKafkaStream(topic,ssc,offsetMap,groupId)
    }else{
      recordDStream = MyKafkaUtil.getKafkaStream(topic,ssc,groupId)
    }

    //获取当前批次处理的数据对应的分区以及偏移量
    var offsetRanges: Array[OffsetRange] = Array.empty[OffsetRange]
    val offsetDStream: DStream[ConsumerRecord[String, String]] = recordDStream.transform {
      rdd => {
        offsetRanges = rdd.asInstanceOf[HasOffsetRanges].offsetRanges
        rdd
      }
    }
    //对读取到的kafka数据进行转换，只保留消息部分，并转换为json类型对象进行处理
    val jsonObjDStream: DStream[JSONObject] = offsetDStream.map {
      record => {
        //获取消息的json字符串
        val jsonStr: String = record.value()
        //将json字符串转换为json对象
        val jsonObj: JSONObject = JSON.parseObject(jsonStr)
        jsonObj
      }
    }

    //jsonObjDStream.print(1000)
    //根据表名，将不同表数据发送到kafka的不同topic中去
    jsonObjDStream.foreachRDD{
      rdd=>{
        rdd.foreach{
          jsonObj=>{
            //获取表名
            val tableName: String = jsonObj.getString("table")
            //获取当前表中变动的数据
            val dataArr: JSONArray = jsonObj.getJSONArray("data")
            val opType = jsonObj.getString("type")
            if("INSERT".equals(opType)){
              //将java数组转换为json类型数据
              import scala.collection.JavaConverters._
              //根据表名，拼接目标topic名字
              var sendTopic:String = "ods_" + tableName
              for (data <- dataArr.asScala) {
                MyKafkaSink.send(sendTopic,data.toString)
              }
            }
          }
        }
        //保存偏移量到Redis中
        OffsetManagerUtil.saveOffset(topic,groupId,offsetRanges)
      }
    }

    ssc.start()
    ssc.awaitTermination()
  }
}
