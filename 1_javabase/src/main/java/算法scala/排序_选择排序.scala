package 算法scala

import java.text.SimpleDateFormat
import java.util.Date

/*
  数据结构和算法 -- 选择排序
  */
object SelectSort {
  def main(args: Array[String]): Unit = {
    //var arr = Array(101, 34, 119, 1)

    val random = new util.Random()
    val arr = new Array[Int](80000)
    for (i <- 0 until 80000) { //循环的生成随机数
      arr(i) = random.nextInt(8000000)
    }
    println("排序前")
    val now: Date = new Date()
    val dateFormat: SimpleDateFormat =
      new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    val date = dateFormat.format(now)
    println("排序前时间=" + date) //输出时间

    //我们发现规律,调用选择排序
    selectSort(arr)

    println("排序后")
    //println(arr.mkString(" "))
    val now2: Date = new Date()
    val date2 = dateFormat.format(now2)
    println("排序后时间=" + date2) //输出时间


    //我们看一下排序的一个演变过程
    //第1轮排序 (101, 34, 119, 1) => (1, 34, 119, 101)

    /*

    var min = arr(0)
    var minIndex = 0
    //遍历
    for (j <- (0 + 1) until arr.length) {
      if (min > arr(j)) { // 说明min不是真的最小值
        min = arr(j) // 重置min
        minIndex = j // 重置minIndex
      }
    }
    //判断是否需要交换
    if (minIndex != 0) {
      arr(minIndex) = arr(0)
      arr(0) = min
    }

    println("第1轮结束")
    println(arr.mkString(" "))

    //第2轮排序 (1, 34, 119, 101) => (1, 34, 119, 101)

    min = arr(1)
    minIndex = 1
    //遍历
    for (j <- (1 + 1) until arr.length) {
      if (min > arr(j)) { // 说明min不是真的最小值
        min = arr(j) // 重置min
        minIndex = j // 重置minIndex
      }
    }
    //判断是否需要交换
    if (minIndex != 1) {
      arr(minIndex) = arr(1)
      arr(1) = min
    }

    println("第2轮结束")
    println(arr.mkString(" "))


    //第3轮排序 (1, 34, 119, 101) => (1, 34, 101, 119)

    min = arr(2)
    minIndex = 2
    //遍历
    for (j <- (2 + 1) until arr.length) {
      if (min > arr(j)) { // 说明min不是真的最小值
        min = arr(j) // 重置min
        minIndex = j // 重置minIndex
      }
    }
    //判断是否需要交换
    if (minIndex != 2) {
      arr(minIndex) = arr(2)
      arr(2) = min
    }

    println("第3轮结束")
    println(arr.mkString(" "))
    */

  }

  def selectSort(arr: Array[Int]): Unit = {

    for (i <- 0 until arr.length - 1) {
      var min = arr(i)
      var minIndex = i
      //遍历
      for (j <- (i + 1) until arr.length) {
        if (min > arr(j)) { // 说明min不是真的最小值
          min = arr(j) // 重置min
          minIndex = j // 重置minIndex
        }
      }
      //判断是否需要交换
      if (minIndex != i) {
        arr(minIndex) = arr(i)
        arr(i) = min
      }

      //      println(s"第${i+1}轮结束")
      //      println(arr.mkString(" "))
    }
  }
}