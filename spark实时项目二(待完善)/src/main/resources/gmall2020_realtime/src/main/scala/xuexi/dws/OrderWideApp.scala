//package 项目二实时项目.gmall2020-realtime.src.main.scala.xuexi.dws

import java.lang
import java.util.Properties

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.serializer.SerializeConfig
import xuexi.bean.{OrderDetail, OrderInfo, OrderWide}
import xuexi.utils.{MyKafkaSink, MyKafkaUtil, MyRedisUtil, OffsetManagerUtil}
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.common.TopicPartition
import org.apache.spark.SparkConf
import org.apache.spark.sql.{DataFrame, SaveMode, SparkSession}
import org.apache.spark.streaming.dstream.{DStream, InputDStream}
import org.apache.spark.streaming.kafka010.{HasOffsetRanges, OffsetRange}
import org.apache.spark.streaming.{Seconds, StreamingContext}
import redis.clients.jedis.Jedis

import scala.collection.mutable.ListBuffer

/**
  * Author: lizhong.liu
  * Date: 2020/9/19
  * Desc:  订单和订单明细双流处理
  */
object OrderWideApp {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setAppName("OrderWideApp").setMaster("local[4]")
    val ssc: StreamingContext = new StreamingContext(conf,Seconds(5))

    var orderTopic = "dwd_order_info"
    var orderGroupId = "dwd_order_info_group"

    var orderDetailTopic = "dwd_order_detail"
    var orderDetailGroupId = "dwd_order_detail_group"

    //1.从Redis获取偏移量
    val orderOffsetMap: Map[TopicPartition, Long] = OffsetManagerUtil.getOffset(orderTopic,orderGroupId)

    val orderDetailOffsetMap: Map[TopicPartition, Long] = OffsetManagerUtil.getOffset(orderDetailTopic,orderDetailGroupId)

    //2.根据偏移量读取Kafka数据
    var orderRecordDStream: InputDStream[ConsumerRecord[String, String]] = null
    if(orderOffsetMap!=null && orderOffsetMap.size > 0){
      orderRecordDStream = MyKafkaUtil.getKafkaStream(orderTopic,ssc,orderOffsetMap,orderGroupId)
    }else{
      orderRecordDStream = MyKafkaUtil.getKafkaStream(orderTopic,ssc,orderGroupId)
    }

    var orderDetailRecordDStream: InputDStream[ConsumerRecord[String, String]] = null
    if(orderDetailOffsetMap!=null && orderDetailOffsetMap.size > 0){
      orderDetailRecordDStream = MyKafkaUtil.getKafkaStream(orderDetailTopic,ssc,orderDetailOffsetMap,orderDetailGroupId)
    }else{
      orderDetailRecordDStream = MyKafkaUtil.getKafkaStream(orderDetailTopic,ssc,orderDetailGroupId)
    }

    //3.获取本批次处理的数据的偏移量
    var orderOffsetRanges: Array[OffsetRange] = Array.empty[OffsetRange]
    val orderOffsetDStream: DStream[ConsumerRecord[String, String]] = orderRecordDStream.transform {
      rdd => {
        orderOffsetRanges = rdd.asInstanceOf[HasOffsetRanges].offsetRanges
        rdd
      }
    }

    var orderDetilOffsetRanges: Array[OffsetRange] = Array.empty[OffsetRange]
    val orderDetailOffsetDStream: DStream[ConsumerRecord[String, String]] = orderDetailRecordDStream.transform {
      rdd => {
        orderDetilOffsetRanges = rdd.asInstanceOf[HasOffsetRanges].offsetRanges
        rdd
      }
    }

    //对结构进行转换，将json转换为样例类
    val orderInfoDStream: DStream[OrderInfo] = orderOffsetDStream.map {
      orderRecord => {
        val jsonStr: String = orderRecord.value()
        val orderInfo: OrderInfo = JSON.parseObject(jsonStr, classOf[OrderInfo])
        orderInfo
      }
    }

    val orderDetailDStream: DStream[OrderDetail] = orderDetailOffsetDStream.map {
      orderDetailRecord => {
        val jsonStr: String = orderDetailRecord.value()
        val orderDetail: OrderDetail = JSON.parseObject(jsonStr, classOf[OrderDetail])
        orderDetail
      }
    }
    //orderInfoDStream.print(1000)
    //orderDetailDStream.print(1000)

    /*
    这种join方式存在问题：无法保证所有订单和订单明细都在同一个采集周期内
    val orderInfoWithKeyDStream: DStream[(Long, OrderInfo)] = orderInfoDStream.map {
      orderInfo => {
        (orderInfo.id, orderInfo)
      }
    }

    val orderDetailWithKeyDStream: DStream[(Long, OrderDetail)] = orderDetailDStream.map {
      orderDetail => {
        (orderDetail.order_id, orderDetail)
      }
    }
    orderInfoWithKeyDStream.join(orderDetailWithKeyDStream)
    */

    //双流join    滑窗  + 去重
    val orderInfoWithWindowDStream: DStream[OrderInfo] = orderInfoDStream.window(Seconds(20),Seconds(5))

    val orderDetailWithWindowDStream: DStream[OrderDetail] = orderDetailDStream.window(Seconds(20),Seconds(5))

