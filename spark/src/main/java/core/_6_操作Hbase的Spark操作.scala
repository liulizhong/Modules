package core

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.hbase.{CellUtil, HBaseConfiguration}
import org.apache.hadoop.hbase.client.{Put, Result}
import org.apache.hadoop.hbase.io.ImmutableBytesWritable
import org.apache.hadoop.hbase.mapred.TableOutputFormat
import org.apache.hadoop.hbase.mapreduce.TableInputFormat
import org.apache.hadoop.hbase.util.Bytes
import org.apache.hadoop.mapred.JobConf
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
  * @class 用spark连接hbaes操作
  * @CalssName _6_操作Hbase的Spark操作
  * @author lizhong.liu 
  * @create 2020-06-30 16:27
  * @Des TODO
  * @version TODO
  */
object TestHbaseRDD {
  def main(args: Array[String]): Unit = {
    // 1、准备Spark配置信息
    val conf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("Test")
    val sc: SparkContext = new SparkContext(conf)
    // 使用新API获取RDD，访问Hbase
    // 2、获取Hadoop配置信息（把hbase-site.xml文件放入resources里即可）
    val configuration: Configuration = HBaseConfiguration.create()

    // 3、告诉spark我要读哪张表
    configuration.set(TableInputFormat.INPUT_TABLE, "student")

    // 4.1、构建读取hbase的RDD
    val hbaseRDDs: RDD[(ImmutableBytesWritable, Result)] = sc.newAPIHadoopRDD(
      configuration,
      classOf[TableInputFormat],
      classOf[ImmutableBytesWritable],
      classOf[Result]
    )
    // 4.2、读取数据
    hbaseRDDs.foreach(t => {
      val rowkey = t._1
      val row = t._2
      row.rawCells().foreach(cell => {
        println(Bytes.toString(CellUtil.cloneValue(cell)))
      })
    })

    // 5.1、向hbase中写数据
    val rdd = sc.makeRDD(Array(("1003", "wangwu")))

    // 5.2、RDD修改为指定格式(new ImmutableBytesWritable(rowkey), put)
    val hbaseRDD: RDD[(ImmutableBytesWritable, Put)] = rdd.map(t => {
      val rowkey = Bytes.toBytes(t._1)
      val put: Put = new Put(rowkey)
      put.addColumn(Bytes.toBytes("info"), Bytes.toBytes("name"), Bytes.toBytes(t._2))
      (new ImmutableBytesWritable(rowkey), put)
    })

    // 5.3、创建任务写入数据
    val cfg = new JobConf(configuration)
    cfg.setOutputFormat(classOf[TableOutputFormat])
    cfg.set(TableOutputFormat.OUTPUT_TABLE, "student")

    hbaseRDD.saveAsHadoopDataset(cfg)


    sc.stop
  }
}
