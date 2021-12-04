package project.mock

import scala.util.Random

/**
  * @class 随机数字方法
  * @CalssName RandomNum
  * @author lizhong.liu 
  * @create 2020-07-09 16:28
  * @Des TODO
  * @version TODO
  */
object RandomNum {
  def apply(fromNum: Int, toNum: Int): Int = {
    fromNum + new Random().nextInt(toNum - fromNum + 1)
  }

  def multi(fromNum: Int, toNum: Int, amount: Int, delimiter: String, canRepeat: Boolean) = {
    "1,2,3"
    // 实现方法  在fromNum和 toNum之间的 多个数组拼接的字符串 共amount个
    //    用delimiter分割  canRepeat为false则不允许重复
    //      此处逻辑请同学们自行实现
  }
}
