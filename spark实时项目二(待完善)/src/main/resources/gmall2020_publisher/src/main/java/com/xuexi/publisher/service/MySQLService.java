package 项目二实时项目.gmall2020_publisher.src.main.java.com.xuexi.publisher.service;

import java.util.List;
import java.util.Map;

/**
 * Author: lizhong.liu
 * Date: 2020/9/24
 * Desc: 品牌统计service
 */
public interface MySQLService {

    public List<Map> getTrademarkSum(String startDate, String endDate, int topN);
}
