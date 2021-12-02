package flink基础

import org.apache.flink.api.common.functions.RichFlatMapFunction
import org.apache.flink.api.common.restartstrategy.RestartStrategies
import org.apache.flink.api.common.state.{ValueState, ValueStateDescriptor}
import org.apache.flink.streaming.api.scala.{DataStream, OutputTag, StreamExecutionEnvironment}
import org.apache.flink.api.scala._
import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.CheckpointingMode
import org.apache.flink.streaming.api.environment.CheckpointConfig.ExternalizedCheckpointCleanup
import org.apache.flink.streaming.api.functions.{KeyedProcessFunction, ProcessFunction}
import org.apache.flink.util.Collector

/**
  * @class 利用ProcessedFunction过程函数、状态编程、检查点
  *        解决需求1：同一设备温度再10秒内一直上升的，便报警【知识点 ProcessFunction】
  *        解决需求2：温度值低于32F的温度输出到side output【知识点 ProcessFunction + 测输出流】
  *        解决需求3：温度变化值跳跃超过10℃，进行报警(可以用processfunction可以实现，这里要求用flatmap实现)
  * @CalssName ProcessedFunction
  * @author lizhong.liu 
  * @create 2020-09-18 11:52
  * @Des TODO
  * @version TODO
  */
object O3_processfunction_state_checkpoint {
  def main(args: Array[String]): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(1)

    //// 【1】、设置检查点，默认是不开启的
    env.enableCheckpointing(60000)     //设置6秒触发一次checkpoint
    env.getCheckpointConfig.setCheckpointingMode(CheckpointingMode.AT_LEAST_ONCE)   //数据至少处理一次的状态保证，EXACTLY_ONCE是精确一次的状态保证(但是性能影响是很大的)
    env.getCheckpointConfig.setCheckpointTimeout(100000)            // 查过多上时间淘汰
    env.getCheckpointConfig.setFailOnCheckpointingErrors(false)   // 如果存盘过程中，任务报错了，那么算不算checkpoint成功呢
    //    env.getCheckpointConfig.setMaxConcurrentCheckpoints(1)  // 最多能有几个checkpoint并行执行，若下边的设置的时间逻辑上不允许这个并行度超过10，那么此处设置12也是没用的
    env.getCheckpointConfig.setMinPauseBetweenCheckpoints(100)    // 两个checkpoint最小间隔多长时间（相比触发check时间不同点在于这个时间是上一个尾和这次头的时间间隔）
    env.getCheckpointConfig.enableExternalizedCheckpoints(ExternalizedCheckpointCleanup.DELETE_ON_CANCELLATION)    //开启外部的持久化保存（任务执行完后DELETE_***是删除RETAIN_***是留着）
    //重启策略配置-checkpoint :: failureRateRestart:固定延迟时间重启( 重启次数，重启延迟总时间，重启间隔时间)
    env.setRestartStrategy(RestartStrategies.failureRateRestart(3, org.apache.flink.api.common.time.Time.seconds(300), org.apache.flink.api.common.time.Time.seconds(10)))
    env.setRestartStrategy(RestartStrategies.noRestart())    //不重启
    env.setRestartStrategy(RestartStrategies.fallBackRestart())   //回滚重启

    // 获取数据源
    val inputStream: DataStream[String] = env.socketTextStream("192.168.1.241", 7777)
    val dataStream: DataStream[SensorReading] = inputStream.map(data => {
      val dataArray: Array[String] = data.split(",")
      new SensorReading(dataArray(0).trim, dataArray(1).trim.toLong, dataArray(2).trim.toDouble)
    })
    /*
        // 需求一：
        val keyProcessStream: DataStream[String] = dataStream.keyBy(_.id).process(new MyTempincreWarning())
        keyProcessStream.print("processtest:")

        // 需求二：
        val higeTempStream: DataStream[SensorReading] = dataStream.process(new MyOutSidealert())
        higeTempStream.print("high: ")
        val lowTempStream: DataStream[String] = higeTempStream.getSideOutput(new OutputTag[String]("freezing"))
        lowTempStream.print("low: ")

        // 需求三：只输出比上一个温差大于10℃的数据
        val flapMapProcessStream: DataStream[(String, Long, Double, Double)] = dataStream.keyBy(_.id).flatMap(new TempChangeAlert(10.0))
        flapMapProcessStream.print("温差大于10℃：") //证明，这种无状态的算子也可以处理变成他又状态的算子
    */

