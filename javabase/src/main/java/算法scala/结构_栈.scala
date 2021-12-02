package 算法scala

/*
  数据机构和算法 -- 栈-实现综合计算器
  */
object Calculator {
  def main(args: Array[String]): Unit = {

    //val expression = "3101+4*(6-2)" => 开阔思路[编程] |
    val expression = "7*2*2-5+1-5+3-4"

    val numStack = new ArrayStack2(10)
    val operStack = new ArrayStack2(10)

    /*
    思路
    1 ) 设计两个栈 , 数栈，符号栈
    2)  对 exp 进行扫描 , 一个一个的取出
    3)  当取出的字符是数时，就直接入数栈
    4)  当取出的字符是符号时
    4.1 如果当前符号栈没有数据，就直接入栈
    4.2 如果当前符号的优先级 小于等于符号栈的栈顶的符号的优先级，则取出该符号，并从数栈依次pop 出两个数据，进行运算，将结果重新puhs到 数栈，再将当前符号push 到符号栈
    4.3 反之，符号直接入符号栈
    5) 当整个表达式扫描完毕后，依次从数栈和符号栈取出数据，进行运行，最后在数栈中的数据就是结果.
     */
    var index = 0
    var num1 = 0
    var num2 = 0
    var oper = 0
    var res = 0
    var ch = ' '
    var keepNum = "" // 再进行扫描时，保存上次的数字ch ,并进行拼接
    //会循环的取出expression 字符
    breakable {
      while (true) {

        //扫描expression
        ch = expression.substring(index, index + 1)(0)

        if (operStack.isOper(ch)) { //如果是操作符..

          if (!operStack.isEmpty()) {
            //如果当前符号的优先级 小于等于符号栈的栈顶的符号的优先级，则取出该符号，并从数栈依次 //pop 出两个数据，进行运算，将结果重新puhs到 数栈，再将当前符号push 到符号栈
            if (operStack.priority(ch) <= operStack.priority(operStack.stack(operStack.top))) {
              //开始计算
              num1 = numStack.pop().toString.toInt
              num2 = numStack.pop().toString.toInt
              oper = operStack.pop().toString.toInt
              res = numStack.cal(num1, num2, oper)
              //入数字栈
              numStack.push(res)
              //把当前ch入符号栈
              operStack.push(ch)
            } else {
              //如果当前的符号的优先级大于符号栈顶的符号优先级，直接入栈
              operStack.push(ch)
            }
          } else {
            //符号就直接入栈
            operStack.push(ch) // '+' => 43
          }

        } else { // 是数
          //处理多位数的逻辑
          keepNum += ch

          //如果ch 已经是expression 最后一个字符
          if (index == expression.length - 1) {
            numStack.push(keepNum.toInt)
          } else {

            //判断ch 的下一个字符是不是数字, 如果是数字，则进行一次扫描，如果是操作符，就直接入栈
            //看到expresson的下一个字符时，不要真正的移动index ,只是探测一下
            if (operStack.isOper(expression.substring(index + 1, index + 2)(0))) {
              //是操作符入栈
              numStack.push(keepNum.toInt)
              keepNum = "" // 清空
            }
          }

          //numStack.push((ch + "").toInt) // ? '1' => 49 '3' "1"=> 1
        }

        //index 后移
        index += 1
        //判断是否到表达式的最后
        if (index >= expression.length()) {
          break()
        }

      }
    }

    //当整个表达式扫描完毕后，依次从数栈和符号栈取出数据，进行运行，最后在数栈中的数据就是结果
    breakable {
      while (true) {
        if (operStack.isEmpty()) {
          break()
        }
        //运算
        //开始计算
        num1 = numStack.pop().toString.toInt
        num2 = numStack.pop().toString.toInt
        oper = operStack.pop().toString.toInt
        res = numStack.cal(num1, num2, oper)
        numStack.push(res) //入栈
      }
    }

    //将数字栈的最后结果pop
    val res2 = numStack.pop()
    printf("表达式 %s = %d", expression, res2)
  }
}


//栈，该栈已经测试过了
class ArrayStack2(size: Int) {
  val maxSize = size // 栈的大小
  var stack = new Array[Int](maxSize)
  //栈顶, 初始化为-1
  var top = -1

  //栈满
  def isFull(): Boolean = {
    top == maxSize - 1
  }

  //栈空
  def isEmpty(): Boolean = {
    top == -1
  }

  //入栈, 放入数据
  def push(value: Int): Unit = {
    if (isFull()) {
      println("栈满")
      return
    }
    top += 1
    stack(top) = value
  }

  //出栈, 取出数据
  def pop(): Any = {
    if (isEmpty()) {
      return new Exception("栈空")
    }
    val value = stack(top)
    top -= 1
    return value
  }

  //遍历栈
  def list(): Unit = {
    if (isEmpty()) {
      println("栈空，没有数据")
      return
    }
    for (i <- 0 to top reverse) {
      printf("stack[%d]=%d\n", i, stack(i))
    }
  }

  //返回运算符的优先级, 是程序员定, 数字越大，优先级越高
  // + 1 => 0 *[] /[] => 1
  def priority(oper: Int): Int = {
    if (oper == '*' || oper == '/') {
      return 1
    } else if (oper == '+' || oper == '-') {
      return 0
    } else {
      -1 //不正确
    }
  }

  def isOper(value: Int): Boolean = {
    value == '+' || value == '-' || value == '/' || value == '*'
  }

  //计算方法
  def cal(num1: Int, num2: Int, oper: Int): Int = {
    var res = 0
    oper match {
      case '+' => {
        res = num1 + num2
      }
      case '-' => {
        res = num2 - num1
      }
      case '*' => {
        res = num1 * num2
      }
      case '/' => {
        res = num2 / num1
      }
    }
    res
  }
}
