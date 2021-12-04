//package 项目二实时项目.gmall2020-realtime.src.main.scala.xuexi.utils

import java.util

import org.apache.kafka.common.TopicPartition
import org.apache.spark.streaming.kafka010.OffsetRange
import redis.clients.jedis.Jedis

/**
  * Author: lizhong.liu
  * Date: 2020/9/14
  * Desc: 手动维护偏移量的工具类
  */
object OffsetManagerUtil {
  //从Redis中获取偏移量
  def getOffset(topicName:String,groupId:String): Map[TopicPartition,Long] ={
    //获取Jedis
    val jedis: Jedis = MyRedisUtil.getJedisClient()
    //拼接Redis中存储偏移量的key
    var offsetKey = "offset:" + topicName + ":" + groupId
    //从Redis中获取偏移量的值
    val offsetMap: util.Map[String, String] = jedis.hgetAll(offsetKey)
    //关闭Jedis
    jedis.close()
    //对获取到的偏移量map进行遍历
    import scala.collection.JavaConverters._
    offsetMap.asScala.map {
      case (partitionId, offset) => {
        println("读取分区偏移量：" + partitionId + ":" + offset)
        (new TopicPartition(topicName, partitionId.toInt), offset.toLong)
      }
    }.toMap
  }
  //向Redis中保存偏移量
  def saveOffset(topicName: String, groupId: String, offsetRanges: Array[OffsetRange]): Unit = {
    //定义一个Java类型的Map,用于存放分区和偏移量信息
    val offsetMap = new util.HashMap[String,String]()
    //对OffsetRanges数据进行遍历
    for (offsestRange <- offsetRanges) {
      //获取分区编号
      val partitionId = offsestRange.partition
      //获取结束偏移量
      val untilOffset = offsestRange.untilOffset
      //将分区和偏移量封装放到map集合中
      offsetMap.put(partitionId.toString,untilOffset.toString)
      //打印测试
      println("保存分区:" + partitionId +":" + offsestRange.fromOffset+"--->" + offsestRange.untilOffset)
    }
    //拼接Redis中存储偏移量的key
    var offsetKey = "offset:" + topicName + ":" + groupId
    //获取Jedis
    val jedis: Jedis = MyRedisUtil.getJedisClient()
    jedis.hmset(offsetKey,offsetMap)
    //关闭连接
    jedis.close()
  }
}