//package 项目二实时项目.gmall2020-realtime.src.main.scala.xuexi.bean

/**
  * Author: lizhong.liu
  * Desc: 用户样例类
  */
case class UserInfo(
                     id:String,
                     user_level:String,
                     birthday:String,
                     gender:String,
                     var age_group:String,//年龄段
                     var gender_name:String) //性别
