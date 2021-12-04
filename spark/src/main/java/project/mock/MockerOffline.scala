package project.mock

import java.io.FileWriter
import java.text.SimpleDateFormat
import java.util.UUID

import scala.collection.mutable.ListBuffer

/**
  * @class ??
  * @CalssName MockerOffline
  * @author lizhong.liu 
  * @create 2020-07-09 16:30
  * @Des TODO
  * @version TODO
  */
object MockerOffline {

  val userNum = 100; // 用户个数
  val productNum = 100 // 产品个数
  val sessionNum = 10000 // session个数

  val pageNum = 50 // 网页个数
  val categoryNum = 20 //

  val logAboutNum = 100000 //日志大致数量，用于分布时间

  val professionRandomOpt = RandomOptions(RanOpt("学生", 40), RanOpt("程序员", 30), RanOpt("经理", 20), RanOpt("老师", 10))

  val genderRandomOpt = RandomOptions(RanOpt("男", 60), RanOpt("女", 40))
  val ageFrom = 10
  val ageTo = 59

  val productExRandomOpt = RandomOptions(RanOpt("自营", 70), RanOpt("第三方", 30))

  val searchKeywordsOptions = RandomOptions(RanOpt("手机", 30), RanOpt("笔记本", 70), RanOpt("内存", 70), RanOpt("i7", 70), RanOpt("苹果", 70), RanOpt("吃鸡", 70))
  val actionsOptions = RandomOptions(RanOpt("search", 20), RanOpt("click", 60), RanOpt("order", 6), RanOpt("pay", 4), RanOpt("quit", 10))


  def main(args: Array[String]): Unit = {
    /*

        val sparkConf = new SparkConf().setAppName("Mock").setMaster("local[*]")
        val sparkSession = SparkSession.builder().config(sparkConf).enableHiveSupport().getOrCreate()
    */

    // 模拟数据
    val userVisitActionData: List[UserVisitAction] = this.mockUserAction()
    val userInfoData: List[UserInfo] = this.mockUserInfo()
    val productInfoData: List[ProductInfo] = this.mockProductInfo()
    val cityInfoData: List[CityInfo] = this.mockCityInfo()
    val user_visit_action: FileWriter = new FileWriter("D:\\tmp\\projectSpark\\user_visit_action.txt")
    val user_info: FileWriter = new FileWriter("D:\\tmp\\projectSpark\\user_info.txt")
    val product_info: FileWriter = new FileWriter("D:\\tmp\\projectSpark\\product_info.txt")
    val city_info: FileWriter = new FileWriter("D:\\tmp\\projectSpark\\city_info.txt")
    println("userVisitActionData:" + userVisitActionData.size)
    println("userInfoData:" + userInfoData.size)
    println("productInfoData:" + productInfoData.size)
    println("cityInfoData:" + cityInfoData.size)
    for (elem <- userVisitActionData) {
      user_visit_action.write(elem.datetime + "," + elem.user_id + "," + elem.session_id + "," + elem.page_id + "," + elem.action_time + "," + elem.search_keyword + ","
        + elem.click_category_id + "," + elem.click_product_id + "," + elem.order_category_ids + "," + elem.order_product_ids + "," + elem.pay_category_ids + ","
        + elem.pay_product_ids + "," + elem.city_id + "\n")
    }
    for (elem <- userInfoData) {
      user_info.write(elem.user_id + "," + elem.username + "," + elem.name + "," + elem.age + "," + elem.professional + "," + elem.gender + "\n")
      print(elem.user_id + "," + elem.username + "," + elem.name + "," + elem.age + "," + elem.professional + "," + elem.gender + "\n")
    }
    for (elem <- productInfoData) {
      product_info.write(elem.product_id + "," + elem.product_name + "," + elem.extend_info + "\n")
      print(elem.product_id + "," + elem.product_name + "," + elem.extend_info + "\n")
    }
    for (elem <- cityInfoData) {
      city_info.write(elem.city_id + "," + elem.city_name + "," + elem.area + "\n")
      print(elem.city_id + "," + elem.city_name + "," + elem.area + "\n")
    }

    /*
        // 将模拟数据装换为RDD
        val userVisitActionRdd = sparkSession.sparkContext.makeRDD(userVisitActionData)
        val userInfoRdd = sparkSession.sparkContext.makeRDD(userInfoData)
        val productInfoRdd = sparkSession.sparkContext.makeRDD(productInfoData)
        val cityInfoRdd = sparkSession.sparkContext.makeRDD(cityInfoData)

        import sparkSession.implicits._
        val userVisitActionDF = userVisitActionRdd.toDF()
        val userInfoDF = userInfoRdd.toDF()
        val productInfoDF = productInfoRdd.toDF()
        val cityInfoDF = cityInfoRdd.toDF()


        insertHive(sparkSession, "user_visit_action", userVisitActionDF)
        insertHive(sparkSession, "user_info", userInfoDF)
        insertHive(sparkSession, "product_info", productInfoDF)
        insertHive(sparkSession, "city_info", cityInfoDF)

        sparkSession.close()*/
  }

