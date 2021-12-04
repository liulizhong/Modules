package core

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
  * @class 方法和属性序列化问题，解决driver的tasks在executer执行对象序列化问题
  * @CalssName 方法和属性的序列化问题
  * @author lizhong.liu
  * @create 2020-06-30 14:39
  * @Des TODO
  * @version TODO
  */
object TestSparkSearch {
  def main(args: Array[String]): Unit = {
    //1.初始化配置信息及SparkContext
    val sparkConf: SparkConf = new SparkConf().setAppName("WordCount").setMaster("local[*]")
    val sc = new SparkContext(sparkConf)

    //2.创建一个RDD(集合产生)
    val rdd: RDD[String] = sc.parallelize(Array("hadoop", "spark", "hive", "atguigu"))

    val search = new Search("h")

    // 3、Search类若没序列化，那方法getMatch1里有调用this.isMatch，调用时用的this便是Search对象，报错。
    val resultRDD: RDD[String] = search.getMatch1(rdd)
    resultRDD.foreach(println);

    // 4、Search类若没序列化，getMatch2方法里有用到this.query，同样是this需序列化问题。
    //    解决方法二：方法中把类变量赋值给局部变量：val query_ : String = this.query
    val match1: RDD[String] = search.getMatch2(rdd)
    match1.collect().foreach(println)

    sc.stop()
  }
}

class Search(query: String) extends Serializable {

  //过滤出包含字符串的数据
  def isMatch(s: String): Boolean = {
    s.contains(query)
  }

  //过滤出包含字符串的RDD
  def getMatch1(rdd: RDD[String]): RDD[String] = {
    rdd.filter(isMatch)
  }

  //过滤出包含字符串的RDD
  def getMatch2(rdd: RDD[String]): RDD[String] = {
    //val query_ : String = this.query//将类变量赋值给局部变量
    rdd.filter(x => x.contains(query))
  }

}

