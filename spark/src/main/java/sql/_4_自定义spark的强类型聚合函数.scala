package sql

import org.apache.spark.SparkConf
import org.apache.spark.sql.expressions.Aggregator
import org.apache.spark.sql._

/**
  * @class 自定义spark的强类型聚合函数，适合DataSet
  * @CalssName _3_自定义spark的强类型聚合函数
  * @author lizhong.liu 
  * @create 2020-07-01 15:56
  * @Des TODO
  * @version TODO
  */
object _4_自定义spark的强类型聚合函数 {
  def main(args: Array[String]): Unit = {
    // 1、创建SparkSession对象并配置，SparkSession是包含sparkConf的关系，相当于DataFrame和RDD关系
    val sparkConf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("Test")
    val spark: SparkSession = SparkSession.builder().config(sparkConf).getOrCreate()
    import spark.implicits._

    // 2、注册函数，spark-sql用不了强类型函数，因为传入的是对象，那么只能用DSL风格使用强类型聚合函数
    val myAggAverage: MyAggAverage = new MyAggAverage
    val averageage: TypedColumn[Employee, Double] = myAggAverage.toColumn.name("myAggAverage")

    // 3、读取数据源
    val ds: Dataset[Employee] = spark.read.json("D:\\tmp\\2.json").as[Employee]
    ds.show()
    val resultAvgage: Dataset[Double] = ds.select(averageage)
    resultAvgage.show()
  }
}

// 强类型聚合函数
class MyAggAverage extends Aggregator[Employee, Average, Double] {

  // 1、定义一个数据结构，保存工资总数和工资总个数，初始都为0
  override def zero: Average = {
    new Average(0L, 0L)
  }

  // 2、相同Execute间的数据合并。buffer是缓存的值，input是新来的值
  override def reduce(b: Average, a: Employee): Average = {
    b.sum = b.sum + a.age
    b.count = b.count + 1
    b
  }

  // 3、不同Execute间的数据合并
  override def merge(b1: Average, b2: Average): Average = {
    b1.sum += b2.sum
    b1.count += b2.count
    b1
  }

  // 4、计算最终输出结果
  override def finish(reduction: Average): Double = {
    reduction.sum.toDouble / reduction.count
  }

  // 5、设定之间值类型的编码器，要转换成我们自定义的case类，便传一个生产器就可以
  override def bufferEncoder: Encoder[Average] = Encoders.product

  // 6、设定最终输出值的编码器,scala已经存在的数据类型直接传给它，要是没有就给个生产器
  override def outputEncoder: Encoder[Double] = Encoders.scalaDouble
}

// 样例类
case class Employee(name: String, age: Long)

case class Average(var sum: Long, var count: Long) //注意一定要加上var，因为默认是val，不能sum的相加