//package 项目二实时项目.gmall2020-realtime.src.main.scala.xuexi.utils

import java.sql.{Connection, DriverManager, ResultSet, ResultSetMetaData, Statement}
import com.alibaba.fastjson.JSONObject
import scala.collection.mutable.ListBuffer

/**
  * Desc: 从Phoenix中查询[Hbase]数据的工具类
  */
object PhoenixUtil {
  def main(args: Array[String]): Unit = {
    val list: List[JSONObject] = queryList("select * from user_status0421")
    println(list.size)
  }

  def queryList(sql: String): List[JSONObject] = {
    val rsList: ListBuffer[JSONObject] = new ListBuffer[JSONObject]()
    //注册驱动
    Class.forName("org.apache.phoenix.jdbc.PhoenixDriver")
    //创建连接
    val conn: Connection = DriverManager.getConnection("jdbc:phoenix:hadoop202,hadoop203,hadoop204:2181")
    //创建数据库操作对象
    val st: Statement = conn.createStatement()
    //执行SQL语句
    val rs: ResultSet = st.executeQuery(sql)
    val metaData: ResultSetMetaData = rs.getMetaData
    //处理结果集
    while (rs.next) {
      val rowData = new JSONObject()
      //获取列名,获取列的数量，进行循环，从第一列开始
      for (i <- 1 to metaData.getColumnCount) {
        rowData.put(metaData.getColumnName(i), rs.getString(i))
      }
      //将当前行的数据放到List集合中
      rsList.append(rowData)
    }
    //释放资源
    rs.close()
    st.close()
    conn.close()
    rsList.toList
  }
}
