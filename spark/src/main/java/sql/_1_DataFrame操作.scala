package sql

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.{DataFrame, Dataset, Row, SparkSession}

/**
  * @class spark-sql的 DataFrame 操作
  * @CalssName _1_DataFrame操作
  * @author lizhong.liu 
  * @create 2020-07-01 11:42
  * @Des TODO
  * @version TODO
  */
object _1_DataFrame操作 {
  def main(args: Array[String]): Unit = {
    // 1、创建SparkSession对象并配置，SparkSession是包含sparkConf的关系，相当于DataFrame和RDD关系
    val sparkConf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("Test")
    val spark: SparkSession = SparkSession.builder().config(sparkConf).getOrCreate()

    // 2、提前准备好隐式转换
    import spark.implicits._

    // 3、创建dataframe的三种方式
    val df: DataFrame = spark.read.json("D:\\tmp\\2.json") //一、数据源创建csv、jdbc、load、options、parquet、table、textFile、format、json、option、orc、schema、text
    // val df = ***    // 二、从现有的RDD转化
    // val df = ***    // 三、从hive表查询返回

    // 4、展示结果
    df.show()

    // 5、过滤操作 ( 要是判断相等的话用===，表示类型和值都相等，==只表示值相等 )
    df.filter($"age" > 21).show()

    // 6、创建临时试图表（全局视图：createGlobalTempView 有则替换：createOrReplaceTempView  有则替换全局[全局即跨越session]：createOrReplaceGlobalTempView   临时视图最简单：createTempView）
    df.createOrReplaceTempView("persons")

    // 7、对刚创建的试图进行sql语句执行
    spark.sql("SELECT * FROM persons where age > 21").show()

    // 8、创建RDD转化为Dataframe两种方法：（一、指定每一列的属性名。二、先将RDD一行元素转为样例类）
    val txtRDD: RDD[String] = spark.sparkContext.textFile("D:\\tmp\\nameAndAge.txt")
    //val dataFrame: DataFrame = txtRDD.map{x=>val para = x.split(",");(para(0),para(1).trim.toInt)}.toDF("name","age")
    val dataFrame: DataFrame = txtRDD.map { x => val para = x.split(","); People(para(0), para(1).trim.toInt) }.toDF
    dataFrame.show()

    // 9、Dataframe转化为RDD
    val dfToRDD: RDD[Row] = dataFrame.rdd
    dfToRDD.foreach(println)

    // 19、关闭sparksession资源
    spark.stop()
  }
}

case class People(name: String, age: Int)
