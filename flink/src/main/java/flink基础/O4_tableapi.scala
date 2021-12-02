package flink基础

import org.apache.flink.api.scala._
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.table.api.scala.{StreamTableEnvironment, _}
import org.apache.flink.table.api.{Table, TableEnvironment}

/**
  * @class 针对文件数据进行Flink-Table-API的普通操作和开窗操作
  * @CalssName O4_tableapi
  * @author lizhong.liu
  * @create 2020-09-24 17:03
  * @Des TODO
  * @version TODO
  */
object O4_tableapi {
  def main(args: Array[String]): Unit = {
    // 【1】、创建环境，得到流数据
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    env.setParallelism(1)
    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)
    val inputStream: DataStream[String] = env.readTextFile("C:\\workhouse\\LearnAccumulate\\src\\main\\java\\alltool\\flink\\wordcount\\sensor.txt")
    val dataStream: DataStream[SensorReading] = inputStream.map(x => {
      val datas = x.split(",")
      SensorReading(datas(0).trim, datas(1).trim.toLong, datas(2).trim.toDouble)
    })
    // 【2】、创建table的env环境
    val senTableEnv: StreamTableEnvironment = TableEnvironment.getTableEnvironment(env)
    // 【3】、根据样例类自动生成表
    val senTable: Table = senTableEnv.fromDataStream(dataStream)
    // 【4】、查询数据
    val selectTable: Table = senTable.select('id, 'temperature).filter("id = 'sensor_1'")
    val selectStream: DataStream[(String, Double)] = selectTable.toAppendStream[(String, Double)]
    selectStream.print("筛选结果：")

    // 【5】、创建带有时间语义的数据流
    val dataWaterMarkStream: DataStream[SensorReading] = inputStream.map(x => {
      val datas = x.split(",")
      SensorReading(datas(0).trim, datas(1).trim.toLong, datas(2).trim.toDouble)
    }).assignTimestampsAndWatermarks(new BoundedOutOfOrdernessTimestampExtractor[SensorReading](Time.seconds(4)) {
      override def extractTimestamp(t: SensorReading): Long = t.timestamp * 1000
    })
    // 【6】、从一条数据流创建一张表，按照字段定义，并指定事件时间的时间字段
    val dataWatermarkTable: Table = senTableEnv.fromDataStream(dataWaterMarkStream, 'id, 'temperature, 'ts.rowtime)
    // 【7】、对table进行开窗，并指定窗口大小15秒、时间字段、窗口别名。
    val selectStream2: Table = dataWatermarkTable.window(Tumble over 15.seconds on 'ts as 'tw).groupBy('id, 'tw).select('id, 'id.count)
    val resultStream: DataStream[(Boolean, (String, Long))] = selectStream2.toRetractStream[(String, Long)]
    resultStream.filter(_._1).print("开窗后：")

    // 8、对Table表进行直接sql操作，表名就是获取的‘流表’
    val sqlTable: Table = senTableEnv.sqlQuery("select id,count(*) from " + dataWatermarkTable + " group by id,tumble(ts,interval '15' second)")
    val sqlResult: DataStream[(String, Long)] = sqlTable.toAppendStream[(String, Long)]
    sqlResult.print("sql执行：")

    env.execute("table test")
  }
}