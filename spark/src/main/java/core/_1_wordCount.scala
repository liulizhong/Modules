package core

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
  * @class 案例1、对磁盘文件进行简单的wordCount
  * @CalssName WordCount
  * @author lizhong.liu
  * @create 2020-06-29 15:06
  * @Des TODO
  * @version TODO
  */
object WordCount {
  def main(args: Array[String]): Unit = {
    //1.创建SparkConf并设置App名称
    val conf = new SparkConf().setMaster("local[*]").setAppName("WC")
    //2.创建SparkContext，该对象是提交Spark App的入口
    val sc = new SparkContext(conf)
    // 3、从开发项目根目录读取本地文件
    val lines: RDD[String] = sc.textFile("D:\\tmp")
    val words: RDD[String] = lines.flatMap(_.split(" "))
    val wordAndOne: RDD[(String, Int)] = words.map((_, 1))
    val wordAndNums: RDD[(String, Int)] = wordAndOne.reduceByKey(_ + _, 1)
    val result: RDD[(String, Int)] = wordAndNums.sortBy(_._2, false)
    result.foreach(println(_))
    result.saveAsTextFile("D:\\tmp\\result.txt")
    sc.stop()
  }
}
