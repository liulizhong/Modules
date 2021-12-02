package flink基础.电商项目

import java.sql.Timestamp
import java.util.Properties
import org.apache.flink.api.common.functions.AggregateFunction
import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.api.common.state.{ListState, ListStateDescriptor}
import org.apache.flink.api.java.tuple.{Tuple, Tuple1, Tuple2}
import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.functions.KeyedProcessFunction
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.scala.function.WindowFunction
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.api.windowing.windows.TimeWindow
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer
import org.apache.flink.util.Collector

import scala.collection.mutable.ListBuffer

/**
  * @class 需求一：实时热门商品统计
  *        -- 1小时窗口大小滑动5分钟，实时统计这一小时内广告点击量前三的展示
  * @CalssName HotItems
  * @author lizhong.liu
  * @create 2020-10-14 16:11
  * @Des TODO
  * @version TODO
  */

// 定义输入数据的样例类
case class UserBehavior(userId: Long, itemId: Long, categoryId: Int, behavior: String, timestamp: Long)

// 定义窗口聚合结果样例类
case class ItemViewCount(itemId: Long, windowEnd: Long, count: Long)

object HotItems {
  def main(args: Array[String]): Unit = {
    //## 1. 创建执行环境
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(1) //实际应用并行度肯定不是1，可考虑32
    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)

    //## 2. 读取数据
    val dataStream = env.readTextFile("C:\\workhouse\\LearnAccumulate\\src\\main\\java\\alltool\\flink\\电商项目\\resources\\UserBehavior.csv")
      /*    val properties = new Properties()
          properties.setProperty("bootstrap.servers", "localhost:9092")
          properties.setProperty("group.id", "consumer-group")
          properties.setProperty("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
          properties.setProperty("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
          properties.setProperty("auto.offset.reset", "latest")
    val dataStream = env.addSource( new FlinkKafkaConsumer[String]("hotitems", new SimpleStringSchema(), properties) )*/
      //Kafka数据源时的代码
      .map(data => {
      val dataArray = data.split(",")
      UserBehavior(dataArray(0).trim.toLong, dataArray(1).trim.toLong, dataArray(2).trim.toInt, dataArray(3).trim, dataArray(4).trim.toLong)
    })
      .assignAscendingTimestamps(_.timestamp * 1000L) // 当前为有序数据情况使用Watermark设置为timestamp字段

    //## 3. transform 处理数据
    val processedStream: DataStream[String] = dataStream
      .filter(_.behavior == "pv") // 过滤出pv行为
      .keyBy(_.itemId) // 按照itemId分组,用"itemId"的话会导致系统识别不出来字段类型
      .timeWindow(Time.hours(1), Time.minutes(5)) // 开窗口函数
      .aggregate(new CountAgg(), new WindowResult()) // 窗口聚合操作aggregate(CountAgg:自定义聚合函数, WindowResult:自定义窗口函数)
      .keyBy(_.windowEnd) // 打乱重新按照窗口分组，为了让同意窗口的所有数据进行并行计算
      .process(new TopNHotItems(3)) // 自定义ProcessFunction

    //## 4. sink：控制台测试输出
    processedStream.print()

    //## 5. 开始执行
    env.execute("hot items job")
  }
}

// 自定义预聚合函数 - 累加器
class CountAgg() extends AggregateFunction[UserBehavior, Long, Long] { // 【输入数据类型，累加器数据类型，输出的聚合结果传给windowfunction的类型】
  // 每来一条数据如何累加
  override def add(value: UserBehavior, accumulator: Long): Long = accumulator + 1

  // 创建累加器初始值
  override def createAccumulator(): Long = 0L

  // 获取累加器的结果
  override def getResult(accumulator: Long): Long = accumulator

  // 语句和的合并操作
  override def merge(a: Long, b: Long): Long = a + b
}

