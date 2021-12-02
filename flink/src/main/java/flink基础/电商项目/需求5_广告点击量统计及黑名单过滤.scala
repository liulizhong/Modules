package flink基础.电商项目

import java.sql.Timestamp

import org.apache.flink.api.common.functions.{AggregateFunction, RichFilterFunction}
import org.apache.flink.api.common.state.{ValueState, ValueStateDescriptor}
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.functions.KeyedProcessFunction
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.scala.function.WindowFunction
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.api.windowing.windows.TimeWindow
import org.apache.flink.util.Collector

/**
  * @class 需求5：页面广告点击量统计
  *        -- 1小时窗口大小滑动10秒，实时统计每种渠道下每种点击行为的 汇总量信息
  *        -- 过滤掉用户单天点击单个广告次数超过100次的用户加入黑名单，输出到测输出流。
  * @CalssName AdStatisticsByGeo
  * @author lizhong.liu
  * @create 2020-10-15 17:28
  * @Des TODO
  * @version TODO
  */

// 输入log数据样例类
case class AdClickLog(userId: Long, adId: Long, province: String, city: String, timestamp: Long)

// 输入的广告点击事件样例类
case class AdClickEvent(userId: Long, adId: Long, province: String, city: String, timestamp: Long)

// 按照省份统计的输出结果样例类
case class CountByProvince(windowEnd: String, province: String, count: Long)

// 输出的黑名单报警信息
case class BlackListWarning(userId: Long, adId: Long, msg: String)

object AdStatisticsByGeo {
  // 定义侧输出流的tag
  val blackListOutputTag: OutputTag[BlackListWarning] = new OutputTag[BlackListWarning]("blackList")

  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)
    env.setParallelism(1)

    // 读取数据并转换成AdClickEvent
    val adEventStream = env.readTextFile("C:\\workhouse\\LearnAccumulate\\src\\main\\java\\alltool\\flink\\电商项目\\resources\\AdClickLog.csv")
      .map(data => {
        val dataArray = data.split(",")
        AdClickEvent(dataArray(0).trim.toLong, dataArray(1).trim.toLong, dataArray(2).trim, dataArray(3).trim, dataArray(4).trim.toLong)
      })
      .assignAscendingTimestamps(_.timestamp * 1000L)

    // 自定义process function，过滤大量刷点击的行为
    val filterBlackListStream = adEventStream
      .keyBy(data => (data.userId, data.adId))
      //      .filter(new MyFilter(100))
      .process(new FilterBlackListUser(100)) // 当前用户保存状态，累加次数超过阈值后就会进行过滤

    // 根据省份做分组，开窗聚合
    val adCountStream = filterBlackListStream
      .keyBy(_.province)
      .timeWindow(Time.hours(1), Time.seconds(5))
      .aggregate(new AdCountAggSheng(), new AdCountResultSheng())

    adCountStream.print("count")
    filterBlackListStream.getSideOutput(blackListOutputTag).print("blacklist")

    env.execute("ad statistics job")
  }

  class FilterBlackListUser(maxCount: Int) extends KeyedProcessFunction[(Long, Long), AdClickEvent, AdClickEvent] {
    // 定义状态，保存当前用户对当前广告的点击量
    lazy val countState: ValueState[Long] = getRuntimeContext.getState(new ValueStateDescriptor[Long]("count-state", classOf[Long]))
    // 标级当前用户时候已经进入黑名单
    lazy val isSentBlackList: ValueState[Boolean] = getRuntimeContext.getState(new ValueStateDescriptor[Boolean]("issent-state", classOf[Boolean]))
    // 保存定时器触发的时间戳
    lazy val resetTimer: ValueState[Long] = getRuntimeContext.getState(new ValueStateDescriptor[Long]("resettime-state", classOf[Long]))

    override def processElement(value: AdClickEvent, ctx: KeyedProcessFunction[(Long, Long), AdClickEvent, AdClickEvent]#Context, out: Collector[AdClickEvent]): Unit = {
      // 取出count状态
      val curCount = countState.value()

      // 如果是第一次处理，注册定时器，每天00：00触发
      if (curCount == 0) {
        val ts = (ctx.timerService().currentProcessingTime() / (1000 * 60 * 60 * 24) + 1) * (1000 * 60 * 60 * 24) // 获取当前时间天数的整数倍毫秒，再加上一天的时间即明天零点
        resetTimer.update(ts)
        ctx.timerService().registerProcessingTimeTimer(ts) // 注册定时器，指定触发时间
      }

      // 判断计数是否达到上限，如果到达则加入黑名单，测输出流输出报警
      if (curCount >= maxCount) {
        // 判断是否发送过黑名单，只发送一次
        if (!isSentBlackList.value()) {
          isSentBlackList.update(true)
          // 输出到侧输出流
          ctx.output(blackListOutputTag, BlackListWarning(value.userId, value.adId, "Click over " + maxCount + " times today."))
        }
        return
      }
      // 计数状态加1，输出数据到主流
      countState.update(curCount + 1)
      out.collect(value)
    }

    override def onTimer(timestamp: Long, ctx: KeyedProcessFunction[(Long, Long), AdClickEvent, AdClickEvent]#OnTimerContext, out: Collector[AdClickEvent]): Unit = {
      // 定时器触发时，清空状态
      if (timestamp == resetTimer.value()) {
        isSentBlackList.clear()
        countState.clear()
        resetTimer.clear()
      }
    }
  }

}

// 自定义预聚合函数
class AdCountAggSheng() extends AggregateFunction[AdClickEvent, Long, Long] {
  override def add(value: AdClickEvent, accumulator: Long): Long = accumulator + 1

  override def createAccumulator(): Long = 0L

  override def getResult(accumulator: Long): Long = accumulator

  override def merge(a: Long, b: Long): Long = a + b
}

// 自定义窗口处理函数
class AdCountResultSheng() extends WindowFunction[Long, CountByProvince, String, TimeWindow] {
  override def apply(key: String, window: TimeWindow, input: Iterable[Long], out: Collector[CountByProvince]): Unit = {
    out.collect(CountByProvince(new Timestamp(window.getEnd).toString, key, input.iterator.next()))
  }
}

class MyFilter(maxCount: Int) extends RichFilterFunction[AdClickLog] {
  lazy val countState: ValueState[Long] = getRuntimeContext.getState(new ValueStateDescriptor[Long]("count", classOf[Long]))

  override def filter(value: AdClickLog): Boolean = {
    val curCount = countState.value()
    if (curCount >= maxCount) {
      false
    } else {
      countState.update(curCount + 1)
      true
    }
  }

  override def close(): Unit = {
    countState.clear()
  }
}