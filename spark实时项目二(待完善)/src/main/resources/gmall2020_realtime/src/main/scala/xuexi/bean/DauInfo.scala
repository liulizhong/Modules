//package 项目二实时项目.gmall2020-realtime.src.main.scala.xuexi.bean

/**
  * Author: lizhong.liu
  * Desc:  封装日活数据的样例类
  */
case class DauInfo(
                    mid:String,//设备id
                    uid:String,//用户id
                    ar:String,//地区
                    ch:String,//渠道
                    vc:String,//版本
                    var dt:String,//日期
                    var hr:String,//小时
                    var mi:String,//分钟
                    ts:Long //时间戳
                  ) {}

