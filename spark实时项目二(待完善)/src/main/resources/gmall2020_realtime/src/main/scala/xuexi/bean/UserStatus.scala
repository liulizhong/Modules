//package 项目二实时项目.gmall2020-realtime.src.main.scala.xuexi.bean

/**
  * Author: lizhong.liu
  * Desc: 用于映射用户状态表的样例类
  */
case class UserStatus(
                       userId:String,  //用户id
                       ifConsumed:String //是否消费过   0首单   1非首单
                     )

