package project.offline

import java.sql.{Connection, DriverManager, PreparedStatement}
import java.util.UUID

import com.alibaba.fastjson.JSON
import project.xbean._
import org.apache.spark.SparkConf
import org.apache.spark.sql.{DataFrame, SparkSession}
import project.xutil._
import org.apache.spark.rdd.RDD
import org.apache.spark.util.AccumulatorV2

import scala.collection.{immutable, mutable}

// 需求一：Top10 热门品类：获取点击数量排名前 10 的品类，若点击数量一致则看下单数量，下单一样看支付。
object One_CategoryTop10Application {
  def main(args: Array[String]): Unit = {
    // 1、创建Spark、SparkSession配置对象、导入隐式转换
    val sparkConf = new SparkConf().setMaster("local[*]").setAppName("CategoryTop10Application")
    val spark = SparkSession.builder().config(sparkConf).enableHiveSupport().getOrCreate()
    import spark.implicits._

    // 2、读取动作日志数据
    spark.sql("use " + getConfigValue("hive.database"));
    // 因为后续需要拼接动态条件，所以sql中增加where 1 = 1
    val sql = "select * from user_visit_action where 1 = 1 "
    // 获取查询条件，并利用Alibaba工具解析JSON
    val jsonConfig: String = getValueFromResource("condition", "condition.params.json")
    val jsonObject = JSON.parseObject(jsonConfig)
    val startDate = jsonObject.getString("startDate")
    val endDate = jsonObject.getString("endDate")
    // 用StringBuilder拼接sql语句
    val sqlBuilder = new StringBuilder(sql)
    if (isNotEmptyString(startDate)) {
      sqlBuilder.append(" and action_time >= '").append(startDate).append("'")
    }
    if (isNotEmptyString(endDate)) {
      sqlBuilder.append(" and action_time <= '").append(endDate).append("'")
    }
    // 查询数据，将数据转换为特定类型RDD[UserVisitAction]
    val dataFrame: DataFrame = spark.sql(sqlBuilder.toString())
    val actionLogRdd: RDD[UserVisitAction] = dataFrame.as[UserVisitAction].rdd

    // 3、算子操作数据
    // (1, click, sum)
    //                        (1, order, sum)
    //                                              (1, pay, sum)
    // (1, click, 1)
    // 使用join算子可以实现功能，但是性能不好。
    // 使用累加器。将不同操作的数据计算总和
    val accumulator = new CategoryCountAccumulator
    // 注册累加器
    spark.sparkContext.register(accumulator, "accumulator")

    // 循环RDD数据，进行累加操作
    actionLogRdd.foreach(log => {
      if (log.click_category_id != -1) {
        accumulator.add(log.click_category_id + "_click")
      } else if (log.order_category_ids != null) {
        log.order_category_ids.split(",").foreach(cid => {
          accumulator.add(cid + "_order")
        })
      } else if (log.pay_category_ids != null) {
        log.pay_category_ids.split(",").foreach(cid => {
          accumulator.add(cid + "_pay")
        })
      }
    })

    // 将累加器的结果进行分组
    val statResult: Map[String, mutable.HashMap[String, Long]] = accumulator.value.groupBy {
      case (k, _) => {
        k.split("_")(0)
      }
    }

    val taskId = UUID.randomUUID().toString

    val infoList: List[CategoryCountInfo] = statResult.map {
      case (cid, map) => {
        CategoryCountInfo(
          taskId,
          cid,
          map.getOrElse(cid + "_click", 0L),
          map.getOrElse(cid + "_order", 0L),
          map.getOrElse(cid + "_pay", 0L)
        )
      }
    }.toList

    // sortWith默认情况下返回true表示升序，返回false表示降序
    // 排序后取前10个
    // 获取品类的前10名
    val infoes: List[CategoryCountInfo] = infoList.sortWith {
      case (left, right) => {

        if (left.clickCount < right.clickCount) {
          false
        } else if (left.clickCount == right.clickCount) {
          if (left.orderCount < right.orderCount) {
            false
          } else if (left.orderCount == right.orderCount) {
            left.payCount > right.payCount
          } else {
            true
          }
        } else {
          true
        }
      }
    }.take(10)

    // 4、操作数据库，保存数据
    val driverClass = getConfigValue("jdbc.driver.class")
    val url = getConfigValue("jdbc.url")
    val user = getConfigValue("jdbc.user")
    val password = getConfigValue("jdbc.password")
    Class.forName(driverClass)
    val conn: Connection = DriverManager.getConnection(url, user, password)
    val insertSql = "insert into category_top10 values (?, ?, ?, ?, ?)"
    val pstat: PreparedStatement = conn.prepareStatement(insertSql)
    infoes.foreach(info => {
      pstat.setObject(1, info.taskid)
      pstat.setObject(2, info.category_id)
      pstat.setObject(3, info.clickCount)
      pstat.setObject(4, info.orderCount)
      pstat.setObject(5, info.payCount)
      pstat.executeUpdate()
    })
    pstat.close()
    conn.close()
    spark.close()
  }

  class CategoryCountAccumulator extends AccumulatorV2[String, mutable.HashMap[String, Long]] {

    private var map = new mutable.HashMap[String, Long]()

    override def isZero: Boolean = map.isEmpty

    override def copy(): AccumulatorV2[String, mutable.HashMap[String, Long]] = {
      new CategoryCountAccumulator
    }

    override def reset(): Unit = {
      map = new mutable.HashMap[String, Long]()
    }

    override def add(v: String): Unit = {
      map(v) = map.getOrElse(v, 0L) + 1L
    }

    override def merge(other: AccumulatorV2[String, mutable.HashMap[String, Long]]): Unit = {
      map = map.foldLeft(other.value) {
        case (xmap, (key, count)) => {
          xmap(key) = xmap.getOrElse(key, 0L) + count
          xmap
        }
      }
    }

    override def value: mutable.HashMap[String, Long] = map
  }

}
