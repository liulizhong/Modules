//package 项目二实时项目.gmall2020-realtime.src.main.scala.xuexi.dim

import java.text.SimpleDateFormat
import java.util.Date

import com.alibaba.fastjson.JSON
import xuexi.bean.{ProvinceInfo, UserInfo}
import xuexi.utils.{MyKafkaUtil, OffsetManagerUtil}
import org.apache.hadoop.conf.Configuration
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.common.TopicPartition
import org.apache.spark.SparkConf
import org.apache.spark.streaming.dstream.{DStream, InputDStream}
import org.apache.spark.streaming.kafka010.{HasOffsetRanges, OffsetRange}
import org.apache.spark.streaming.{Seconds, StreamingContext}

/**
  * Author: lizhong.liu
  * Date: 2020/9/18
  * Desc: 从Kafka主题中读取用户维度数据保存到Hbase中
  */
object UserInfoApp {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setAppName("BaseDBCanalApp").setMaster("local[4]")
    val ssc: StreamingContext = new StreamingContext(conf,Seconds(5))

    var topic = "ods_user_info"
    var groupId = "gmall_user_info_group"
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

    //==========2.对获取到的数据进行结构的转换，并给属性赋值==========
    val mapDStream: DStream[UserInfo] = offsetDStream.map {
      record => {
        val jsonStr: String = record.value()
        //将json格式字符串转换为对象
        val userInfo: UserInfo = JSON.parseObject(jsonStr, classOf[UserInfo])

        val formattor = new SimpleDateFormat("yyyy-MM-dd")
        val date: Date = formattor.parse(userInfo.birthday)
        val curTs: Long = System.currentTimeMillis()
        val betweenMs = curTs - date.getTime
        val age = betweenMs / 1000L / 60L / 60L / 24L / 365L
        if (age < 20) {
          userInfo.age_group = "20岁及以下"
        } else if (age > 30) {
          userInfo.age_group = "30岁以上"
        } else {
          userInfo.age_group = "21岁到30岁"
        }

        if (userInfo.gender == "M") {
          userInfo.gender_name = "男"
        } else {
          userInfo.gender_name = "女"
        }
        userInfo
      }
    }

    //将数据保存到Hbase中
    import org.apache.phoenix.spark._
    mapDStream.foreachRDD{
      rdd=>{
        rdd.saveToPhoenix(
          "GMALL0421_USER_INFO",
          Seq("ID","USER_LEVEL","BIRTHDAY","GENDER","AGE_GROUP","GENDER_NAME"),
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
