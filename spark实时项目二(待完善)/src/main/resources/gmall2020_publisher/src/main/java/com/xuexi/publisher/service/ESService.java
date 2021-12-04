package 项目二实时项目.gmall2020_publisher.src.main.java.com.xuexi.publisher.service;

import java.util.Map;

/**
 * Author: lizhong.liu
 * Date: 2020/9/15
 * Desc: 业务操作接口
 */
public interface ESService {
    //获取日活总数
    public Long getDauTotal(String date);

    //日活的分时查询{"00"->10,"01"->20,..."23"->30}
    public Map<String,Long> getDauHour(String date);

}
