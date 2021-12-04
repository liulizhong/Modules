package core

import org.apache.spark.broadcast.Broadcast
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}
import scala.collection.mutable.ArrayBuffer

/**
  * @class 广播变量的案例使用
  * @CalssName _8_广播变量
  * @author lizhong.liu 
  * @create 2020-06-30 17:32
  * @Des TODO
  * @version TODO
  */
object TestBroadcast {
  def main(args: Array[String]): Unit = {
    // 1、准备Spark配置信息及上下文对象
    val conf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("Test")
    val sc: SparkContext = new SparkContext(conf)

    // 2、初始化RDD模拟数据
    val rdd1: RDD[(String, Int)] = sc.makeRDD(Array(("a", 1), ("b", 1)))

    // 3、初始化要广播的变量
    val arr = Array(("a", 3), ("a", 4))

    // 4、使用广播变量来共享数据，但只能读取而不能改。将变量封装为广播变量，这里的广播变量不能太大
    val arrBroadcast: Broadcast[Array[(String, Int)]] = sc.broadcast(arr)

    val resultRDD: RDD[Array[Any]] = rdd1.map(t => {
      val rddKey = t._1
      // 5、从广播变量中获取数据
      arrBroadcast.value.map(t1 => {
        val arrKey = t1._1
        if (rddKey.equals(arrKey)) {
          (rddKey, (t._2, t1._2))
        }
      })
    })
    resultRDD.foreach(x => x.foreach(println))
  }
}

