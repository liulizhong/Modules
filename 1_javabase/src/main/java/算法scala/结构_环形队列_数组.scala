package 算法scala

import scala.io.StdIn

/*
  数据结构和算法 -- 环形队列(数组实现)
  */
object CircleArrayQueueDemo {
  def main(args: Array[String]): Unit = {

    println("~~~环形队列的案例~~~")
    //初始化一个队列
    val queue = new CircleArrayQueue(4)
    var key = ""
    while (true) {
      println("show: 表示显示队列")
      println("exit: 表示退出程序")
      println("add: 表示添加队列数据")
      println("get: 表示取出队列数据")
      println("head: 查看队列头的数据(不改变队列)")

      key = StdIn.readLine()
      key match {
        case "show" => queue.showQueue()
        case "add" => {
          println("请输入一个数")
          val value = StdIn.readInt()
          queue.addQueue(value)
        }
        case "get" => {
          val res = queue.getQueue()
          if (res.isInstanceOf[Exception]) {
            println(res.asInstanceOf[Exception].getMessage)
          } else {
            println(s"取出数据是 $res")
          }
        }
        case "head" => {
          val res = queue.headQueue()
          if (res.isInstanceOf[Exception]) {
            //显示错误信息
            println(res.asInstanceOf[Exception].getMessage)
          } else {
            println("队列头元素值为=" + res)
          }
        }
        case "exit" => System.exit(0)
      }
    }

  }
}

//环形的队列和前面的单向队列有类似的地方，因为我们修改即可
class CircleArrayQueue(arrMaxSize: Int) {
  val maxSize = arrMaxSize
  val arr = new Array[Int](maxSize) //该数组存放数据，模拟队列
  var front = 0 // 指向队列头部
  var rear = 0 // 指向队列的尾部

  //判断队列满的方法
  //队列容量空出一个作为约定
  def isFull(): Boolean = {
    //1 => rear 1
    //2 => rear 2
    //3 => rear 3
    (rear + 1) % maxSize == front
  }

  //判断队列空的条件
  def isEmpty(): Boolean = {
    rear == front
  }


  //添加数据到队列
  def addQueue(n: Int): Unit = {
    //判断是否满
    if (isFull()) {
      println("队列满，无法加入..")
      return
    }
    //将数据加入
    arr(rear) = n
    //然后将rear 后移, 这里必须考虑取模
    rear = (rear + 1) % maxSize
  }

  //取出队列的数据(按先进先出的原则)
  def getQueue(): Any = {
    if (isEmpty()) {
      return new Exception("队列空~")
    }
    //这里我们需要分析处理 front 已经指向了队列的头元素
    //1. 先把front 对应的数据保存到变量
    //2. 将front后移
    //3. 返回前面保存的变量值
    val value = arr(front)
    front = (front + 1) % maxSize
    return value
  }

  //显示队列的所有数据
  def showQueue(): Unit = {
    if (isEmpty()) {
      println("队列空的，没有数据..")
      return
    }
    //思路: 从front 取，取出几个元素
    //动脑筋
    for (i <- front until front + size()) {
      printf("arr[%d]=%d\n", i % maxSize, arr(i % maxSize))
    }

  }

  //求出当前环形队列有几个元素
  //动脑筋
  //基础 【问题/需求 ---> 设计算法】
  def size(): Int = {
    //rear = 1
    //front = 0
    //maxSize = 3
    (rear + maxSize - front) % maxSize //求出当前队列实际有多少个数据
  }

  //查看队列的头元素，但是不是改变队列
  def headQueue(): Any = {
    if (isEmpty()) {
      return new Exception("队列空~")
    }
    //这里注意，不要去改变fornt 值
    return arr(front)
  }
}
