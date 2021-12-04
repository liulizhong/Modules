package sql

import org.apache.spark.SparkConf
import org.apache.spark.sql.{DataFrame, Row, SparkSession}
import org.apache.spark.sql.expressions.{MutableAggregationBuffer, UserDefinedAggregateFunction}
import org.apache.spark.sql.types.{DataType, DoubleType, LongType, StructType}

/**
  * @class 自定义spark的弱类型聚合函数，只有结构无类型适合DataFrame
  * @CalssName _3_自定义spark的弱类型聚合函数
  * @author lizhong.liu 
  * @create 2020-07-01 15:56
  * @Des TODO
  * @version TODO
  */
object _3_自定义spark的弱类型聚合函数 {
  def main(args: Array[String]): Unit = {
    // 1、创建SparkSession对象并配置，SparkSession是包含sparkConf的关系，相当于DataFrame和RDD关系
    val sparkConf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("Test")
    val spark: SparkSession = SparkSession.builder().config(sparkConf).getOrCreate()

    // 2、注册函数
    val myUserAverage: MyUserAverage = new MyUserAverage
    spark.udf.register("myUserAverage", myUserAverage)

    // 3、读取数据源
    val df: DataFrame = spark.read.json("D:\\tmp\\2.json")
    df.createOrReplaceTempView("employees")
    df.show()
    val result: DataFrame = spark.sql("SELECT myUserAverage(age) as average_age FROM employees")
    result.show()
  }
}

class MyUserAverage extends UserDefinedAggregateFunction {

  // 1、聚合函数输入参数的数据类型
  override def inputSchema: StructType = {
    new StructType().add("inputColumn", LongType)
  }

  // 2、聚合缓冲区中值的数据类型
  override def bufferSchema: StructType = {
    new StructType().add("sum", LongType).add("count", LongType)
  }

  // 3、返回值的数据类型
  override def dataType: DataType = {
    DoubleType
  }

  // 4、对于相同的输入是否一直返回相同的输出。
  override def deterministic: Boolean = true

  // 5、初始化
  override def initialize(buffer: MutableAggregationBuffer): Unit = {
    // 第一个结构[add("sum", LongType)]：初始化存工资的总额
    buffer(0) = 0L
    // 第二个结构[add("count", LongType)]:初始化存工资的个数
    buffer(1) = 0L
  }

  // 6、相同Execute间的数据合并。buffer是缓存的值，input是新来的值
  override def update(buffer: MutableAggregationBuffer, input: Row): Unit = {
    buffer(0) = buffer.getLong(0) + input.getLong(0)
    buffer(1) = buffer.getLong(1) + 1
  }

  // 7、不同Execute间的数据合并
  override def merge(buffer1: MutableAggregationBuffer, buffer2: Row): Unit = {
    buffer1(0) = buffer1.getLong(0) + buffer2.getLong(0)
    buffer1(1) = buffer1.getLong(1) + buffer2.getLong(1)
  }

  // 8、计算最终结果
  override def evaluate(buffer: Row): Any = {
    buffer.getLong(0).toDouble / buffer.getLong(1)
  }
}
