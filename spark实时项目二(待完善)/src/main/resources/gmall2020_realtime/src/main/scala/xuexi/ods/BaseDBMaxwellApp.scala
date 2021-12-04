//package 项目二实时项目.gmall2020-realtime.src.main.scala.xuexi.ods

import com.alibaba.fastjson.{JSON, JSONObject}
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
  * Desc: 基于Maxwell的ODS层处理
  */
object BaseDBMaxwellApp {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setAppName("BaseDBCanalApp").setMaster("local[4]")
    val ssc: StreamingContext = new StreamingContext(conf,Seconds(5))
    var topic = "gmall0421_db_m"
    var groupId = "base_db_maxwell_group"

    //1.从Redis获取偏移量
    val offsetMap: Map[TopicPartition, Long] = OffsetManagerUtil.getOffset(topic,groupId)
    //2.根据偏移量读取Kafka数据
    var recordDStream: InputDStream[ConsumerRecord[String, String]] = null
    if(offsetMap!=null && offsetMap.size > 0){
      recordDStream = MyKafkaUtil.getKafkaStream(topic,ssc,offsetMap,groupId)
    }else{
      recordDStream = MyKafkaUtil.getKafkaStream(topic,ssc,groupId)
    }

    //3.获取本批次处理的数据的偏移量
    var offsetRanges: Array[OffsetRange] = Array.empty[OffsetRange]
    val offsetDStream: DStream[ConsumerRecord[String, String]] = recordDStream.transform {
      rdd => {
        offsetRanges = rdd.asInstanceOf[HasOffsetRanges].offsetRanges
        rdd
      }
    }

    //4.对读取到的Kafka数据进行结构的转换，保留value部分，转换为json对象
    val jsonObjDStream = offsetDStream.map {
      record => {
        //4.1 获取json格式字符串
        val jsonStr: String = record.value()
        //4.2 将json格式字符串转换为json对象
        val jsonObj: JSONObject = JSON.parseObject(jsonStr)
        jsonObj
      }
    }
    //5.根据不同表名，将数据分别发送到不同的kafka主题中
    jsonObjDStream.foreachRDD{
      rdd=>{
        rdd.foreach{
          jsonObj=>{
            //5.0 获取类型
            val opType = jsonObj.getString("type")
            val dataObj = jsonObj.getJSONObject("data")
            //5.1 获取表名
            val tableName: String = jsonObj.getString("table")
            if(dataObj!= null && !dataObj.isEmpty){
              if(
                (tableName.equals("order_info")&&"insert".equals(opType))
                  || (tableName.equals("order_detail") && "insert".equals(opType))
                  ||  tableName.equals("base_province")
                  ||  tableName.equals("user_info")
                  ||  tableName.equals("sku_info")
                  ||  tableName.equals("base_trademark")
                  ||  tableName.equals("base_category3")
                  ||  tableName.equals("spu_info")
              ){
                //5.3 拼接发送到kafka的主题名
                var sendTopic :String = "ods_" + tableName
                //5.4 发送消息到kafka
                MyKafkaSink.send(sendTopic,dataObj.toString)
              }
            }

          }
        }
        //6.保存偏移量到Redis中
        OffsetManagerUtil.saveOffset(topic,groupId,offsetRanges)
      }
    }

    ssc.start()
    ssc.awaitTermination()
  }
}
