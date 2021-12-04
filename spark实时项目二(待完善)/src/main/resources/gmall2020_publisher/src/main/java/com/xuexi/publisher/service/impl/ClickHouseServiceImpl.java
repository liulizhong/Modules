package 项目二实时项目.gmall2020_publisher.src.main.java.com.xuexi.publisher.service.impl;

import com.xuexi.publisher.mapper.OrderWideMapper;
import com.xuexi.publisher.service.ClickHouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author: lizhong.liu
 * Date: 2020/9/23
 * Desc:
 */
@Service
public class ClickHouseServiceImpl implements ClickHouseService {

    @Autowired
    OrderWideMapper orderWideMapper;


    @Override
    public BigDecimal getOrderAmountTotal(String date) {
        return orderWideMapper.selectOrderAmountTotal(date);
    }

    @Override
    public Map<String, BigDecimal> getOrderAmountHourMap(String date) {
        Map<String, BigDecimal> rsMap = new HashMap<>();
        List<Map> mapList = orderWideMapper.selectOrderAmountHourMap(date);
        //List[{"hr"->"10","am":"50"},{"hr"->"11","am":"60"}]
        for (Map map : mapList) {
            rsMap.put(String.format("%02d",map.get("hr")),(BigDecimal)map.get("am"));
        }
        return rsMap;
    }
}
