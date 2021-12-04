//package alltool.项目二实时项目.gmall2020_publisher.src.main.java.com.xuexi.publisher.controller;
//
//import com.xuexi.publisher.service.MySQLService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//import org.springframework.stereotype.Controller;
//import org.springframework.stereotype.Service;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
///**
// * Author: lizhong.liu
// * Date: 2020/9/24
// * Desc:
// */
//@RestController
//public class DataVController {
//
//    @Autowired
//    MySQLService mySQLService;
//
//    @RequestMapping("/trademark-sum")
//    public Object trademarkSum(String startDate, String endDate, int topN) {
//        //Map:{"trademark_id":"001","trademark_name":"xxxx","amount":1000}
//        //{"x":品牌名称,"y":当前品牌交易额汇总,"s":1}
//        List<Map> mapList = mySQLService.getTrademarkSum(startDate, endDate, topN);
//        List<Map> rsList = new ArrayList<>();
//        for (Map map : mapList) {
//            Map rsMap = new HashMap();
//            rsMap.put("x",map.get("trademark_name"));
//            rsMap.put("y",map.get("amount"));
//            rsMap.put("s",1);
//            rsList.add(rsMap);
//        }
//        return rsList;
//    }
//}