  def mockUserInfo() = {
    val rows = new ListBuffer[UserInfo]()
    for (i <- 1 to userNum) {
      val user = UserInfo(i,
        "user_" + i,
        "name_" + i,
        RandomNum(ageFrom, ageTo), //年龄
        professionRandomOpt.getRandomOpt(),
        genderRandomOpt.getRandomOpt()
      )
      rows += user
    }
    rows.toList
  }

  def mockUserAction() = {
    val rows = new ListBuffer[UserVisitAction]() // 含有UserVisitAction 的list
    val startDate = new SimpleDateFormat("yyyy-MM-dd").parse("2018-11-26")
    val endDate = new SimpleDateFormat("yyyy-MM-dd").parse("2018-11-27")
    val randomDate = RandomDate(startDate, endDate, logAboutNum) // 获取randomDate对象(为后续随机事件准备)
    for (i <- 1 to sessionNum) {
      val userId = RandomNum(1, userNum)
      val sessionId = UUID.randomUUID().toString
      var isQuit = false

      while (!isQuit) {
        val action = actionsOptions.getRandomOpt()

        if (action == "quit") {
          isQuit = true
        } else {
          val actionDateTime = randomDate.getRandomDate()
          val actionDateString = new SimpleDateFormat("yyyy-MM-dd").format(actionDateTime)
          val actionDateTimeString = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(actionDateTime)

          var searchKeyword: String = null
          var clickCategoryId: Long = -1L
          var clickProductId: Long = -1L
          var orderCategoryIds: String = null
          var orderProductIds: String = null
          var payCategoryIds: String = null
          var payProductIds: String = null

          var cityId: Long = RandomNum(1, 26).toLong

          action match {
            case "search" => searchKeyword = searchKeywordsOptions.getRandomOpt()
            case "click" => clickCategoryId = RandomNum(1, categoryNum)
              clickProductId = RandomNum(1, productNum)
            case "order" => orderCategoryIds = RandomNum.multi(1, categoryNum, RandomNum(1, 5), ",", false)
              orderProductIds = RandomNum.multi(1, categoryNum, RandomNum(1, 5), ",", false)
            case "pay" => payCategoryIds = RandomNum.multi(1, categoryNum, RandomNum(1, 5), ",", false)
              payProductIds = RandomNum.multi(1, categoryNum, RandomNum(1, 5), ",", false)
          }

          val userVisitAction = UserVisitAction(
            actionDateString,
            userId.toLong,
            sessionId,
            RandomNum(1, pageNum).toLong,
            actionDateTimeString,
            searchKeyword,
            clickCategoryId.toLong,
            clickProductId.toLong,
            orderCategoryIds,
            orderProductIds,
            payCategoryIds,
            payProductIds,
            cityId
          )
          rows += userVisitAction
        }
      }

    }
    rows.toList
  }

  def mockProductInfo() = {
    val rows = new ListBuffer[ProductInfo]()
    for (i <- 1 to productNum) {
      val productInfo = ProductInfo(
        i,
        "商品_" + i,
        productExRandomOpt.getRandomOpt()
      )
      rows += productInfo
    }
    rows.toList
  }

  // 城市表数据
  def mockCityInfo() = {
    List(CityInfo(1L, "北京", "华北"), CityInfo(2L, "上海", "华东"),
      CityInfo(3L, "深圳", "华南"), CityInfo(4L, "广州", "华南"),
      CityInfo(5L, "武汉", "华中"), CityInfo(6L, "南京", "华东"),
      CityInfo(7L, "天津", "华北"), CityInfo(8L, "成都", "西南"),
      CityInfo(9L, "哈尔滨", "东北"), CityInfo(10L, "大连", "东北"),
      CityInfo(11L, "沈阳", "东北"), CityInfo(12L, "西安", "西北"),
      CityInfo(13L, "长沙", "华中"), CityInfo(14L, "重庆", "西南"),
      CityInfo(15L, "济南", "华东"), CityInfo(16L, "石家庄", "华北"),
      CityInfo(17L, "银川", "西北"), CityInfo(18L, "杭州", "华东"),
      CityInfo(19L, "保定", "华北"), CityInfo(20L, "福州", "华南"),
      CityInfo(21L, "贵阳", "西南"), CityInfo(22L, "青岛", "华东"),
      CityInfo(23L, "苏州", "华东"), CityInfo(24L, "郑州", "华北"),
      CityInfo(25L, "无锡", "华东"), CityInfo(26L, "厦门", "华南")
    )
  }
}

