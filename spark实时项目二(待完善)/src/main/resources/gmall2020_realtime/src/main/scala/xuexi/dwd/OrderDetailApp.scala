//package 项目二实时项目.gmall2020-realtime.src.main.scala.xuexi.dwd

import com.alibaba.fastjson.serializer.SerializeConfig
import com.alibaba.fastjson.{JSON, JSONObject}
import xuexi.bean.{OrderDetail, SkuInfo}
import xuexi.utils.{MyKafkaSink, MyKafkaUtil, OffsetManagerUtil, PhoenixUtil}
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.common.TopicPartition
import org.apache.spark.SparkConf
import org.apache.spark.streaming.dstream.{DStream, InputDStream}
import org.apache.spark.streaming.kafka010.{HasOffsetRanges, OffsetRange}
import org.apache.spark.streaming.{Seconds, StreamingContext}

/**
  * Author: lizhong.liu
  * Date: 2020/9/19
  * Desc: 从Kafka中读取订单明细数据
  */
object OrderDetailApp {

  def main(args: Array[String]): Unit = {

    // 加载流 //手动偏移量
    val sparkConf: SparkConf = new SparkConf().setMaster("local[4]").setAppName("OrderDetailApp")
    val ssc = new StreamingContext(sparkConf, Seconds(5))
    val topic = "ods_order_detail";
    val groupId = "order_detail_group"

    //从redis中读取偏移量
    val offsetMapForKafka: Map[TopicPartition, Long] = OffsetManagerUtil.getOffset(topic, groupId)

    //通过偏移量到Kafka中获取数据
    var recordInputDstream: InputDStream[ConsumerRecord[String, String]] = null
    if (offsetMapForKafka != null && offsetMapForKafka.size > 0) {
      recordInputDstream = MyKafkaUtil.getKafkaStream(topic, ssc, offsetMapForKafka, groupId)
    } else {
      recordInputDstream = MyKafkaUtil.getKafkaStream(topic, ssc, groupId)
    }

    //从流中获得本批次的 偏移量结束点（每批次执行一次）
    var offsetRanges: Array[OffsetRange] = null //周期性储存了当前批次偏移量的变化状态，重要的是偏移量结束点
    val inputGetOffsetDstream: DStream[ConsumerRecord[String, String]] = recordInputDstream.transform {
      rdd => {
        //周期性在driver中执行
        offsetRanges = rdd.asInstanceOf[HasOffsetRanges].offsetRanges
        rdd
      }
    }

    //提取数据
    val orderDetailDstream: DStream[OrderDetail] = inputGetOffsetDstream.map {
      record =>{
        val jsonString: String = record.value()
        //订单处理  转换成更方便操作的专用样例类
        val orderDetail: OrderDetail = JSON.parseObject(jsonString, classOf[OrderDetail])
        orderDetail
      }
    }

    //orderDetailDstream.print(1000)
    //和Hbase中的商品维度表进行关联
    val orderDetailWithSkuDStream: DStream[OrderDetail] = orderDetailDstream.mapPartitions {
      orderDetailItr => {
        val orderDetailList: List[OrderDetail] = orderDetailItr.toList
        //获取当前分区商品明细中所有的商品id
        val skuIdList: List[Long] = orderDetailList.map(_.sku_id)
        //根据商品id到Phoenix中查询所有商品对象
        val sql = s"select id ,tm_id,spu_id,category3_id,tm_name ," +
          s"spu_name,category3_name  from gmall0421_sku_info  where id in ('${skuIdList.mkString("','")}')"
        val skuInfoJsonList: List[JSONObject] = PhoenixUtil.queryList(sql)
        val skuInfoMap: Map[String, SkuInfo] = skuInfoJsonList.map {
          skuInfoJsonObj => {
            val skuInfo: SkuInfo = JSON.parseObject(skuInfoJsonObj.toString(), classOf[SkuInfo])
            (skuInfo.id, skuInfo)
          }
        }.toMap

        for (orderDetail <- orderDetailList) {
          val skuInfo: SkuInfo = skuInfoMap.getOrElse(orderDetail.sku_id.toString, null)
          if (skuInfo != null) {
            orderDetail.spu_id = skuInfo.spu_id.toLong
            orderDetail.tm_id = skuInfo.tm_id.toLong
            orderDetail.category3_id = skuInfo.category3_id.toLong
            orderDetail.spu_name = skuInfo.spu_name
            orderDetail.tm_name = skuInfo.tm_name
            orderDetail.category3_name = skuInfo.category3_name
          }
        }
        orderDetailList.toIterator
      }
    }
    //orderDetailWithSkuDStream.print(1000)
    //将和维度关联后的订单明细宽表写到Kafka(DWD)
    orderDetailWithSkuDStream.foreachRDD{
      rdd=>{
        rdd.foreach{
          orderDetail=>{
            val msg: String = JSON.toJSONString(orderDetail,new SerializeConfig(true))
            MyKafkaSink.send("dwd_order_detail",msg)
          }
        }

        //修改偏移量
        OffsetManagerUtil.saveOffset(topic,groupId,offsetRanges)
      }
    }

    ssc.start()
    ssc.awaitTermination()
  }
}
