package 项目二实时项目.gmall2020_publisher.src.main.java.com.xuexi.publisher.service;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Author: lizhong.liu
 * Date: 2020/9/23
 * Desc:
 */
public interface ClickHouseService {
    //获取当日交易额总和
    public BigDecimal getOrderAmountTotal(String date);

    //获取当日交易额分时值
    public Map<String,BigDecimal> getOrderAmountHourMap(String date);
}
