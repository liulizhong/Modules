package 项目二实时项目.gmall2020_publisher.src.main.java.com.xuexi.publisher.service.impl;

import com.xuexi.publisher.mapper.TrademarkMapper;
import com.xuexi.publisher.service.MySQLService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Author: lizhong.liu
 * Date: 2020/9/24
 * Desc:
 */
@Service
public class MySQLServiceImpl implements MySQLService {

    @Autowired
    TrademarkMapper trademarkMapper;

    @Override
    public List<Map> getTrademarkSum(String startDate, String endDate, int topN) {
        return trademarkMapper.selectTrademarkSum(startDate,endDate,topN);
    }
}
