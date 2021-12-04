//package 项目二实时项目.gmall2020-realtime.src.main.scala.xuexi.utils

import java.sql.{Connection, DriverManager, ResultSet, ResultSetMetaData, Statement}

import com.alibaba.fastjson.JSONObject

import scala.collection.mutable.ListBuffer

/**
  * Author: lizhong.liu
  * Date: 2020/9/23
  * Desc: 从MySQL中获取数据的工具类
  */
object MySQLUtil {
  def main(args: Array[String]): Unit = {
    val list:  List[JSONObject] = queryList("select * from offset_0421")
    println(list)
  }

  def queryList(sql:String):List[JSONObject]={
    Class.forName("com.mysql.jdbc.Driver")
    val resultList: ListBuffer[JSONObject] = new  ListBuffer[ JSONObject]()
    val conn: Connection = DriverManager.getConnection(
      "jdbc:mysql://hadoop202:3306/gmall0421_rs?characterEncoding=utf-8&useSSL=false",
      "root",
      "123456")
    val stat: Statement = conn.createStatement
    //println(sql)
    val rs: ResultSet = stat.executeQuery(sql)
    val md: ResultSetMetaData = rs.getMetaData
    while (  rs.next ) {
      val rowData = new JSONObject();
      for (i  <-1 to md.getColumnCount  ) {
        rowData.put(md.getColumnName(i), rs.getObject(i))
      }
      resultList+=rowData
    }

    stat.close()
    conn.close()
    resultList.toList
  }

}

