package 所有方法

import io.searchbox.client.config.HttpClientConfig
import io.searchbox.client.{JestClient, JestClientFactory}
import io.searchbox.core._
import java.util

import org.elasticsearch.index.query.{BoolQueryBuilder, MatchQueryBuilder, TermQueryBuilder}
import org.elasticsearch.search.builder.SearchSourceBuilder
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder
import org.elasticsearch.search.sort.SortOrder

/**
  * Author: lizhong.liu
  * Date: 2020/9/11
  * Desc: 操作ES的工具类
  */
object MyESUtil {
  // 1、声明JestClient客户端工厂
  private var factory: JestClientFactory = null

  // 2、获取客户端的方法
  def getClient(): JestClient = {
    if (factory == null) {
      build()
    }
    factory.getObject
  }

  // 3、创建JestClient客户端工厂
  def build(): Unit = {
    factory = new JestClientFactory
    factory.setHttpClientConfig(
      new HttpClientConfig
      .Builder("http://192.168.1.101:9200")
        .maxTotalConnection(10) //最大连接数设置
        .connTimeout(10000) //连接超时时间
        .readTimeout(1000) //读数超时事件
        .multiThreaded(true) //允许多线程
        .build()
    )
  }

  // 4-1、向ES的Index中插入Document 方式1：这种方式可读性极差，建立用方法2
  def putIndexOld(): Unit = {
    //获取客户端连接
    val jestClient = getClient()
    var source: String =
      """
        {
        |    "id":222,
        |    "name":"湄公河行动",
        |    "doubanScore":8.0,
        |    "actorList":[
        |    {"id":3,"name":"张涵予"}
        |    ]
        |  }
      """.stripMargin
    val index: Index = new Index.Builder(source) //为下边执行方法船舰参数对象，source为string类型的一条文档
      .index("movie_index_5") //指定文档的index
      .`type`("movie") //指定文档的type
      .id("1") //指定文档的id
      .build()

    //执行方法
    jestClient.execute(index)
    //关闭连接
    jestClient.close()
  }

  // 4-2、向ES的Index中插入Document方式2：封装样例类存放文档信息
  def putIndex(): Unit = {
    //建立连接
    val jestClient = getClient()
    val actorList = new util.ArrayList[util.Map[String, Any]]() //必须用Java的集合类型来封装对象，因为es是用java写的，若用scala的话不会拨错但是会数据无效
    val actorMap: util.HashMap[String, Any] = new util.HashMap[String, Any]()
    actorMap.put("id", 10)
    actorMap.put("name", "黄日华")
    actorList.add(actorMap)
    //底层会将source转换为json格式字符串进行操作
    val index: Index = new Index.Builder(
      Movie(111, "天龙八部", 8.5, actorList)
    )
      .index("movie_index_5")
      .`type`("movie")
      .id("2")
      .build()
    //执行操作
    jestClient.execute(index)
    //关闭连接
    jestClient.close()
  }

  // 5-1、根据文档id从ES索引中查询数据：“GET /movie_index_5/movie/1”
  def queryIndexById(): Unit = {
    val jestClient = getClient()
    val get: Get = new Get.Builder("movie_index_5", "1").build()
    val result: DocumentResult = jestClient.execute(get)
    println(result.getJsonString)
    jestClient.close()
  }

  // 5-2、按照查询条件从ES中查询文档数据 方式1：原装语句拿过来string运行
  def queryIndexOld(): Unit = {
    val jestClient = getClient()
    var queryStr =
      """
        |{
        |  "query": {
        |    "bool": {
        |       "must": [
        |        {"match": {
        |          "name": "red"
        |        }}
        |      ],
        |      "filter": [
        |        {"term": { "actorList.name.keyword": "zhang han yu"}}
        |      ]
        |    }
        |  },
        |  "from": 0,
        |  "size": 20,
        |  "sort": [
        |    {
        |      "doubanScore": {
        |        "order": "desc"
        |      }
        |    }
        |  ],
        |  "highlight": {
        |    "fields": {
        |      "name": {}
        |    }
        |  }
        |}
      """.stripMargin
    val search: Search = new Search.Builder(queryStr).addIndex("movie_index").build()
    //执行查询操作
    val result: SearchResult = jestClient.execute(search)
    //获取命中的查询结果
    val hitList: util.List[SearchResult#Hit[util.Map[String, Any], Void]] = result.getHits(classOf[util.Map[String, Any]])
    //将java集合转换为scala
    import scala.collection.JavaConverters._
    println(hitList.asScala.map(_.source).toList.mkString("\n"))
    jestClient.close()
  }

  // 5-3、按照查询条件从ES中查询文档数据 方式2：面向对象的方式
  def queryIndex(): Unit = {
    //获取连接
    val jestClient = getClient()

    //通过一个类SearchSourceBuilder，构建查询字符串
    val sourceBuilder = new SearchSourceBuilder
    val boolQueryBuilder = new BoolQueryBuilder() //通过一个类BoolQueryBuilder，构建query语句
    boolQueryBuilder.must(new MatchQueryBuilder("name", "red"))
    boolQueryBuilder.filter(new TermQueryBuilder("actorList.name.keyword", "zhang han yu"))
    sourceBuilder.query(boolQueryBuilder)
    sourceBuilder.from(0)
    sourceBuilder.size(10)
    sourceBuilder.sort("doubanScore", SortOrder.DESC)
    sourceBuilder.highlighter(new HighlightBuilder().field("name"))
    var queryStr: String = sourceBuilder.toString
    //println(queryStr)
    val search: Search = new Search.Builder(queryStr).addIndex("movie_index").build()
    //执行查询操作
    val result: SearchResult = jestClient.execute(search)
    val hisList: util.List[SearchResult#Hit[util.Map[String, Any], Void]] = result.getHits(classOf[util.Map[String, Any]])

    import scala.collection.JavaConverters._
    println(hisList.asScala.map(_.source).toList.mkString("\n"))
    //关闭连接
    jestClient.close()
  }

  // 6、批量插入
  def bulkInsert(dauList: List[(String, Any)], indexName: String): Unit = {
    if (dauList != null && dauList.size != 0) {
      //获取客户端连接
      val jestClient = getClient()
      val builder: Bulk.Builder = new Bulk.Builder
      for ((id, dau) <- dauList) {
        val index: Index = new Index.Builder(dau).
          index(indexName).
          `type`("_doc")
          .id(id)
          .build()
        builder.addAction(index)
      }
      val bulk: Bulk = builder.build()
      //执行批量操作
      val result: BulkResult = jestClient.execute(bulk)
      val items: util.List[BulkResult#BulkResultItem] = result.getItems
      println("向ES中插入了" + items.size() + "条数据")
      //关闭连接
      jestClient.close()
    }
  }

  def main(args: Array[String]): Unit = {
    queryIndex
  }
}

case class Movie(
                  id: Long,
                  name: String,
                  doubanScore: Double,
                  actorList: util.List[util.Map[String, Any]]
                )
