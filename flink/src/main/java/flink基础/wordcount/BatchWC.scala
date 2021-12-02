package flink基础.wordcount

import org.apache.flink.api.scala.{DataSet, ExecutionEnvironment}
import org.apache.flink.streaming.api.scala._

/**
  * @class fink的word count批处理模式
  * @CalssName BatchWC
  * @author lizhong.liu 
  * @create 2020-09-04 8:33
  * @Des TODO
  * @version TODO
  */
object BatchWC {
  def main(args: Array[String]): Unit = {
    // 1、创建一个批处理的执行环境
    val env: ExecutionEnvironment = ExecutionEnvironment.getExecutionEnvironment
    // 2、从文件中读取数据
    val inputPath: String = "D:\\workhouse\\LearnAccumulate\\src\\main\\java\\alltool\\flink\\wordcount\\hello.txt"
    val inputDataSet:DataSet[String] = env.readTextFile(inputPath)
    // 3、分词之后做count
    val wordCountDataSet = inputDataSet
      .flatMap(_.split(" "))
      .map( (_, 1) )
      .groupBy(0)
      .sum(1)

    // 打印输出
    wordCountDataSet.print()
  }
}
