package sql

import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, Dataset, Row, SparkSession}

/**
  * @class park-sql的 DataSet操作
  * @CalssName _2_DataSet操作
  * @author lizhong.liu 
  * @create 2020-07-01 14:37
  * @Des TODO
  * @version TODO
  */
object _2_DataSet操作 {
  def main(args: Array[String]): Unit = {
    // 1、创建SparkSession对象并配置，SparkSession是包含sparkConf的关系，相当于DataFrame和RDD关系
    val sparkConf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("Test")
    val spark: SparkSession = SparkSession.builder().config(sparkConf).getOrCreate()
    // val sparkSession: SparkSession = new SparkSession(sparkConf)  // 报错，因SparkSession此构造方法为peivate，idea的小bug

    // 2、提前准备好隐式转换(spark是Sparksession对象)
    import spark.implicits._

    // 3、创建dataframe的三种方式
    val dataSet: Dataset[Person] = Seq(Person("zhangsan", 18), Person("李四", 32), Person("wangwu", 25)).toDS()

    // 4、展示结果
    dataSet.show()

    // 5、过滤操作 ( 要是判断相等的话用===，表示类型和值都相等，==只表示值相等 )
    dataSet.filter($"age" > 21).show()

    // 6、创建临时试图表（全局视图：createGlobalTempView 有则替换：createOrReplaceTempView  有则替换全局[全局即跨越session]：createOrReplaceGlobalTempView   临时视图最简单：createTempView）
    dataSet.createOrReplaceTempView("persons")

    // 7、对刚创建的试图进行sql语句执行
    spark.sql("SELECT * FROM persons where age > 21").show()

    // 8、创建RDD转化为DataSet只有一种方法：样例类
    val txtRDD: RDD[String] = spark.sparkContext.textFile("D:\\tmp\\nameAndAge.txt")
    val txtToDataSet: Dataset[Person] = txtRDD.map { x => val para = x.split(","); Person(para(0), para(1).trim.toInt) }.toDS()
    txtToDataSet.show()

    // 9、Dataframe转化为RDD
    val dsToRDD: RDD[Person] = txtToDataSet.rdd
    dsToRDD.foreach(println)

    // 10、DataSet转化为DataFrame
    val dataFrame: DataFrame = txtToDataSet.toDF()
    dataFrame.show()

    // 11、DataFram转化为DataSete
    val dfToDs: Dataset[Person] = dataFrame.as[Person]
    dfToDs.show()

    // 12、用户自定义UDF函数,先定义addName函数，并在执行sql时使用
    spark.udf.register("addName", (x: String) => "Name:" + x)
    spark.sql("SELECT addName(name),age FROM persons where age > 21").show()

    // 14、关闭sparksession资源
    spark.stop()
  }
}

// 样例类
case class Person(name: String, age: Int)
