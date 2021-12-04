//package 项目二实时项目.gmall2020-realtime.src.main.scala.xuexi.dwd

import java.text.SimpleDateFormat
import java.util.Date

import com.alibaba.fastjson.serializer.SerializeConfig
import com.alibaba.fastjson.{JSON, JSONObject}
import xuexi.bean.{OrderInfo, ProvinceInfo, UserInfo, UserStatus}
import xuexi.utils._
import org.apache.hadoop.conf.Configuration
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.common.TopicPartition
import org.apache.spark.SparkConf
import org.apache.spark.broadcast.Broadcast
import org.apache.spark.rdd.RDD
import org.apache.spark.streaming.dstream.{DStream, InputDStream}
import org.apache.spark.streaming.kafka010.{HasOffsetRanges, OffsetRange}
import org.apache.spark.streaming.{Seconds, StreamingContext}

/**
  * Author: lizhong.liu
  * Date: 2020/9/16
  * Desc:  从订单主题中，查询数据，判断是否为首单
  */
object OrderInfoApp {
  def main(args: Array[String]): Unit = {
    val sparkConf: SparkConf = new SparkConf().setMaster("local[4]").setAppName("OrderInfoApp")
    val ssc = new StreamingContext(sparkConf, Seconds(5))
    val topic = "ods_order_info"
    val groupId = "order_info_group"


    //============1.读取kafka中数据=============
    //1.1 从Redis中获取偏移量
    val offsetMap: Map[TopicPartition, Long] = OffsetManagerUtil.getOffset(topic, groupId)

    //1.2 根据偏移量从Kafka中消费数据
    var recordDStream: InputDStream[ConsumerRecord[String, String]] = null
    if (offsetMap != null && offsetMap.size > 0) {
      recordDStream = MyKafkaUtil.getKafkaStream(topic, ssc, offsetMap, groupId)
    } else {
      recordDStream = MyKafkaUtil.getKafkaStream(topic, ssc, groupId)
    }

    //1.3 获取当前批次中数据的偏移量
    var offsetRangs: Array[OffsetRange] = Array.empty[OffsetRange]
    val offsetDStream: DStream[ConsumerRecord[String, String]] = recordDStream.transform {
      rdd => {
        offsetRangs = rdd.asInstanceOf[HasOffsetRanges].offsetRanges
        rdd
      }
    }

    //1.4.对从Kafka中读取到的数据进行结构的转换，转换为OrderInfo
    val orderInfoDStream: DStream[OrderInfo] = offsetDStream.map {
      record => {
        //获取订单的json格式字符串
        val jsonStr: String = record.value()
        //将json字符串类型数据转换为OrderInfo类型
        val orderInfo: OrderInfo = JSON.parseObject(jsonStr, classOf[OrderInfo])

        //获取创建时间  2020-09-16 14:37:34
        val createTime: String = orderInfo.create_time
        //对创建时间进行字符串的切割
        val createTimeArr: Array[String] = createTime.split(" ")
        //给OrderInfo对象的日期和小时属性赋值
        orderInfo.create_date = createTimeArr(0)
        orderInfo.create_hour = createTimeArr(1).split(":")(0)
        orderInfo
      }
    }

    //=============2.判断是否首单================
    //2.1 判断是否首单方案1 每条订单都会执行一次SQL，SQL执行过于频繁
    /*val orderInfoWithFirstFlagDStream: DStream[OrderInfo] = orderInfoDStream.map {
      orderInfo => {
        //根据当前订单中用户id，到Hbase中查询用户的状态
        var sql: String = s"select user_id,if_consumed from user_status0421 where user_id='${orderInfo.user_id}'"
        val userStatusList: List[JSONObject] = PhoenixUtil.queryList(sql)
        //判断是否为首单
        if (userStatusList != null && userStatusList.size > 0) {
          orderInfo.if_first_order = "0"
        } else {
          orderInfo.if_first_order = "1"
        }
        orderInfo
      }
    }
    orderInfoWithFirstFlagDStream.print(1000)*/
    //2.2 判断是否首单方案2 以分区为单位进行处理,每个分区数据执行一次SQL
    val orderInfoWithFirstFlagDStream: DStream[OrderInfo] = orderInfoDStream.mapPartitions {
      orderInfoItr => {
        val orderInfoList: List[OrderInfo] = orderInfoItr.toList
        //对订单集合进行转换
        val userIdList: List[Long] = orderInfoList.map(_.user_id)
        //执行Sql，从Phoenix查询数据
        val sql: String = s"select user_id,if_consumed from user_status0421 where user_id in ('${userIdList.mkString("','")}')"

        val userStatusList: List[JSONObject] = PhoenixUtil.queryList(sql)

        //获取已经消费过的用户的id
        val consumedUserIdList: List[String] = userStatusList.map(jsonObj => jsonObj.getString("USER_ID"))

        for (orderInfo <- orderInfoList) {
          if (consumedUserIdList != null && consumedUserIdList.contains(orderInfo.user_id.toString)) {
            orderInfo.if_first_order = "0"
          } else {
            orderInfo.if_first_order = "1"
          }
        }
        orderInfoList.toIterator
      }
    }
    //orderInfoWithFirstFlagDStream.print(1000)

    //============ 4.同批次状态修正 =============
    //4.0 因为DStream只能对KV数据进行分组，所以我们先对结构进行转换
    val userOrderDStream: DStream[(Long, OrderInfo)] = orderInfoWithFirstFlagDStream.map {
      orderInfo => {
        (orderInfo.user_id, orderInfo)
      }
    }
    //4.1将当前批次中，同一个用户的订单放到一组
    val groupByKeyDStream: DStream[(Long, Iterable[OrderInfo])] = userOrderDStream.groupByKey()

    //4.2 对当前组内的数据（用户的下单数据）  按照订单的创建时间进行排序
    val orderInfoRealWithFirstFlagDStream: DStream[OrderInfo] = groupByKeyDStream.flatMap {
      case (userId, orderInfoItr) => {
        //判断在一个采集周期内，当前用户是否下了多个订单
        if (orderInfoItr.size > 1) {
          //如果一个用户下了多个多个订单，按照时间进行排序
          val sortedList: List[OrderInfo] = orderInfoItr.toList.sortWith {
            (orderInfo1, orderInfo2) => {
              orderInfo1.create_time < orderInfo2.create_time
            }
          }
          //从用户下的订单集合中，获取第一个元素
          val firstOrderInfo: OrderInfo = sortedList(0)
          if (firstOrderInfo.if_first_order == "1") {
            //如果第一个订单标记为首单的话，那么将这个批次，该用户下的其它订单标记为非首单
            for (i <- 1 until sortedList.size) {
              sortedList(i).if_first_order = "0"
            }
          }
          sortedList.toIterator
        } else {
          orderInfoItr
        }
      }
    }

    //orderInfoRealWithFirstFlagDStream.print()

    //5.维度关联(通过Phonix进行查询)
    /*
    //5.1 关联省份维度 方案1： 和关联用户状态思路一样，以分区为单位进行关联处理
    val orderInfoWithProvinceDStream: DStream[OrderInfo] = orderInfoRealWithFirstFlagDStream.mapPartitions {
      orderInfoItr => {
        val orderInfoList: List[OrderInfo] = orderInfoItr.toList
        //获取当前批次订单中，所有的省份id
        val provinceIdList: List[Long] = orderInfoList.map(_.province_id)
        //从Phoenix中查询省份
        var sql: String = s"select id,name,area_code,iso_code from gmall0421_province_info where id in('${provinceIdList.mkString("','")}')"
        val provinceInfoJsonObjList: List[JSONObject] = PhoenixUtil.queryList(sql)
        //将省份List结构转换为Map   {"ID":"11","NAME":"江西","AREA_CODE":"360000","ISO_CODE":"CN_JX"}
        val provinceInfoJsonObjMap: Map[Long, JSONObject] = provinceInfoJsonObjList.map {
          provinceInfoJsonObj => {
            (provinceInfoJsonObj.getLongValue("ID"), provinceInfoJsonObj)
          }
        }.toMap

        for (orderInfo <- orderInfoList) {
          val provinceInfoJsonObj: JSONObject = provinceInfoJsonObjMap.getOrElse(orderInfo.province_id, null)
          orderInfo.province_name = provinceInfoJsonObj.getString("NAME")
          orderInfo.province_area_code = provinceInfoJsonObj.getString("AREA_CODE")
          orderInfo.province_iso_code = provinceInfoJsonObj.getString("ISO_CODE")
        }
        orderInfoList.toIterator
      }
    }

    //orderInfoWithProvinceDStream.print(1000)
    */
    //5.2 关联省份维度 方案2： 每个采集周期执行一次  前提：省份维度表数据比较少
    val orderInfoWithProvinceDStream: DStream[OrderInfo] = orderInfoRealWithFirstFlagDStream.transform {
      rdd => {
        //从Phoenix中查询所有的省份
        var sql: String = "select id,name,area_code,iso_code from gmall0421_province_info"
        val provinceInfoJsonObjList: List[JSONObject] = PhoenixUtil.queryList(sql)
        val provinceInfoMap: Map[String, ProvinceInfo] = provinceInfoJsonObjList.map {
          provinceInfoJsonObj => {
            val provinceInfo: ProvinceInfo = JSON.parseObject(provinceInfoJsonObj.toString, classOf[ProvinceInfo])
            (provinceInfo.id, provinceInfo)
          }
        }.toMap
        val provinceInfoMapBC: Broadcast[Map[String, ProvinceInfo]] = ssc.sparkContext.broadcast(provinceInfoMap)

        rdd.mapPartitions {
          orderInfoItr => {
            val orderInfoList: List[OrderInfo] = orderInfoItr.toList
            val provinceMap: Map[String, ProvinceInfo] = provinceInfoMapBC.value
            for (orderInfo <- orderInfoList) {
              val provinceInfo: ProvinceInfo = provinceMap.getOrElse(orderInfo.province_id.toString, null)
              if (provinceInfo != null) {
                orderInfo.province_name = provinceInfo.name
                orderInfo.province_area_code = provinceInfo.area_code
                orderInfo.province_iso_code = provinceInfo.iso_code
              }
            }
            orderInfoList.toIterator
          }
        }
      }
    }
    //orderInfoWithProvinceDStream.print()
    //5.3订单和用户维度进行关联  以分区为单位进行处理
    val orderInfoWithUserDStream: DStream[OrderInfo] = orderInfoWithProvinceDStream.mapPartitions {
      orderInfoItr => {
        val orderInfoList: List[OrderInfo] = orderInfoItr.toList
        //获取当前分区中订单对应的所有用户id集合
        val userIdList: List[Long] = orderInfoList.map(_.user_id)
        //通过Id到Phoenix进行查询
        var sql: String =
          s"select id,user_level,birthday,gender,age_group,gender_name from gmall0421_user_info where id in('${userIdList.mkString("','")}')"
        val userInfoJsonObjList: List[JSONObject] = PhoenixUtil.queryList(sql)
        val userInfoMap: Map[String, UserInfo] = userInfoJsonObjList.map {
          userInfoJsonObj => {
            val userInfo: UserInfo = JSON.parseObject(userInfoJsonObj.toString(), classOf[UserInfo])
            (userInfo.id, userInfo)
          }
        }.toMap

        for (orderInfo <- orderInfoList) {
          val userInfo: UserInfo = userInfoMap.getOrElse(orderInfo.user_id.toString, null)
          if (userInfo != null) {
            orderInfo.user_gender = userInfo.gender_name
            orderInfo.user_age_group = userInfo.age_group
          }
        }
        orderInfoList.toIterator
      }
    }
    orderInfoWithUserDStream.print(1000)


    //=============3.相关保存操作================
    // 3.1 保存到 Hbase 中
    import org.apache.phoenix.spark._
    orderInfoWithUserDStream.foreachRDD {
      rdd => {
        //优化：对RDD进行缓存
        rdd.cache()
        //3.1维护Hbase中用户首单状态
        //过滤出首单
        val firstOrderRDD: RDD[OrderInfo] = rdd.filter(_.if_first_order == "1")
        //注意：在保存的时候，RDD中元素的属性个数和表中的字段数要一致
        val firstOrderUserRDD: RDD[UserStatus] = firstOrderRDD.map {
          orderInfo => {
            UserStatus(orderInfo.user_id.toString, "1")
          }
        }
        firstOrderUserRDD.saveToPhoenix(
          "USER_STATUS0421",
          Seq("USER_ID", "IF_CONSUMED"),
          new Configuration,
          Some("hadoop202,hadoop203,hadoop204:2181")
        )

        // 3.2 保存到 ES 中
        rdd.foreachPartition {
          orderInfoItr => {
            val orderInfoList: List[(String, OrderInfo)] = orderInfoItr.toList.map {
              orderInfo => {
                (orderInfo.id.toString, orderInfo)
              }
            }
            val dt: String = new SimpleDateFormat("yyyy-MM-dd").format(new Date)
            MyESUtil.bulkInsert(orderInfoList, "gmall0421_order_info_" + dt)

            //3.3将订单信息推回 kafka 进入下一层处理   主题： dwd_order_info
            for ((id, orderInfo) <- orderInfoList) {
              //fastjson 要把scala对象包括caseclass转json字符串 需要加入,new SerializeConfig(true)
              MyKafkaSink.send("dwd_order_info", JSON.toJSONString(orderInfo, new SerializeConfig(true)))
            }
          }
        }
        //修改偏移量
        OffsetManagerUtil.saveOffset(topic, groupId, offsetRangs)
      }
    }

    ssc.start()
    ssc.awaitTermination()
  }
}
