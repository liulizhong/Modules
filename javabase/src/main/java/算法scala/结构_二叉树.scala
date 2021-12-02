package 算法scala

/*
  数据结构和算法 -- 经典二叉树(遍历、查找、删除)
  */
object BinaryTree {
  def main(args: Array[String]): Unit = {

    // 0、先使用比较简单方法，直接关联的方法创建数据体系
    val root = new HeroNode2(1, "宋江")
    val hero2 = new HeroNode2(2, "吴用")
    val hero3 = new HeroNode2(3, "卢俊义")
    val hero4 = new HeroNode2(4, "林冲")
    val hero5 = new HeroNode2(5, "关胜")

    root.left = hero2
    root.right = hero3
    hero3.left = hero5
    hero3.right = hero4

    // 1、创建管理节点类
    val binaryTree = new BinaryTree
    binaryTree.root = root

    // 2、遍历
    binaryTree.preOrder() //前序遍历
    binaryTree.infixOrder() //前序遍历
    binaryTree.postOrder() //前序遍历

    // 3、查找指定节点
    val resNode = binaryTree.preOrderSearch(51)
    //    val resNode = binaryTree.infixOrderSeacher(5)
    //    val resNode = binaryTree.postOrderSearch(5)
    if (resNode != null) {
      printf("找到，编号=%d name=%s", resNode.no, resNode.name)
    } else {
      println("没有找到~")
    }

    // 4、测试删除节点
    binaryTree.delNode(1)
  }
}

//定义节点
class HeroNode2(hNo: Int, hName: String) {
  val no = hNo
  var name = hName
  var left: HeroNode2 = null
  var right: HeroNode2 = null

  //删除节点
  //删除节点规则
  //1如果删除的节点是叶子节点，则删除该节点
  //2如果删除的节点是非叶子节点，则删除该子树
  def delNode(no: Int): Unit = {
    //首先比较当前节点的左子节点是否为要删除的节点
    if (this.left != null && this.left.no == no) {
      this.left = null
      return
    }
    //比较当前节点的右子节点是否为要删除的节点
    if (this.right != null && this.right.no == no) {
      this.right = null
      return
    }
    //向左递归删除
    if (this.left != null) {
      this.left.delNode(no)
    }
    //向右递归删除
    if (this.right != null) {
      this.right.delNode(no)
    }
  }

  //后序遍历查找
  def postOrderSearch(no: Int): HeroNode2 = {
    //向左递归输出左子树
    var resNode: HeroNode2 = null
    if (this.left != null) {
      resNode = this.left.postOrderSearch(no)
    }
    if (resNode != null) {
      return resNode
    }
    if (this.right != null) {
      resNode = this.right.postOrderSearch(no)
    }
    if (resNode != null) {
      return resNode
    }
    println("ttt~~")
    if (this.no == no) {
      return this
    }
    resNode
  }

  //后序遍历
  def postOrder(): Unit = {
    //向左递归输出左子树
    if (this.left != null) {
      this.left.postOrder()
    }
    //向右边递归输出右子树
    if (this.right != null) {
      this.right.postOrder()
    }
    //先输出当前节点值
    printf("节点信息 no=%d name=%s\n", no, name)
  }

  //中序遍历查找
  def infixOrderSearch(no: Int): HeroNode2 = {


    var resNode: HeroNode2 = null
    //先向左递归查找
    if (this.left != null) {
      resNode = this.left.infixOrderSearch(no)
    }
    if (resNode != null) {
      return resNode
    }
    println("yyy~~")
    if (no == this.no) {
      return this
    }
    //向右递归查找
    if (this.right != null) {
      resNode = this.right.infixOrderSearch(no)
    }
    return resNode

  }

  //中序遍历
  def infixOrder(): Unit = {
    //向左递归输出左子树
    if (this.left != null) {
      this.left.infixOrder()
    }
    //先输出当前节点值
    printf("节点信息 no=%d name=%s\n", no, name)
    //向右边递归输出右子树
    if (this.right != null) {
      this.right.infixOrder()
    }

  }

  //前序查找
  def preOrderSearch(no: Int): HeroNode2 = {
    if (no == this.no) {
      return this
    }
    //向左递归查找
    var resNode: HeroNode2 = null
    if (this.left != null) {
      resNode = this.left.preOrderSearch(no)
    }
    if (resNode != null) {
      return resNode
    }
    //向右边递归查找
    if (this.right != null) {
      resNode = this.right.preOrderSearch(no)
    }
    return resNode
  }

  //前序遍历
  def preOrder(): Unit = {
    //先输出当前节点值
    printf("节点信息 no=%d name=%s\n", no, name)
    //向左递归输出左子树
    if (this.left != null) {
      this.left.preOrder()
    }
    //向右边递归输出右子树
    if (this.right != null) {
      this.right.preOrder()
    }
  }
}

class BinaryTree {
  var root: HeroNode2 = null

  // 删除节点
  def delNode(no: Int): Unit = {
    if (root != null) {
      //先处理一下root是不是要删除的
      if (root.no == no) {
        root = null
      } else {
        root.delNode(no)
      }
    }
  }

  //后续遍历查找
  def postOrderSearch(no: Int): HeroNode2 = {
    if (root != null) {
      root.postOrderSearch(no)
    } else {
      null
    }
  }

  //后序遍历
  def postOrder(): Unit = {
    if (root != null) {
      root.postOrder()
    } else {
      println("当前二叉树为空，不能遍历")
    }
  }

  //中序遍历查找
  def infixOrderSeacher(no: Int): HeroNode2 = {
    if (root != null) {
      return root.infixOrderSearch(no)
    } else {
      return null
    }
  }

  //中序遍历
  def infixOrder(): Unit = {
    if (root != null) {
      root.infixOrder()
    } else {
      println("当前二叉树为空，不能遍历")
    }
  }

  //前序查找
  def preOrderSearch(no: Int): HeroNode2 = {

    if (root != null) {
      return root.preOrderSearch(no)
    } else {
      //println("当前二叉树为空，不能查找")
      return null
    }

  }

  //前序遍历
  def preOrder(): Unit = {
    if (root != null) {
      root.preOrder()
    } else {
      println("当前二叉树为空，不能遍历")
    }
  }
}