    //需求三：相比之前实现方法不同的是可以忽略每个key的首条数据
    val flatMapWithStateStream: DataStream[(String, Long, Double, Double)] = dataStream.keyBy(_.id).flatMapWithState[(String, Long, Double, Double), Double] {
      case (input: SensorReading, None) => (List.empty, Some(input.temperature))
      case (input: SensorReading, lastTemp: Some[Double]) => {
        if ((lastTemp.get - input.temperature).abs > 10) {
          (List((input.id, input.timestamp, input.temperature, lastTemp.get)), Some(input.temperature))
        } else {
          (List.empty, Some(input.temperature))
        }
      }
    }
    flatMapWithStateStream.print("变化过快报警：")

    env.execute("process function test!!")
  }
}

// 需求一：自定义KeyedProcessFunction类，解决10秒内温度持续升高报警的需求
class MyTempincreWarning() extends KeyedProcessFunction[String, SensorReading, String] { //参数类型：[key，输入类型，输出类型]
  // 首先定义状态，用来保存上一次的温度值，以及已经核定的定时器时间戳
  private lazy val lastTempState: ValueState[Double] = getRuntimeContext.getState(new ValueStateDescriptor[Double]("last-temp", classOf[Double]))
  private lazy val currentTimerState: ValueState[Long] = getRuntimeContext.getState(new ValueStateDescriptor[Long]("last-currenttime", classOf[Long]))

  // 处理每一个元素的方法
  override def processElement(value: SensorReading, ctx: KeyedProcessFunction[String, SensorReading, String]#Context, out: Collector[String]): Unit = {
    // value：数据， ctx上下文对象， out输出。（ctx可以获取测输出、时间戳、key、还能调时间服务【时间服务能获取处理时间、watermark、还能注册定时器、还能删除定时器提醒】）
    val lastTemp: Double = lastTempState.value() //获取上次的温度值
    val lastCurrentTime: Long = currentTimerState.value() //获取上次的定时器时间戳
    lastTempState.update(value.temperature) //将状态更新为最新温度
    if (lastTemp < value.temperature && lastCurrentTime == 0) {
      val ts: Long = ctx.timerService().currentProcessingTime() + 10000L //获取此条数据的时间戳
      ctx.timerService().registerProcessingTimeTimer(ts) //注册当前key的processing time的定时器。当processing time到达定时时间时，触发timer
      currentTimerState.update(ts)
    } else if (lastTemp <= value.temperature) {
      ctx.timerService().deleteProcessingTimeTimer(currentTimerState.value())
      currentTimerState.clear()
    }
  }

  //
  override def onTimer(timestamp: Long, ctx: KeyedProcessFunction[String, SensorReading, String]#OnTimerContext, out: Collector[String]): Unit = {
    out.collect("sensor " + ctx.getCurrentKey + "温度在10秒内持续升高！！！")
    currentTimerState.clear()
  }
}

//需求二：温度值低于32F的温度输出到侧输出流
class MyOutSidealert extends ProcessFunction[SensorReading, SensorReading] {
  override def processElement(value: SensorReading, ctx: ProcessFunction[SensorReading, SensorReading]#Context, out: Collector[SensorReading]): Unit = {
    if (value.temperature < 32) {
      ctx.output(new OutputTag[String]("freezing"), "freezing alert for " + value.id)
    } else {
      out.collect(value)
    }
  }
}

//需求三：温度变化值跳跃超过10℃，进行报警
class TempChangeAlert(d: Double) extends RichFlatMapFunction[SensorReading, (String, Long, Double, Double)] {
  private var lastTempState: ValueState[Double] = _

  override def open(parameters: Configuration): Unit = {
    lastTempState = getRuntimeContext.getState(new ValueStateDescriptor[Double]("lastTemp", classOf[Double]))
  }

  override def flatMap(value: SensorReading, out: Collector[(String, Long, Double, Double)]): Unit = {
    if ((lastTempState.value() - value.temperature).abs > d) {
      out.collect((value.id, value.timestamp, value.temperature, lastTempState.value()))
    }
    lastTempState.update(value.temperature)
  }
}