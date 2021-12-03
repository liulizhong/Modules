package 算法scala

import java.text.SimpleDateFormat
import java.util.Date

/*
  排序_归并排序
  */
object MergeSort {
  def main(args: Array[String]): Unit = {

    //var arr = Array(-9, 78, 0, 23, -567, 70)

    val random = new util.Random()
    val arr = new Array[Int](80000000)
    for (i <- 0 until 80000000) { //循环的生成随机数
      arr(i) = random.nextInt(8000000)
    }
    println("排序前")
    val now: Date = new Date()
    val dateFormat: SimpleDateFormat =
      new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    val date = dateFormat.format(now)

    val temp = new Array[Int](arr.length) //临时的数组

    println("排序前时间=" + date) //输出时间

    mergeSort(arr, 0, arr.length - 1, temp) //调用归并排序法

    println("归并排序后")

    val now2: Date = new Date()
    val date2 = dateFormat.format(now2)
    println("排序后时间=" + date2) //输出时间

    // println(arr.mkString(" "))

  }

  //是后一个归并排序
  /*
  1. arr :待排序的数组
  2. left: 最初的左边的下标 0
  3. right: 最初的右边下标 length - 1
  4. temp ： 就是临时数组 , 事先就开辟好的，大小和 arr 一样.
   */
  def mergeSort(arr: Array[Int], left: Int, right: Int, temp: Array[Int]): Unit = {
    if (left < right) { //如果 left < right 就可以继续分
      val mid = (left + right) / 2
      mergeSort(arr, left, mid, temp) // 递归将左边的数据做成有序列表
      mergeSort(arr, mid + 1, right, temp) //递归将右边的数据做成有序列表
      // merge 是合并的操作
      merge(arr, left, mid, right, temp)
    }
  }

  def merge(arr: Array[Int], left: Int, mid: Int, right: Int, temp: Array[Int]) {
    var i = left // i 就是左边指针[索引]
    var j = mid + 1 //j 就是右边的指针
    var t = 0 // t 是temp 数组的索引
    while (i <= mid && j <= right) {
      // 如果是当前的左边的有序列表的值小于当前的右边有序列表的值
      // 就把这个值拷贝到 temp数组
      if (arr(i) <= arr(j)) {
        temp(t) = arr(i)
        t += 1
        i += 1
      } else { // 如果是当前的右边的有序列表的值小于当前的左边有序列表的值，就把这个值拷贝到 temp数组
        temp(t) = arr(j)
        t += 1
        j += 1
      }
    }
    while (i <= mid) { //如果左边有序列表还有剩余数据，就依次拷贝到temp数组
      temp(t) = arr(i)
      t += 1
      i += 1
    }
    while (j <= right) { //如果右边有序列表还有剩余数据，就依次拷贝到temp数组
      temp(t) = arr(j)
      t += 1
      j += 1
    }

    // 下面代码是完成将本次的temp 的数据拷贝到arr中
    t = 0
    var tempLeft = left
    while (tempLeft <= right) {
      arr(tempLeft) = temp(t)
      t += 1
      tempLeft += 1
    }
  }


}
