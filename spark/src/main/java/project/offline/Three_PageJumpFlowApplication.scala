package project.offline

import java.sql.{Connection, DriverManager, PreparedStatement}
import java.util.UUID
import project.xbean._
import project.xutil._
import com.alibaba.fastjson.{JSON, JSONObject}
import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, SparkSession}
import org.apache.spark.util.AccumulatorV2

import scala.collection.mutable

// 页面单跳转化率统计：("1-2", int)、("2-3", int)、、、
object Three_PageJumpFlowApplication extends Application {
  override def doApplication: Unit = {
    // 创建Spark配置对象
    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("CategoryTop10Application")
    // 创建SparkSession对象
    val spark = SparkSession.builder().config(sparkConf).enableHiveSupport().getOrCreate()
    val (logRDD, jsonObject) = hiveDataToRDD(spark)
    doService(spark, jsonObject, logRDD)
  }

  def doService(spark: SparkSession, jsonObject: JSONObject, actionLogRdd: RDD[UserVisitAction]): Unit = {
    // 需求三的业务逻辑
    // 4.1 将日志数据进行筛选过滤：符合跳转转换的页面ID（1~7）
    val pageids: Array[String] = jsonObject.getString("targetPageFlow").split(",")
    // 4.2.1 将数据通过sessionid进行分组
    val sessionGroupRDD: RDD[(String, Iterable[UserVisitAction])] = actionLogRdd.groupBy(log => log.session_id)
    // 4.2.2 将过滤后的数据进行排序：按照点击时间升序
    // [sessionid, List(UserVisitAction)]
    // zip
    // [sessionid, List(pageflow, count)] ==> map(_._2)
    // [List(pageflow, count)] ==> flatMap
    // [pageflow, count]
    val zipLogRDD: RDD[(String, Int)] = sessionGroupRDD.mapValues(datas => {
      val sortLogs: List[UserVisitAction] = datas.toList.sortWith {
        case (left, right) => {
          left.action_time < right.action_time
        }
      }
      val sortPageids: List[Long] = sortLogs.map(log => log.page_id)
      sortPageids.zip(sortPageids.tail).map {
        case (pageid1, pageid2) => {
          (pageid1 + "-" + pageid2, 1)
        }
      }
    }).map(_._2).flatMap(x => x)

    // 4.3 将筛选后的数据根据页面ID进行分组聚合统计点击次数（除数A）
    val filterPageRDD: RDD[UserVisitAction] = actionLogRdd.filter(log => {
      pageids.contains("" + log.page_id)
    })
    val pageSumClickRDD: RDD[(Long, Long)] = filterPageRDD.map(log => (log.page_id, 1L)).reduceByKey(_ + _)
    val pageSumClickMap: Map[Long, Long] = pageSumClickRDD.collect().toMap

    // 4.4 将页面转换的ID进行拉链（zip）操作: (12, 23,34)
    val zipPageArray: Array[String] = pageids.zip(pageids.tail).map {
      case (pageid1, pageid2) => {
        pageid1 + "-" + pageid2
      }
    }

    // 4.5 将筛选后的日志数据进行拉链，然后和我们需要的页面转换拉链数据进行比对过滤
    // 4.5.1 将数据进行过滤
    val zipLogFilterRDD: RDD[(String, Int)] = zipLogRDD.filter(zipLog => {
      zipPageArray.contains(zipLog._1)
    })

    // 4.6 将过滤的数据进行分组聚合统计点击次数（B）
    // [pageflow, sum]
    val zipLogReduceRDD: RDD[(String, Int)] = zipLogFilterRDD.reduceByKey(_ + _)

    val taskId = UUID.randomUUID().toString

    // 4.7 将(A-B)/A结果保存到Mysql数据库
    zipLogReduceRDD.foreach {
      case (pageflow, sum) => {
        val a = pageflow.split("-")(0)
        val aCount = pageSumClickMap(a.toLong)
        // 15 / 100 = 0.15564365456 * 100 = 15.45454545
        val result = (sum.toDouble / aCount * 100).toLong
        println(pageflow + ":" + result)
      }
    }

    // 操作数据库，保存数据
    //        val driverClass = getConfigValue("jdbc.driver.class")
    //        val url = getConfigValue("jdbc.url")
    //        val user = getConfigValue("jdbc.user")
    //        val password = getConfigValue("jdbc.password")
    //
    //        Class.forName(driverClass)
    //        val conn : Connection = DriverManager.getConnection(url, user, password)
    //
    //        val insertSql = "insert into category_top10 values (?, ?, ?, ?, ?)"
    //
    //        val pstat: PreparedStatement = conn.prepareStatement(insertSql)
    //
    //        infoes.foreach(info=>{
    //            pstat.setObject(1, info.taskid)
    //            pstat.setObject(2, info.category_id)
    //            pstat.setObject(3, info.clickCount)
    //            pstat.setObject(4, info.orderCount)
    //            pstat.setObject(5, info.payCount)
    //            pstat.executeUpdate()
    //        })
    //        pstat.close()
    //        conn.close()
    spark.close()
  }
}