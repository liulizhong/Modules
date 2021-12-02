package flink基础.wordcount

import org.apache.flink.api.java.utils.ParameterTool
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}

/**
  * @class ??
  * @CalssName StreamWC
  * @author lizhong.liu 
  * @create 2020-09-04 8:57
  * @Des TODO
  * @version TODO
  */
object StreamWC {
  def main(args: Array[String]): Unit = {
//    val params: ParameterTool =  ParameterTool.fromArgs(args)
//    val host: String = params.get("host")
//    val port: Int = params.getInt("port")
    // 1、创建流处理环境
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
    // 2、接收socket文本流
    // val textDstream: DataStream[String] = env.socketTextStream(host, port)
    val textDstream: DataStream[String] = env.socketTextStream("192.168.1.101", 19999)
    // 3、flatMap和Map需要引用的隐式转换
    import org.apache.flink.api.scala._
    val dataStream: DataStream[(String, Int)] = textDstream.flatMap(_.split(" ")).map( (_, 1) ).keyBy(0).sum(1)
    dataStream.print().setParallelism(1)  // 3> (hello,1) ：输出结果的“3>”代表哪个分区计算的，根绝key的hash算出来的，可指定setParallelism(1)设置并行度(即分区数)。
    env.execute("WC")
  }
}
