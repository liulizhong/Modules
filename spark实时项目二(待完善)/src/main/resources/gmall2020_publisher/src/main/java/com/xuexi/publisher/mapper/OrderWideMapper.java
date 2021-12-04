package 项目二实时项目.gmall2020_publisher.src.main.java.com.xuexi.publisher.mapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Author: lizhong.liu
 * Date: 2020/9/23
 * Desc:
 */
public interface OrderWideMapper {
    //查询当日交易额总数
    public BigDecimal selectOrderAmountTotal(String date);

    //查询当日交易额分时值
    public List<Map> selectOrderAmountHourMap(String date);
}
