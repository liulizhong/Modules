//package 项目二实时项目.gmall2020-realtime.src.main.scala.xuexi.utils

import java.io.InputStreamReader
import java.nio.charset.StandardCharsets
import java.util.Properties

/**
  * Author: lizhong.liu
  * Date: 2020/9/12
  * Desc: 读取配置文件的工具类
  */
object MyPropertiesUtil {
  def load(propertiesName: String): Properties = {
    val prop: Properties = new Properties()
    prop.load(new InputStreamReader(
      Thread.currentThread().getContextClassLoader.getResourceAsStream(propertiesName), StandardCharsets.UTF_8))
    prop
  }

  def main(args: Array[String]): Unit = {
    val prop: Properties = MyPropertiesUtil.load("config.properties")
    val str: String = prop.getProperty("kafka.broker.list")
    println(str)
  }
}
