package project.offline

import project.xbean.UserVisitAction
import com.alibaba.fastjson.{JSON, JSONObject}
import org.apache.spark.SparkConf
import project.xbean._
import project.xutil._
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, SparkSession}

class Application {
    def doApplication: Unit = ???
    def main(args: Array[String]): Unit = {
        doApplication
    }
    def hiveDataToRDD(spark:SparkSession) : (RDD[UserVisitAction], JSONObject) = {
        // 导入隐式转换
        import spark.implicits._
        // 读取动作日志数据
        spark.sql("use " + getConfigValue("hive.database"));
        // 因为后续需要拼接动态条件，所以sql中增加where 1 = 1
        val sql = "select * from user_visit_action where 1 = 1 "
        // 获取查询条件
        val jsonConfig : String = getValueFromResource("condition", "condition.params.json")
        // 将JSON字符串转换为JSON对象
        val jsonObject = JSON.parseObject(jsonConfig)
        val startDate = jsonObject.getString("startDate")
        val endDate = jsonObject.getString("endDate")
        val sqlBuilder = new StringBuilder(sql)
        if ( isNotEmptyString(startDate) ) {
            sqlBuilder.append(" and action_time >= '").append(startDate).append("'")
        }
        if ( isNotEmptyString(endDate) ) {
            sqlBuilder.append(" and action_time <= '").append(endDate).append("'")
        }
        // 查询数据，将数据转换为特定类型
        val dataFrame: DataFrame = spark.sql(sqlBuilder.toString())
        (dataFrame.as[UserVisitAction].rdd, jsonObject)
    }
}
