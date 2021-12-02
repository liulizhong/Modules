package flink基础.电商项目

import java.net.URL
import java.sql.Timestamp
import java.text.SimpleDateFormat

import org.apache.flink.api.common.functions.AggregateFunction
import org.apache.flink.api.common.state.{ListState, ListStateDescriptor, MapState, MapStateDescriptor}
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.functions.KeyedProcessFunction
import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.scala.function.{AllWindowFunction, WindowFunction}
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.api.windowing.windows.TimeWindow
import org.apache.flink.util.Collector

import scala.collection.mutable.ListBuffer

/**
  * @class 需求二：实时流量统计（实现方法基本和需求已完全相同）
  *        -- 10分钟小时窗口大小滑动5秒钟，实时展示这十分钟内url点击量前五的url
  * @CalssName NetworkFlow
  * @author lizhong.liu
  * @create 2020-10-14 16:11
  * @Des TODO
  * @version TODO
  */

// 输入数据样例类
case class ApacheLogEvent(ip: String, userId: String, eventTime: Long, method: String, url: String)

// 窗口聚合结果样例类
case class UrlViewCount(url: String, windowEnd: Long, count: Long)

// 统计UV的输出数据类型
case class UvCount(windowEnd: Long, uvCount: Long)

object NetworkFlow {
  def main(args: Array[String]): Unit = {
    //## 1. 创建执行环境
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(1)
    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)

    //## 2. 读取数据 + 处理数据
    val dataStream: DataStream[UrlViewCount] = env.readTextFile("C:\\workhouse\\LearnAccumulate\\src\\main\\java\\alltool\\flink\\电商项目\\resources\\apache.log")
      //    val dataStream = env.socketTextStream("localhost", 7777)    // 读取文件是离线日志的PV，读取Kafka则是实时展示的PV。
      .map(data => {
      val dataArray = data.split(" ")
      // 定义时间转换
      val simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy:HH:mm:ss")
      val timestamp = simpleDateFormat.parse(dataArray(3).trim).getTime
      ApacheLogEvent(dataArray(0).trim, dataArray(1).trim, timestamp, dataArray(5).trim, dataArray(6).trim)
    })
      .assignTimestampsAndWatermarks(new BoundedOutOfOrdernessTimestampExtractor[ApacheLogEvent](Time.seconds(1)) { // 允许延迟时间越长，不能关闭的窗口就越多，占用资源就越大
        override def extractTimestamp(element: ApacheLogEvent): Long = element.eventTime // 数据乱序情况处理就不能直接用assignAscendingTimestamps指定watermark，可以给个1秒的允许延迟
      })
      .filter(data => {
        val pattern = "^((?!\\.(css|js)$).)*$".r //利用正则扣除css和js结尾的文件
        (pattern findFirstIn data.url).nonEmpty
      })
      .keyBy(_.url)
      .timeWindow(Time.minutes(10), Time.seconds(5))
      .allowedLateness(Time.seconds(60)) // 允许处理迟到数据（上边的watermark的1秒是延迟计算时间，没设置allowedLateness则窗口立即关闭，设置allowedLateness则继续等待60s后关闭）
      .sideOutputLateData(new OutputTag[ApacheLogEvent]("late")) // 数据延迟最后一层保障，迟到了60秒之后的数据输出到测输出流。在后边再"getSideOutPut(new xx)"取出来
      .aggregate(new UrlCountAgg(), new UrlWindowResult())
    /* // 一、PV：统计离线数据每小时的 pv 操作
    .filter( _.behavior == "pv" )
    .map( data => ("pv", 1) )
    .keyBy(_._1)
    .timeWindow(Time.hours(1))         //滚动窗口
    .sum(1)
    */
    /* // 二、UV：统计UV操作
    .filter( _.behavior == "pv" )
    .timeWindowAll( Time.hours(1) )
    .apply( new UvCountByWindow() )
    */

    val processedStream = dataStream
      .keyBy(_.windowEnd)
      .process(new TopNHotUrls(5))

    //    dataStream.print("aggregate")
    processedStream.print("process")

    env.execute("network flow job")
  }
}

// 自定义预聚合函数
class UrlCountAgg() extends AggregateFunction[ApacheLogEvent, Long, Long] {
  override def add(value: ApacheLogEvent, accumulator: Long): Long = accumulator + 1

  override def createAccumulator(): Long = 0L

  override def getResult(accumulator: Long): Long = accumulator

  override def merge(a: Long, b: Long): Long = a + b
}

// 自定义窗口处理函数
class UrlWindowResult() extends WindowFunction[Long, UrlViewCount, String, TimeWindow] {
  override def apply(key: String, window: TimeWindow, input: Iterable[Long], out: Collector[UrlViewCount]): Unit = {
    out.collect(UrlViewCount(key, window.getEnd, input.iterator.next()))
  }
}

// 自定义排序输出处理函数
class TopNHotUrls(topSize: Int) extends KeyedProcessFunction[Long, UrlViewCount, String] {
  lazy val urlState: MapState[String, Long] = getRuntimeContext.getMapState(new MapStateDescriptor[String, Long]("url-state", classOf[String], classOf[Long]))

  override def processElement(value: UrlViewCount, ctx: KeyedProcessFunction[Long, UrlViewCount, String]#Context, out: Collector[String]): Unit = { // 同需求一情况
    urlState.put(value.url, value.count)
    ctx.timerService().registerEventTimeTimer(value.windowEnd + 1)
  }

  override def onTimer(timestamp: Long, ctx: KeyedProcessFunction[Long, UrlViewCount, String]#OnTimerContext, out: Collector[String]): Unit = {
    // 从状态中拿到数据
    val allUrlViews: ListBuffer[(String, Long)] = new ListBuffer[(String, Long)]()
    val iter = urlState.entries().iterator()
    while (iter.hasNext) {
      val entry = iter.next()
      allUrlViews += ((entry.getKey, entry.getValue))
    }

    //    urlState.clear()

    val sortedUrlViews = allUrlViews.sortWith(_._2 > _._2).take(topSize)

    // 格式化结果输出
    val result: StringBuilder = new StringBuilder()
    result.append("时间：").append(new Timestamp(timestamp - 1)).append("\n")
    for (i <- sortedUrlViews.indices) {
      val currentUrlView = sortedUrlViews(i)
      result.append("NO").append(i + 1).append(":")
        .append(" URL=").append(currentUrlView._1)
        .append(" 访问量=").append(currentUrlView._2).append("\n")
    }
    result.append("=============================")
    Thread.sleep(1000)
    out.collect(result.toString())
  }
}

// 统计UV的自定义apply方法自定义函数
class UvCountByWindow() extends AllWindowFunction[UserBehavior, UvCount, TimeWindow] {
  override def apply(window: TimeWindow, input: Iterable[UserBehavior], out: Collector[UvCount]): Unit = {
    // 定义一个scala set，用于保存所有的数据userId并去重
    var idSet = Set[Long]()
    // 把当前窗口所有数据的ID收集到set中，最后输出set的大小
    for (userBehavior <- input) {
      idSet += userBehavior.userId
    }
    out.collect(UvCount(window.getEnd, idSet.size))
  }
}