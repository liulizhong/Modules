package flink基础

import java.sql.{Connection, DriverManager, PreparedStatement}
import java.time.ZoneId
import java.util
import java.util.{Calendar, Properties}

import org.apache.flink.api.common.functions.{FilterFunction, RichFilterFunction, RuntimeContext}
import org.apache.flink.api.java.ExecutionEnvironment
import org.apache.flink.api.java.tuple.Tuple
import org.apache.flink.streaming.api.scala.{ConnectedStreams, DataStream, KeyedStream, StreamExecutionEnvironment}
import org.apache.flink.api.scala._
import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.functions.sink.{RichSinkFunction, SinkFunction}
import org.apache.flink.streaming.api.functions.source.SourceFunction
import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.connectors.elasticsearch.{ElasticsearchSinkFunction, RequestIndexer}
import org.apache.flink.streaming.connectors.elasticsearch6.ElasticsearchSink
import org.apache.flink.streaming.connectors.fs.bucketing.{BasePathBucketer, BucketingSink, DateTimeBucketer}
import org.apache.flink.streaming.connectors.kafka.{FlinkKafkaConsumer, FlinkKafkaConsumer011, FlinkKafkaProducer011}
import org.apache.flink.streaming.connectors.redis.RedisSink
import org.apache.flink.streaming.connectors.redis.common.config.FlinkJedisPoolConfig
import org.apache.flink.streaming.connectors.redis.common.mapper.{RedisCommand, RedisCommandDescription, RedisMapper}
import org.apache.flink.streaming.util.serialization.SimpleStringSchema
import org.apache.http.HttpHost
import org.elasticsearch.action.index.IndexRequest
import org.elasticsearch.client.Requests

import scala.util.Random

/**
  * @class flink-api(source + 算子+ sink + 自定义UDF函数) 、 时间语义与Wartermark
  * @CalssName 标准案例1
  * @author lizhong.liu 
  * @create 2020-09-14 15:20
  * @Des TODO
  * @version TODO
  */
object O1_source_operator_sink_udf_watermark {
  def main(args: Array[String]): Unit = {
    // 【1】、获取flink的执行环境
    val executionEnvironment = ExecutionEnvironment.getExecutionEnvironment //获取批处理执行环境
    val env = StreamExecutionEnvironment.getExecutionEnvironment //★获取流处理执行环境(自动判断是本地执行环境还是集群执行环境)
    val localEnvironment = StreamExecutionEnvironment.createLocalEnvironment(1) //获取本地执行环境并且设置并行度
    val remoteEnvironment = ExecutionEnvironment.createRemoteEnvironment("jobmanage-hostname", 6123, "YOURPATH//wordcount.jar") //获取集群执行环境
    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime) // 开启时间语义(默认ProcessingTime),设置为EventTime。
    env.getConfig.setAutoWatermarkInterval(5000) // 设置周期性的生成watermark，每5秒

    // 【2】、source
    //// 1) 从集合读取数据
    val stream1: DataStream[String] = env.fromCollection(List("first", "second"))
    val stream11 = env.fromElements("a, 12345, 10.5", "b,123566, 9.5", "c,123566, 9.5", "a,123566, 9.5", "a,123566, 19.5", "b,123566, 9.5") // 比集合数据源更全面
    //    stream1.print("stream1:").setParallelism(1)
    //// 2) 文件读取数据
    val stream2 = env.readTextFile("D:\\workhouse\\LearnAccumulate\\src\\main\\java\\alltool\\flink\\wordcount\\hello.txt")
    //    stream2.print("stream2:").setParallelism(1)
    //// 3) 从kafka获取数据源
    /*    val properties = new Properties()
        properties.setProperty("bootstrap.servers", "192.168.1.101:9092")
        properties.setProperty("group.id", "consumer-group")
        properties.setProperty("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
        properties.setProperty("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
        properties.setProperty("auto.offset.reset", "latest")
        val stream3 = env.addSource(new FlinkKafkaConsumer011("sensor", new SimpleStringSchema(), properties))
        stream3.print("stream3:").setParallelism(1)*/
    //// 4) 自定义Source
    //    val stream4 = env.addSource(new MySensorSource())

