package aaa课程代码.day03_流程控制;

import java.util.Scanner;

/**
 流程控制语句结构：
     1、顺序结构：从上到下挨个执行
     2、分支结构：根据条件执行
     3、循环结构：重复执行某些语句

 一、分支结构
     1、条件判断：if..else
     2、选择结构：switch..case...[default,break]

 二、if分支
         1、单分支条件判断
             if(条件表达式){
             当条件表达式成立时，执行的语句块;
             }

         2、双分支条件判断
             if(条件表达式){
             当条件表达式成立时，执行的语句块;
             }else{
             当条件表达式不成立时，执行的语句块;
             }

         3、多分支条件判断
             语法结构：
             if(条件表达式1){
             当条件表达式1成立时，执行的语句块1;
             }else if(条件表达式2){
             当条件表达式2成立时，执行的语句块2;
             }else if(条件表达式3){
             当条件表达式3成立时，执行的语句块3;
             }
             ....
             【
             else{
             当以上所有的条件表达式都不成立，执行的语句块n+1;
             }】
         4、执行特点：
             多个分支，也只会执行其中的一个；
             条件判断的顺序是从上往下判断，如果上面已经有满足条件了，下面的条件就不看了；
             如果上面的条件不满足，依次往下看；
             如果所有的条件表达式都不成立，看是否存在单独的else，如果有，那么就执行它的语句块。

         5、嵌套
             只有当外层的条件满足时，才会判断内层条件
             形式有很多：
                 单分支嵌套其他形式
                 双分支嵌套其他形式
                 多分支的某一个分支嵌套其他形式
         6、重点说明：
             （1）所有的if()中一定是boolean
             （2）如果{}中语句只有一句，可以省略{}，强烈建议保留
             （3）如果条件的区间范围是互斥的关系（区间没有重叠部分），多个条件的顺序可以调换					    ---------*******------------
             如果条件的区间范围是包含的关系（区间有重叠部分），顺序不能随意调整，范围小的在上，范围大的在下面		---------*******------------

 */
public class 流程控制_if分支语句 {
    public static void main(String[] args) {
        /**
         1、练习1
         例题：岳小鹏参加Java考试，他和父亲岳不群达成承诺：如果：
         成绩为100分时，奖励一辆BMW；
         成绩为(80，99]时，奖励一台iphone7plus；
         当成绩为[60,80]时，奖励一个 iPad；
         其它时，男孩就打，女孩就骂
         请从键盘输入岳小鹏的期末成绩，并加以判断
         */
        int grade = 59;
        char gander = '女';
        if (grade == 100) {
            System.out.println("奖励一辆BMW");
        } else if (grade > 80) {
            System.out.println("奖励一台iphone7plus");
        } else if (grade >= 60) {
            System.out.println("奖励一个 iPad");
        }else {
            if (gander == '男') {
                System.out.println("该打");
            } else {
                System.out.println("该骂");
            }
        }

        /**
         1、练习2
         假设你想开发一个玩彩票的游戏，程序随机地产生一个两位数的彩票，提示用户输入一个两位数，
         然后按照下面的规则判定用户是否能赢。
         1)如果用户输入的数匹配彩票的实际顺序，奖金10 000美元。
         2)如果用户输入的所有数字匹配彩票的所有数字，但顺序不一致，奖金 3 000美元。
         3)如果用户输入的一个数字仅满足顺序情况下匹配彩票的一个数字，奖金1 000美元。
         4)如果用户输入的一个数字仅满足非顺序情况下匹配彩票的一个数字，奖金500美元。
         5)如果用户输入的数字没有匹配任何一个数字，则彩票作废。
         提示：使用Math.random() 产生随机数
         Math.random() 产生[0,1)范围的随机值
         Math.random() * 90：[0,90)
         Math.random() * 90 + 10：[10,100) 即得到  [10,99]
         使用(int)(Math.random() * 90  + 10)产生一个两位数的随机数。
         */
        //1、随机产生一个两位数[10~99]
        double num = Math.random(); // [0,1) 小数
        num = num * 90 + 10;        // [10,100) 小数
        int suiJi = (int)num;       // [10,99] 整数

        //2、接收用户输入
        Scanner input = new Scanner(System.in);
        System.out.print("请下注（两位数）：");
        int cai = input.nextInt();

        //3、判断是否中奖
		/*
		1)如果用户输入的数匹配彩票的实际顺序，奖金10 000美元。
		2)如果用户输入的所有数字匹配彩票的所有数字，但顺序不一致，奖金 3 000美元。
		3)如果用户输入的一个数字仅满足顺序情况下匹配彩票的一个数字，奖金1 000美元。
		4)如果用户输入的一个数字仅满足非顺序情况下匹配彩票的一个数字，奖金500美元。
		5)如果用户输入的数字没有匹配任何一个数字，则彩票作废。
		*/
        int suiJiShi = suiJi/10;
        int suiJiGe = suiJi%10;
        int caiShi = cai/10;
        int caiGe = cai%10;

        if(suiJi == cai){
            System.out.println("奖金10 000美元");
        }else if(suiJiShi==caiGe && suiJiGe==caiShi){
            System.out.println("奖金3 000美元");
        }else if(suiJiShi==caiShi || suiJiGe==caiGe){
            System.out.println("奖金1 000美元");
        }else if(suiJiShi==caiGe || suiJiGe==caiShi){
            System.out.println("奖金500美元");
        }else{
            System.out.println("谢谢惠顾");
        }
        System.out.println("本期中奖号码：" + suiJi);

    }
}
