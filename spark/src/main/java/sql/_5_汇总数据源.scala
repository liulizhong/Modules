package sql

import org.apache.spark.SparkConf
import org.apache.spark.sql.{DataFrame, SparkSession}

/**
  * @class 汇总了spark-sql读取数据源的方式，文件、JDBC、hive
  * @CalssName _5_汇总数据源
  * @author lizhong.liu 
  * @create 2020-07-01 17:09
  * @Des TODO
  * @version TODO
  */
object _5_汇总数据源 {
  def main(args: Array[String]): Unit = {
    // 1、创建SparkSession
    val sparkConf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("Test")
    val spark: SparkSession = SparkSession.builder().config(sparkConf).getOrCreate()

    // 2、普通读取文件（csv、jdbc、load、options、parquet、table、textFile、format、json、option、orc、schema、text）
    val dataFrame: DataFrame = spark.read.json("D:\\tmp\\2.json");
    dataFrame.show()
    // 3、通用方式读取json（load 为通用读文件方法，默认parquet格式，可通过format("json")指定文件格式）
    val jsonDF: DataFrame = spark.read.format("json").load("D:\\tmp\\2.json")
    jsonDF.show()
    // 4、普通写文件
    //jsonDF.write.json("D:\\tmp\\output")
    // 5、通用方法写文件save：save为通用写方法，默认parquet格式，可通过format("json")指定文件格式,
    // 通过mode指定写入模式Append追加，Overwrite覆写，Ignore存在则忽略，默认是存在会报错
    jsonDF.write.format("json").mode("append").save("D:\\tmp\\output")

    // 6、jdbc链接数据库(前提是将MySQL驱动放到../spark/jars/下)操作的两种方法，一、通过option添加连接配置信息。二通过Properties传入连接配置信息
    val jdbcDataFrame: DataFrame = spark.read.format("jdbc").option("url", "jdbc:mysql://hadoop102:3306/rdd").option("dbtable", "rddtable").option("user", "root").option("password", "000000").load() //方式一
    //    val connectionProperties = new Properties()
    //    connectionProperties.put("user", "root");
    //    connectionProperties.put("password", "000000")
    //    val jdbcDF2 = spark.read.jdbc("jdbc:mysql://hadoop102:3306/rdd", "rddtable", connectionProperties)
    // 7、写入数据同样是两种方法(一样可设置写入模式)：一option。二、Properties
    jdbcDataFrame.write
      .format("jdbc")
      .option("url", "jdbc:mysql://hadoop102:3306/rdd")
      .option("dbtable", "dftable")
      .option("user", "root")
      .option("password", "000000")
      .save()
    //    jdbcDataFrame.write.mode("append").jdbc("jdbc:mysql://hadoop102:3306/rdd", "db", connectionProperties)

    // 8、spark-sql操作hive(先设置spark链接外部hive然后通过一、spark-shell> spark.sql("show databases")，或者二、spark-sql>直接使用hive语句即可)
  }
}
