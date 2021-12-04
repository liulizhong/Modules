package core

import java.util

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.util.{AccumulatorV2, LongAccumulator}

/**
  * @class 累加器：想要将共享变量传递给Executor执行，但是由于Spark无法将变量的值传回到Driver端，所以driver端变量的值无法改变，累加器可以实现此场景，共享变量后传回driver
  * @CalssName _7_累加器自定义_单词的黑名单
  * @author lizhong.liu 
  * @create 2020-06-30 17:16
  * @Des TODO
  * @version TODO
  */
object TestAccumulator {

  def main(args: Array[String]): Unit = {

    // 1、准备Spark配置信息及上下文对象
    val conf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("Test")
    val sc: SparkContext = new SparkContext(conf)

    // 2、初始化RDD模拟数据
    val rdd: RDD[String] = sc.makeRDD(Array("hello", "String", "Hadoop", "hadooop"))

    // 3、声明累加器，并注册到driver中
    val accumulator = new BlackListAccumulator
    sc.register(accumulator, "blackList")

    // 4、运用累加器获取rdd中字符串包含"h"的单词
    rdd.foreach(word => {
      accumulator.add(word)
    })

    // 4.2、直接使用累加器
    //    val sum: LongAccumulator = sc.longAccumulator("sum")
    //    rdd.foreach(x => {
    //      sum.add(x)    //假设RDD中都是数字，这样使用累加器即可求和
    //    })

    // 5、获取累加器的值
    println(accumulator.value)

    // 6、关闭上下文对象
    sc.stop()
  }
}

// 黑名单累加器
class BlackListAccumulator extends AccumulatorV2[String, util.ArrayList[String]] {

  var blackList = new util.ArrayList[String]

  // 1、是否已经初始化
  override def isZero: Boolean = {
    blackList.isEmpty
  }

  // 2、复制当前的累加器传递给Executor
  override def copy(): AccumulatorV2[String, util.ArrayList[String]] = {
    val acc: BlackListAccumulator = new BlackListAccumulator()
    acc
  }

  // 3、将当前的累加器重置（清空），确保传给executer是空的
  override def reset(): Unit = {
    blackList.clear()
  }

  // 4、累加数据，每个executer来数据时的处理操作
  override def add(v: String): Unit = {
    if (v.contains("h")) {
      blackList.add(v)
    }
  }

  // 5、合并数据，多个executer直接数据合并操作
  override def merge(other: AccumulatorV2[String, util.ArrayList[String]]): Unit = {
    blackList.addAll(other.value)
  }

  // 6、获取累加器的值
  override def value: util.ArrayList[String] = blackList
}
