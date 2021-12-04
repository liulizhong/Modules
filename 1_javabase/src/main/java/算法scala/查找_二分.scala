package 算法scala

import scala.collection.mutable.ArrayBuffer
import util.control.Breaks._

/*
  数据结构和算法 -- 二分查找
  */
object BinarySearch {
  def main(args: Array[String]): Unit = {

    val arr = Array(1, 8, 10, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1234)
    //    val index = binarySearch(arr, 0, arr.length - 1, 1000)
    //    if (index != -1) {
    //      println("找到，下标为" + index)
    //    } else {
    //      println("没有找到")
    //    }

    var resArr = binarySearch2(arr, 0, arr.length - 1, 1000)
    resArr = resArr.sortBy((x: Int) => x)
    if (resArr.length != 0) {
      for (index <- resArr) {
        println("找到的索引有" + index)
      }
    } else {
      println("没有找到")
    }
  }

  //二分查找的思路
  //1. 先找到中间值
  //2. 然后将中间值和查找值比较
  //2.1 相等 ，找出
  //2.2 中间值 > 查找值, 向左进行递归查找
  //2.3 中间值 < 查找值, 向右进行递归查找
  // ? 在什么情况下，表示找不到?
  //如果存在值，就返回对应的下标，否则返回-1
  def binarySearch(arr: Array[Int], l: Int, r: Int, findVal: Int): Int = {

    //找不到条件?
    if (l > r) {
      return -1
    }

    val midIndex = (l + r) / 2
    val midVal = arr(midIndex)
    if (midVal > findVal) {
      //向左进行递归查找
      binarySearch(arr, l, midIndex - 1, findVal)
    } else if (midVal < findVal) { //向右进行递归查找
      binarySearch(arr, midIndex + 1, r, findVal)
    } else {
      return midIndex
    }

  }

  /*
  课后思考题： {1,8, 10, 89, 1000, 1000，1234} 当一个有序数组中，有多个相同的数值时，如何将所有的数值都查找到，比如这里的 1000.
  //分析
  1. 返回的结果是一个可变数组 ArrayBuffer
  2. 在找到结果时，向左边扫描，向右边扫描 [条件]
  3. 找到结果后，就加入到ArrayBuffer
   */
  def binarySearch2(arr: Array[Int], l: Int, r: Int,
                    findVal: Int): ArrayBuffer[Int] = {

    //找不到条件?
    if (l > r) {
      return ArrayBuffer()
    }

    val midIndex = (l + r) / 2
    val midVal = arr(midIndex)
    if (midVal > findVal) {
      //向左进行递归查找
      binarySearch2(arr, l, midIndex - 1, findVal)
    } else if (midVal < findVal) { //向右进行递归查找
      binarySearch2(arr, midIndex + 1, r, findVal)
    } else {
      println("midIndex=" + midIndex)
      //定义一个可变数组
      val resArr = ArrayBuffer[Int]()
      //向左边扫描
      var temp = midIndex - 1
      breakable {
        while (true) {
          if (temp < 0 || arr(temp) != findVal) {
            break()
          }
          if (arr(temp) == findVal) {
            resArr.append(temp)
          }
          temp -= 1
        }
      }
      //将中间这个索引加入
      resArr.append(midIndex)
      //向右边扫描
      temp = midIndex + 1
      breakable {
        while (true) {
          if (temp > arr.length - 1 || arr(temp) != findVal) {
            break()
          }
          if (arr(temp) == findVal) {
            resArr.append(temp)
          }
          temp += 1
        }
      }
      return resArr
    }
  }
}