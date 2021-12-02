package flink基础.电商项目

import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.scala.function.ProcessWindowFunction
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.api.windowing.triggers.{Trigger, TriggerResult}
import org.apache.flink.streaming.api.windowing.windows.TimeWindow
import org.apache.flink.util.Collector
import redis.clients.jedis.Jedis

/**
  * @class 需求3：统计指标UV，
  *        -- 1小时窗口滚动，利用布隆过滤器统计userid进行去重展示UV指标信息
  * @CalssName UvWithBloom
  * @author lizhong.liu
  * @create 2020-10-15 14:10
  * @Des TODO
  * @version TODO
  */

object UvWithBloom {
  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)
    env.setParallelism(1)

    // 用相对路径定义数据源
    val dataStream = env.readTextFile("C:\\workhouse\\LearnAccumulate\\src\\main\\java\\alltool\\flink\\电商项目\\resources\\UserBehavior.csv")
      .map(data => {
        val dataArray = data.split(",")
        UserBehavior(dataArray(0).trim.toLong, dataArray(1).trim.toLong, dataArray(2).trim.toInt, dataArray(3).trim, dataArray(4).trim.toLong)
      })
      .assignAscendingTimestamps(_.timestamp * 1000L)
      .filter(_.behavior == "pv") // 只统计pv操作
      .map(data => ("dummyKey", data.userId))
      .keyBy(_._1)
      .timeWindow(Time.hours(1))
      .trigger(new MyTrigger()) // 自己定义窗口的触发规则（原本process是所有数据都到了之后才出发计算操作，这里自定义触发规则为每条数到了都触发）
      .process(new UvCountWithBloom()) //

    dataStream.print()

    env.execute("uv with bloom job")
  }
}

// 自定义窗口触发器{ CONTINUE(false, false)什么都不做, FIRE(true, false)触发窗口计算, PURGE(false, true)清理关闭窗口, FIRE_AND_PURGE(true, true)出发计算并清理关闭窗口}
class MyTrigger() extends Trigger[(String, Long), TimeWindow] {
  // 当收到一个新的watermark时触不触发
  override def onEventTime(time: Long, window: TimeWindow, ctx: Trigger.TriggerContext): TriggerResult = TriggerResult.CONTINUE

  // 当时间改变了，触不触发操作
  override def onProcessingTime(time: Long, window: TimeWindow, ctx: Trigger.TriggerContext): TriggerResult = TriggerResult.CONTINUE

  // 最后的清理工作
  override def clear(window: TimeWindow, ctx: Trigger.TriggerContext): Unit = {}

  // 当前来了一条数据看触不触发
  override def onElement(element: (String, Long), timestamp: Long, window: TimeWindow, ctx: Trigger.TriggerContext): TriggerResult = {
    // 每来一条数据，就直接触发窗口操作，并清空所有窗口状态
    TriggerResult.FIRE_AND_PURGE
  }
}

// 定义一个数据结构（布隆过滤器）
class Bloom(size: Long) extends Serializable {
  // 位图的总大小，默认16M
  private val cap = if (size > 0) size else 1 << 27

  // 定义hash函数
  def hash(value: String, seed: Int): Long = {
    var result = 0L
    for (i <- 0 until value.length) {
      result = result * seed + value.charAt(i)
    }
    result & (cap - 1) // 位运算 - - 可使结果在cap范围内。
  }
}

// 自定义过程函数【输入类型，缓存类型，输出类型，窗口类型】
class UvCountWithBloom() extends ProcessWindowFunction[(String, Long), UvCount, String, TimeWindow] {
  // 定义redis连接
  lazy val jedis = new Jedis("10.238.251.3", 6379)
  lazy val bloom = new Bloom(1 << 29) //2^29大约5亿条数据，512M，在redis中存储大小2^29bit,64M

  override def process(key: String, context: Context, elements: Iterable[(String, Long)], out: Collector[UvCount]): Unit = {
    // 在redis里存储的位图，每个窗口都已windowEnd座位位图的key。value是bitmap
    val storeKey = context.window.getEnd.toString
    // 顶一当前窗口的uv count值。
    var count = 0L
    // 把每个窗口的uv count值也存入名为count的redis表，存放内容为（windowEnd -> uvCount），所以要先从redis中读取
    if (jedis.hget("count", storeKey) != null) {
      count = jedis.hget("count", storeKey).toLong
    }
    // 用布隆过滤器判断当前用户是否已经存在
    val userId = elements.last._2.toString // 已经做了触发处理，即elements中只有一条数据
    val offset = bloom.hash(userId, 61)
    // 定义一个标识位，判断reids位图中有没有这一位
    val isExist = jedis.getbit(storeKey, offset)
    if (!isExist) {
      jedis.setbit(storeKey, offset, true) // 如果不存在，位图对应位置1
      jedis.hset("count", storeKey, (count + 1).toString)
      out.collect(UvCount(storeKey.toLong, count + 1))
    } else {
      out.collect(UvCount(storeKey.toLong, count))
    }
  }
}