    // 【3】、Transform
    //// 1) map + Watermark
    val SRsStream: DataStream[SensorReading] = stream11.map(x => {
      val datas = x.split(",")
      SensorReading(datas(0).trim, datas(1).trim.toLong, datas(2).trim.toDouble)
    })
      //.assignAscendingTimestamps(_.timestamp * 1000L) //针对有序数据情况使用Watermark
      .assignTimestampsAndWatermarks(new BoundedOutOfOrdernessTimestampExtractor[SensorReading](Time.seconds(4)) { // 对乱序数据分配时间戳和Watermark，参数是数据里最大乱序程度4秒
      override def extractTimestamp(element: SensorReading): Long = element.timestamp * 1000L
    })
    //    wordAndone.print("wordAndone:").setParallelism(1)
    //// 2) flatMap
    val wordFlatMap: DataStream[String] = stream2.flatMap(_.split(" "))
    //// 3) fliter
    val filterhell = wordFlatMap.filter(_.contains("hell"))
    //    filterhell.print("filter:")
    //// 4) KeyBy
    val keyByFlatmap: KeyedStream[SensorReading, Tuple] = SRsStream.keyBy(0) // 下标都是0开始
    //    keyByFlatmap.print("keyby")
    //// 5) 滚动聚合算子
    val keyByAndSum: DataStream[SensorReading] = keyByFlatmap.sum(2)
    //    keyByFlatmap.min(2).print("min:")
    //    keyByFlatmap.max(2).print("max:")
    //    keyByFlatmap.minBy(2).print("minBy:")
    //    keyByFlatmap.maxBy(2).print("maxBy:")
    //// 6) reduce:要求id相同的时间戳求和，分数求和。
    val reduceSen = SRsStream.keyBy("id").reduce((sen1, sen2) => SensorReading(sen1.id, sen1.timestamp + sen2.timestamp, sen1.temperature + sen2.temperature))
    //    reduceSen.print("reduce").setParallelism(1)
    //// 7) 分流： split / select
    val splitStream = SRsStream.split(sensorData => {
      if (sensorData.temperature > 10) Seq("high") else Seq("low")
    })
    val highStrem = splitStream.select("high")
    val lowStrem = splitStream.select("low")
    val allStrem = splitStream.select("high", "low")
    //// 8) 合流： Connect和 CoMap
    val yuanhigh: DataStream[(String, Double)] = highStrem.map(senData => (senData.id, senData.temperature))
    val highAndLow: ConnectedStreams[(String, Double), SensorReading] = yuanhigh.connect(lowStrem)
    val resulthighandlow: DataStream[(String, Double, String)] = highAndLow.map(
      senhigh => (senhigh._1, senhigh._2, "high"),
      senlow => (senlow.id, senlow.temperature, "low")
    )
    //    resulthighandlow.print("connect:")
    //// 9) 合流： union 要求数据流必须相同，不同于connect的是可以同时合并多条流，即可多参。
    val allNewStream: DataStream[SensorReading] = highStrem.union(lowStrem)
    allNewStream.print("union:")
    //// 10) 自定义函数类
    val filterSRsStream: DataStream[SensorReading] = SRsStream.filter(new MyFilterFunction())
    filterSRsStream.print("自定义UDF结果：")

    // 【4】、Sink（转化为基本类型的数据流才能写）
    //// 1) Kafka Sink
    stream1.addSink(new FlinkKafkaProducer011[String]("localhost:9092", "test", new SimpleStringSchema()))
    //// 2) Redis Sink
    val conf = new FlinkJedisPoolConfig.Builder().setHost("localhost").setPort(6379).build()
    SRsStream.addSink(new RedisSink[SensorReading](conf, new MyRedisMapper))
    //// 3) ES Sink
    val httpHosts = new util.ArrayList[HttpHost]()
    httpHosts.add(new HttpHost("localhost", 9200))

