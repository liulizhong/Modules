package core

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
  * @class 案例2、统计出每一个省份广告被点击次数的TOP3
  * @CalssName adsTop3
  * @author lizhong.liu
  * @create 2020-06-30 11:08
  * @Des TODO
  * @version TODO
  */
object AdsTOP3 {
  def main(args: Array[String]): Unit = {
    // 1、读取和创建SC
    val sparkConf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("adsTop3")
    val sparkContext: SparkContext = new SparkContext(sparkConf)
    val fileRDD: RDD[String] = sparkContext.textFile("D:\\tmp\\agent.log")
    // 2、将“1516609143867 6 7 64 16”数据解析成（（省份，广告），数量）并求和
    val provinceAdsAndSum: RDD[((String, String), Int)] = fileRDD.map(str => {
      val strings: Array[String] = str.split(" ")
      ((strings(1), strings(4)), 1)
    }).reduceByKey(_ + _)
    // 3、将（（省份，广告），数量）改为（省份，（广告，数量）），并按省份分组，排序后取前三
    val provinceAdsTake3: RDD[(String, List[(String, Int)])] = provinceAdsAndSum.map(value => (value._1._1, (value._1._2, value._2))).groupByKey().mapValues(v => {
      v.toList.sortWith((num1, num2) => num1._2 > num2._2).take(3)
    })
    // 4、收集成集合并打印
    provinceAdsTake3.collect().foreach(println)
    // 5、打印方法二
    provinceAdsTake3.foreach(t => {
      t._2.foreach(t1 => {
        println(t._1 + "," + t1._1 + ", " + t1._2)
      })
    })
    // 6、保存文件到本地
    provinceAdsTake3.saveAsTextFile("D:\\tmp\\logresult.log")
    // 7、关闭流
    sparkContext.stop()
  }
}