// 举例：自定义预聚合函数计算平均数
class AverageAgg() extends AggregateFunction[UserBehavior, (Long, Int), Double] {
  override def add(value: UserBehavior, accumulator: (Long, Int)): (Long, Int) = (accumulator._1 + value.timestamp, accumulator._2 + 1)

  override def createAccumulator(): (Long, Int) = (0L, 0)

  override def getResult(accumulator: (Long, Int)): Double = accumulator._1 / accumulator._2.toDouble

  override def merge(a: (Long, Int), b: (Long, Int)): (Long, Int) = (a._1 + b._1, a._2 + b._2)
}

// 自定义窗口函数，【预聚合函数的输出就是这里的输入，输出ItemViewCount，key类型，窗口类型可选择TimeWindow】
class WindowResult() extends WindowFunction[Long, ItemViewCount, Long, TimeWindow] {
  override def apply(key: Long, window: TimeWindow, input: Iterable[Long], out: Collector[ItemViewCount]): Unit = {
    out.collect(ItemViewCount(key, window.getEnd, input.iterator.next()))
  }
}

// 自定义的处理函数【key类型，输入类型，输出类型】
class TopNHotItems(topSize: Int) extends KeyedProcessFunction[Long, ItemViewCount, String] {
  // 1、定义一个列表状态，用来保存当前窗口中所有的item的count的聚合结果
  private var itemState: ListState[ItemViewCount] = _

  // 2、调用生命周期方法open，初始化列表状态值
  override def open(parameters: Configuration): Unit = {
    itemState = getRuntimeContext.getListState(new ListStateDescriptor[ItemViewCount]("item-state", classOf[ItemViewCount]))
  }

  // 3、每条数据来了后保存入状态，第一条数据来的时候，注册一个定时器，延迟一毫秒
  override def processElement(value: ItemViewCount, ctx: KeyedProcessFunction[Long, ItemViewCount, String]#Context, out: Collector[String]): Unit = {
    // 把每条数据存入状态列表
    itemState.add(value)
    // 注册一个定时器，ctx是上下文对象。
    // 正常应该判断是不是第一条数据，此处是因为所有数据的windowEnd都相同，因为定时器是用时间戳座位id的，同样的时间戳的定时器不会重复注册和触发
    ctx.timerService().registerEventTimeTimer(value.windowEnd + 1)
  }

  // 4、定时器触发时(watermark超过windowEnd，达到windowEnd+1)，对所有数据排序，并输出结果
  override def onTimer(timestamp: Long, ctx: KeyedProcessFunction[Long, ItemViewCount, String]#OnTimerContext, out: Collector[String]): Unit = {
    // 1) 将所有state中的数据取出，放到一个List Buffer中
    val allItems: ListBuffer[ItemViewCount] = new ListBuffer()
    import scala.collection.JavaConversions._
    //    val allItems: List[ItemViewCount] = itemState.get().iterator().toList
    for (item <- itemState.get()) {
      allItems += item
    }
    // 2) 按照count大小排序，并取前N个
    val sortedItems: ListBuffer[ItemViewCount] = allItems.sortBy(_.count)(Ordering.Long.reverse).take(topSize) // 函数柯里化，让排序反转

    // 3) 清空状态
    itemState.clear()

    // 4) 将排名结果格式化输出
    val result: StringBuilder = new StringBuilder()
    result.append("时间：").append(new Timestamp(timestamp - 1)).append("\n") //窗口时间我们已经+1了，记得减去1毫秒
    // 5) 输出每一个商品的信息
    for (i <- sortedItems.indices) {
      val currentItem = sortedItems(i)
      result.append("No").append(i + 1).append(":")
        .append(" 商品ID=").append(currentItem.itemId)
        .append(" 浏览量=").append(currentItem.count)
        .append("\n")
    }
    result.append("================================")
    // 控制输出频率
    Thread.sleep(1000)
    out.collect(result.toString()) // 输出结果到out中。
  }
}

