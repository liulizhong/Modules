package 算法scala

import util.control.Breaks._
/*
  数据结构和算法 -- 单链表
  */
object SingleLinkedListDemo {
  def main(args: Array[String]): Unit = {
    //测试单向链表的添加和遍历
    val hero1 = new HeroNode(1, "宋江", "及时雨")
    val hero2 = new HeroNode(3, "宋江3", "及时雨3")
    val hero3 = new HeroNode(4, "宋江4", "及时雨4")
    val hero4 = new HeroNode(2, "宋江2", "及时雨2")
    //创建一个单向链表
    val singleLinkedList = new SingleLinkedList
    singleLinkedList.add(hero1)
    singleLinkedList.add(hero2)
    singleLinkedList.add(hero3)
    singleLinkedList.add(hero4)

    //    singleLinkedList.add2(hero1)
    //    singleLinkedList.add2(hero2)
    //    singleLinkedList.add2(hero3)
    //    singleLinkedList.add2(hero4)
    singleLinkedList.list()

    val hero5 = new HeroNode(3, "吴用", "智多星")
    singleLinkedList.update(hero5)
    println("==========================")
    singleLinkedList.list()

    println("@@@@@@@@@@测试删除@@@@@@@@@@@")
    singleLinkedList.del(4)
    singleLinkedList.del(2)
    singleLinkedList.del(1)
    singleLinkedList.list()
  }
}

//定义我们的单向链表管理Hero
class SingleLinkedList {

  //先初始化一个头结点, 头结点一般不会动
  val head = new HeroNode(0, "", "")

  //删除节点
  //1. 思路
  //  删除的思路
  //  1). head 不能动
  //  2). 使用temp变量, 我们要删除的应该是temp 的下一个结点., 即，我们在比较时，始终比较的是 temp.next 的节点值

  //2. 代码

  def del(no: Int): Unit = {
    var temp = head
    var flag = false // 标志变量用于确定是否有要删除的节点
    breakable {
      while (true) {
        if (temp.next == null) {
          break()
        }
        if (temp.next.no == no) {
          //找到了
          flag = true
          break()
        }
        temp = temp.next //temp后移
      }
    }

    if (flag) {
      //可以删除
      temp.next = temp.next.next

    } else {
      printf("要删除的no=%d 不存在\n", no)
    }
  }

  //修改节点的值, 根据no编号为前提修改, 即no不能改
  //课后思考题： 如果将这个节点替换，如何实现
  def update(newHeroNode: HeroNode): Unit = {
    if (head.next == null) {
      println("链表为空")
      return
    }
    //先找到节点
    var temp = head.next
    var flag = false
    breakable {
      while (true) {
        if (temp == null) {
          break()
        }
        if (temp.no == newHeroNode.no) {
          //找到.
          flag = true
          break()
        }
        temp = temp.next //
      }
    }
    //判断是否找到
    if (flag) {
      temp.name = newHeroNode.name
      temp.nickname = newHeroNode.nickname
    } else {
      printf("没有找到 编号为%d 节点，不能修改\n", newHeroNode.no)
    }

  }

  //编写添加方法
  //第一种方法在添加英雄时，直接添加到链表的尾部
  def add(heroNode: HeroNode): Unit = {
    //因为头结点不能动, 因此我们需要哟有一个临时结点，作为辅助
    var temp = head
    //找到链表的最后
    breakable {
      while (true) {
        if (temp.next == null) { //说明temp已经是链表最后
          break()
        }
        //如果没有到最后
        temp = temp.next
      }
    }
    //当退出while循环后，temp就是链表的最后
    temp.next = heroNode
  }

  //第二种方式在添加英雄时，根据排名将英雄插入到指定位置
  //如果有这个排名，则添加失败，并给出提示
  //编号从小到大排序
  //思路
  def add2(heroNode: HeroNode): Unit = {
    //因为头结点不能动, 因此我们需要哟有一个临时结点，作为辅助
    //注意，我们在找这个添加位置时，是将这个节点加入到temp的后面
    //因此，在比较时，是将当前的heroNode 和 temp.next 比较
    var temp = head
    var flag = false // flag 是用于判断是否该英雄的编号已经存在, 默认为false
    breakable {
      while (true) {
        if (temp.next == null) { //说明temp已经是链表最后
          break()
        }
        if (temp.next.no > heroNode.no) {
          //位置找到，当前这个节点，应当加入到temp后
          break()
        } else if (temp.next.no == heroNode.no) {
          //已经有这个节点
          flag = true
          break()
        }
        temp = temp.next // 注意
      }
    }
    if (flag) { // 不可以加入
      printf("待插入的英雄编号 %d 已经有了，不能加入\n", heroNode.no)
    } else {
      //加入，注意顺序
      heroNode.next = temp.next
      temp.next = heroNode
    }
  }


  //遍历单向链表
  def list(): Unit = {

    //判断当前链表是否为空
    if (head.next == null) {
      println("链表为空!!")
      return
    }
    //因为头结点不能动, 因此我们需要哟有一个临时结点，作为辅助
    //因为head 结点数据，我们不关心，因此这里 temp=head.next
    var temp = head.next
    breakable {
      while (true) {
        //判断是否到最后
        if (temp == null) {
          break()
        }
        printf("结点信息 no=%d name=%s nickname=%s\n",
          temp.no, temp.name, temp.nickname)
        temp = temp.next
      }
    }
  }

}

//先创建HeroNode
class HeroNode(hNo: Int, hName: String, hNickname: String) {
  var no: Int = hNo
  var name: String = hName
  var nickname: String = hNickname
  var next: HeroNode = null //next 默认为null
}