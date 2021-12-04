package project.mock

import java.util.Date

import scala.util.Random

/**
  * @class 生成随机日期
  * @CalssName RandomDate
  * @author lizhong.liu 
  * @create 2020-07-09 16:27
  * @Des TODO
  * @version TODO
  */
object RandomDate {

  // 构造器，初始化一个RandomDate，并将lastDateTime赋值为开始时间，maxTimeStep赋值为平均每条时间*2
  def apply(startDate: Date, endDate: Date, step: Int): RandomDate = {
    val randomDate = new RandomDate()
    val avgStepTime = (endDate.getTime - startDate.getTime) / step
    randomDate.maxTimeStep = avgStepTime * 2
    randomDate.lastDateTime = startDate.getTime
    randomDate
  }

  class RandomDate {
    var lastDateTime = 0L
    var maxTimeStep = 0L

    def getRandomDate() = {
      val timeStep = new Random().nextInt(maxTimeStep.toInt)
      lastDateTime = lastDateTime + timeStep
      new Date(lastDateTime)
    }
  }
}

