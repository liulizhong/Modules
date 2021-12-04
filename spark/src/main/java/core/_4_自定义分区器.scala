package core

import org.apache.spark.rdd.RDD
import org.apache.spark.{HashPartitioner, Partitioner, SparkConf, SparkContext}
import org.apache.spark.util.Utils

/**
  * @class 自定义分区器，实现指定数据到哪个分区
  * @CalssName _4_自定义分区器
  * @author lizhong.liu 
  * @create 2020-06-30 15:43
  * @Des TODO
  * @version TODO
  */
object TestPartitioner {
  def main(args: Array[String]): Unit = {
    val conf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("Test")
    // 创建Spark上下文对象
    val sc: SparkContext = new SparkContext(conf)
    val rdd: RDD[(String, Int)] = sc.makeRDD(Array(("a", 1), ("b", 2), ("c", 3), ("d", 4)), 4)
    val parRDD: RDD[(String, Int)] = rdd.partitionBy(new CustomPartitioner(2))
    val rddWithIndex: RDD[(Int, (String, Int))] = parRDD.mapPartitionsWithIndex((index, items) => {
      items.map((index, _))
    })
    rddWithIndex.foreach(t => {
      println(t)
    })
    sc.stop()
  }
}

class CustomPartitioner(partitions: Int) extends Partitioner {
  require(partitions >= 0, s"Number of partitions ($partitions) cannot be negative")

  // 1、获取分区数量
  override def numPartitions: Int = partitions

  // 2、获取分区号(利用hash值%分区数得出)
  override def getPartition(key: Any): Int = {
    key match {
      case null => 0
      case _ => key.hashCode() % partitions
    }
  }

  // 3、返回hashcode值
  override def hashCode(): Int = numPartitions

  // 4、看分区器是不是一个
  override def equals(obj: Any): Boolean = {
    obj match {
      case h: CustomPartitioner => h.numPartitions == numPartitions
      case _ => false
    }
  }
}