    //转换为kv结构  进行join
    val orderInfoWithKeyDStream: DStream[(Long, OrderInfo)] = orderInfoWithWindowDStream.map {
      orderInfo => {
        (orderInfo.id, orderInfo)
      }
    }
    val orderDetailWithKeyDSteam: DStream[(Long, OrderDetail)] = orderDetailWithWindowDStream.map {
      orderDetail => {
        (orderDetail.order_id, orderDetail)
      }
    }
    val joinedDStream: DStream[(Long, (OrderInfo, OrderDetail))] = orderInfoWithKeyDStream.join(orderDetailWithKeyDSteam)

    //去重    Redis   type:set  key:order_join:[orderId]    value:orderDetailId expire 600
    val orderWideDStream: DStream[OrderWide] = joinedDStream.mapPartitions {
      tupleItr => {
        //获取jedis客户端
        val jedis: Jedis = MyRedisUtil.getJedisClient()
        val orderWideList = new ListBuffer[OrderWide]
        for ((orderId, (orderInfo, orderDetail)) <- tupleItr) {
          var redisKey = "order_join:" + orderId
          // 向Redis中放数据
          val ifNotExist: lang.Long = jedis.sadd(redisKey, orderDetail.id.toString)
          jedis.expire(redisKey, 600)
          if (ifNotExist == 1L) {
            // 将数据封装为OrderWide
            orderWideList.append(new OrderWide(orderInfo, orderDetail))
          }
        }
        jedis.close()
        orderWideList.toIterator
      }
    }

    // 计算实付分摊
    /*
    处理过的明细的实际总价累加:Redis	type:string		key:order_origin_sum:orderId value:累加结果		expire:600
    处理过的明细分摊累加:Redis	type:string		key:order_split_sum:orderId value:累加结果		expire:600

    ?如何判断是最后一笔
      order_price*sku_num  == original_total_amount - Σ处理过的明细的实际总价累加(order_price * sku_num)

    // 计算商品的实付分摊金额
    if(是最后一笔明细){
      final_detail_amount =  final_total_amount - Σ处理过的明细分摊累加
    }else{
      final_detail_amount =  final_total_amount * (order_price * sku_num)/original_total_amount
    }
    */
    val orderWideSplitDStream: DStream[OrderWide] = orderWideDStream.mapPartitions {
      orderWideItr => {
        val jedis: Jedis = MyRedisUtil.getJedisClient()
        val orderWideList: List[OrderWide] = orderWideItr.toList
        for (orderWide <- orderWideList) {
          // 处理过的明细的实际总价累加  order_origin_sum:orderId
          var orderOriginSumKey = "order_origin_sum:" + orderWide.order_id
          val orderOriginSumStr: String = jedis.get(orderOriginSumKey)
          var orderOriginSum = 0D
          if (orderOriginSumStr != null && orderOriginSumStr.size != 0) {
            orderOriginSum = orderOriginSumStr.toDouble
          }

          // 处理过的明细分摊累加key:order_split_sum:orderId
          var orderSplitSumKey = "order_split_sum:" + orderWide.order_id
          val orderSplitSumStr: String = jedis.get(orderSplitSumKey)
          var orderSplitSum = 0D
          if (orderSplitSumStr != null && orderSplitSumStr.size != 0) {
            orderSplitSum = orderSplitSumStr.toDouble
          }

          val detailAmount: Double = orderWide.sku_price * orderWide.sku_num
          // 判断是否为最后一笔明细
          if (detailAmount == orderWide.original_total_amount - orderOriginSum) {
            // 是最后一笔明细
            orderWide.final_detail_amount = Math.round((orderWide.final_total_amount - orderSplitSum) * 100D) / 100D
          } else {
            // 不是最后一笔明细
            orderWide.final_detail_amount = Math.round((orderWide.final_total_amount * detailAmount / orderWide.original_total_amount) * 100D) / 100D
          }

          // 每处理一条明细将处理的结果保存到Redis
          var newOrderOriginSum = orderOriginSum + detailAmount
          jedis.setex(orderOriginSumKey, 600, newOrderOriginSum.toString)

          var newOrderSplitSum = orderWide.final_detail_amount + orderSplitSum
          jedis.setex(orderSplitSumKey, 600, newOrderSplitSum.toString)
        }
        jedis.close()
        orderWideList.toIterator
      }
    }

    /*
    orderWideSplitDStream.map{
      orderWide=>{
        JSON.toJSONString(orderWide,new SerializeConfig(true))
      }
    }.print(1000)
  */

    // 将合并之后的宽表数据保存到ClickHouse中
    val sparkSession: SparkSession = SparkSession.builder().appName("order_wide_spark_session").getOrCreate()
    import sparkSession.implicits._
    orderWideSplitDStream.foreachRDD{
      rdd=>{
        rdd.cache()

        val df: DataFrame = rdd.toDF
        df.write.mode(SaveMode.Append)
          .option("batchsize", "100")
          .option("isolationLevel", "NONE") // 设置事务
          .option("numPartitions", "4") // 设置并发分区数
          .option("driver","ru.yandex.clickhouse.ClickHouseDriver")
          .jdbc("jdbc:clickhouse://hadoop202:8123/default","t_order_wide_0421",new Properties())

        // 将数据写回到Kafka
        rdd.foreach{orderWide=>
          MyKafkaSink.send("dws_order_wide",  JSON.toJSONString(orderWide,new SerializeConfig(true)))
        }

        // 修改偏移量
        OffsetManagerUtil.saveOffset(orderTopic,orderGroupId,orderOffsetRanges)
        OffsetManagerUtil.saveOffset(orderDetailTopic,orderDetailGroupId,orderDetilOffsetRanges)
      }
    }

    ssc.start()
    ssc.awaitTermination()
  }
}
