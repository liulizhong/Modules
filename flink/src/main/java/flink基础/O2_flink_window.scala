package flink基础

import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment, WindowedStream}
import org.apache.flink.api.scala._
import org.apache.flink.streaming.api.windowing.assigners.EventTimeSessionWindows
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.api.windowing.windows.{GlobalWindow, TimeWindow}

/**
  * @class flink的各类窗口函数
  * @CalssName Flink_Window
  * @author lizhong.liu 
  * @create 2020-09-16 10:40
  * @Des TODO
  * @version TODO
  */
object O2_flink_window {
  def main(args: Array[String]): Unit = {
    //【0】环境及数据准备
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(1)
    val streamFromFile: DataStream[String] = env.fromElements("a, 12345, 10.5", "b,123566, 8.5", "c,123566, 9.5", "a,123566, 9.5", "a,123566, 19.5", "b,123566, 9.5") // 比集合数据源更全面
    val dataStream: DataStream[SensorReading] = streamFromFile.map(x => {
      val datas = x.split(",")
      SensorReading(datas(0).trim, datas(1).trim.toLong, datas(2).trim.toDouble)
    })
    // 【1】滚动时间窗口(窗口大小15秒)
    val timeWindow: WindowedStream[SensorReading, String, TimeWindow] = dataStream.keyBy(_.id).timeWindow(Time.seconds(15))
    val timeWindowReducer: DataStream[SensorReading] = timeWindow.reduce((s1,s2)=>new SensorReading(s1.id,s2.timestamp,s1.temperature.min(s2.timestamp))) //计算15秒内温度最小值
    timeWindowReducer.print("最小值：")
    // 【2】滑动时间窗口(窗口大小15秒，步长5秒)
    val timeWindowPace: WindowedStream[SensorReading, String, TimeWindow] = dataStream.keyBy(_.id).timeWindow(Time.seconds(15),Time.seconds(5))
    // 【3】会话窗口(会话间隙时长3分钟)
    val sessionWindow: WindowedStream[SensorReading, String, TimeWindow] = dataStream.keyBy(_.id).window(EventTimeSessionWindows.withGap(Time.minutes(3)))
    // 【4】滚动计数窗口(窗口滚动大小10条)
    val countWindow: WindowedStream[SensorReading, String, GlobalWindow] = dataStream.keyBy(_.id).countWindow(10)
    // 【5】滑动计数窗口(窗口滚动大小10条，步长2条)
    val countWindowPace: WindowedStream[SensorReading, String, GlobalWindow] = dataStream.keyBy(_.id).countWindow(10,2)


    env.execute()
  }
}
