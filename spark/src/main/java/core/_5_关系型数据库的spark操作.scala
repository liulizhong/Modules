package core

import java.sql.{Connection, DriverManager}
import org.apache.spark.rdd.JdbcRDD
import org.apache.spark.{SparkConf, SparkContext}

/**
  * @class 用spark连接关系型数据库jdbc方式
  * @CalssName _5_关系型数据库的spark操作
  * @author lizhong.liu 
  * @create 2020-06-30 16:12
  * @Des TODO
  * @version TODO
  */
object TestJDBCRDD {

  def main(args: Array[String]): Unit = {
    // 1、准备Spark配置信息
    val conf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("Test")
    val sc: SparkContext = new SparkContext(conf)

    // 2、JDBC配置信息
    val driver = "com.mysql.jdbc.Driver"
    val url = "jdbc:mysql://linux1:3306/company"
    val userName = "root"
    val passWd = "000000"
    // 3、sql语句
    val sql = "select * from staff where id >= ? and id <= ?"

    // 4、构建JDBCRDD（SparkContext，getConnection，sql，lowerBound，upperBound，numPartitions，mapRow）
    //                getConnection ：无参函数，获取mysql连接
    //                sql           ：要执行的sql语句
    //                lowerBound    ：下限(sql语句中的？参数)
    //                upperBound    ：上限(sql语句中的？参数)
    //                numPartitions ：分区数(上下限数据除以分区数)
    //                mapRow        ：返回的每条数据做什么处理生成RDD
    val jdbcRDD: JdbcRDD[(Int, String, Int)] = new JdbcRDD(
      sc,
      () => {
        Class.forName(driver)
        DriverManager.getConnection(url, userName, passWd)
      },
      sql,
      1,
      10,
      1,
      (rs) => {
        (rs.getInt(1), rs.getString(2), rs.getInt(3))
      }
    )
    jdbcRDD.foreach(println)

    // 5、向mysql中插入数据，用foreachPartition是为了让每个分区的executer只生成一个连接，用foreach的话每条数据都产生一个连接，其他的都是传统的jdbc操作
    sc.makeRDD(Array((1, "lisi", 50), (2, "wangwu", 30), (3, "zhaoliu", 20))).foreachPartition(t => {
      // JDBC
      // Connection
      val conn: Connection = DriverManager.getConnection(url, userName, passWd)
      // Statement
      // sql : insert into staff () values ()
      // stat.setInt(1)
      // close
    })

    // 6、释放资源
    sc.stop()
  }
}

