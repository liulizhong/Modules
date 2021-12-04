//package 项目二实时项目.gmall2020-realtime.src.main.scala.xuexi.utils

import com.alibaba.fastjson.JSONObject
import org.apache.kafka.common.TopicPartition

/**
  * Author: lizhong.liu
  * Date: 2020/9/23
  * Desc: 维护保存在MySQL中的偏移量
  */
object OffsetManagerM {
  /**
    * 从Mysql中读取偏移量
    * @param consumerGroupId
    * @param topic
    * @return
    */
  def getOffset(topic: String, consumerGroupId: String): Map[TopicPartition, Long] = {
    val sql=" select group_id,topic,topic_offset,partition_id from offset_0421 " +
      " where topic='"+topic+"' and group_id='"+consumerGroupId+"'"

    //{"topic":XX,"group_id":xxx,"partition_id":xx,"topic_offset"}
    val jsonObjList: List[JSONObject] = MySQLUtil.queryList(sql)

    val topicPartitionList: List[(TopicPartition, Long)] = jsonObjList.map {
      jsonObj =>{
        val topicPartition: TopicPartition = new TopicPartition(topic, jsonObj.getIntValue("partition_id"))
        val offset: Long = jsonObj.getLongValue("topic_offset")
        (topicPartition, offset)
      }
    }
    val topicPartitionMap: Map[TopicPartition, Long] = topicPartitionList.toMap
    topicPartitionMap
  }

}