    val esSinkBuilder = new ElasticsearchSink.Builder[SensorReading](httpHosts, new ElasticsearchSinkFunction[SensorReading] {
      override def process(t: SensorReading, runtimeContext: RuntimeContext, requestIndexer: RequestIndexer): Unit = {
        println("saving data: " + t)
        val json = new util.HashMap[String, String]()
        json.put("data", t.toString)
        val indexRequest: IndexRequest = Requests.indexRequest().index("sensor").`type`("readingData").source(json)
        requestIndexer.add(indexRequest)
        println("saved successfully")
      }
    })
    SRsStream.addSink(esSinkBuilder.build())
    //// 4) JDBC Sink
    SRsStream.addSink(new MyJdbcSink())
    //// 5) HDFS Sink
    // 根据日历动态创建文件夹
    val calendar: Calendar = Calendar.getInstance
    // 定义输出sink
    val hdfs_sink: BucketingSink[String] = new BucketingSink[String]("hdfs://192.168.1.101:8020///opc/" + calendar.get(Calendar.YEAR) + "/" + (calendar.get(Calendar.MONTH) + 1) + "/")
    // 文件达到50G或该文件最后一条数据是2小时之前，写入另一个文件
    hdfs_sink.setBatchSize(1024 * 1024 * 1024 * 50L)
    hdfs_sink.setBatchRolloverInterval(120 * 60 * 1000L)
    hdfs_sink.setBucketer(new DateTimeBucketer[String]("yyyy-MM-dd", ZoneId.of("Asia/Shanghai")))
    hdfs_sink.setBucketer(new BasePathBucketer[String])
    // hdfs_sink.setPartPrefix("part");
    hdfs_sink.setPartSuffix(".txt")
    // 存到hdfs
    stream1.addSink(hdfs_sink)

    // 【5】、开启任务
    env.execute()
  }
}

// 自定义source
class MySensorSource extends SourceFunction[SensorReading]() {
  var running: Boolean = true

  override def cancel(): Unit = {
    running = false
  }

  // 通过上下文对象sourceContext发出数据，里边主要是sourceContext.collect()方法
  override def run(sourceContext: SourceFunction.SourceContext[SensorReading]): Unit = {
    // 初始化一个随机数发生器
    val rand: Random = new Random()

    var curTemp: Seq[(String, Double)] = 1.to(10).map(
      i => ("sensor_" + i, 65 + rand.nextGaussian() * 20)
    )
    while (running) {
      // 更新温度值
      curTemp = curTemp.map(
        t => (t._1, t._2 + rand.nextGaussian())
      )
      // 获取当前时间戳
      val curTime: Long = System.currentTimeMillis()
      curTemp.foreach(
        t => sourceContext.collect(SensorReading(t._1, curTime, t._2))
      )
      Thread.sleep(100)
    }
  }
}

// 样例类
case class SensorReading(id: String, timestamp: Long, temperature: Double)

//自定义UDF函数(RichFilterFunction比FilterFunction多了几个方法如获取上下文对象及关闭方法)
class MyFilterFunction() extends RichFilterFunction[SensorReading]() {
  override def filter(t: SensorReading): Boolean = {
    t.id.contains("c")
  }
}

//flink-RedisSink
class MyRedisMapper extends RedisMapper[SensorReading] {
  override def getCommandDescription: RedisCommandDescription = {
    new RedisCommandDescription(RedisCommand.HSET, "sensor_temperature")
  }

  override def getValueFromData(t: SensorReading): String = t.temperature.toString

  override def getKeyFromData(t: SensorReading): String = t.id
}

//flink-JDBCSink
class MyJdbcSink() extends RichSinkFunction[SensorReading] {
  var conn: Connection = _
  var insertStmt: PreparedStatement = _
  var updateStmt: PreparedStatement = _

  // open 主要是创建连接
  override def open(parameters: Configuration): Unit = {
    super.open(parameters)
    conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "123456")
    insertStmt = conn.prepareStatement("INSERT INTO temperatures (sensor, temp) VALUES (?, ?)")
    updateStmt = conn.prepareStatement("UPDATE temperatures SET temp = ? WHERE sensor = ?")
  }

  // 调用连接，执行sql
  override def invoke(value: SensorReading, context: SinkFunction.Context[_]): Unit = {
    updateStmt.setDouble(1, value.temperature)
    updateStmt.setString(2, value.id)
    updateStmt.execute()
    if (updateStmt.getUpdateCount == 0) {
      insertStmt.setString(1, value.id)
      insertStmt.setDouble(2, value.temperature)
      insertStmt.execute()
    }
  }

  override def close(): Unit = {
    insertStmt.close()
    updateStmt.close()
    conn.close()
  }
}
