package 项目二实时项目.gmall2020_publisher.src.main.java.com.xuexi.publisher.controller;

import com.xuexi.publisher.service.ClickHouseService;
import com.xuexi.publisher.service.ESService;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Author: lizhong.liu
 * Date: 2020/9/15
 * Desc: 对外发布接口的类 （接收用户请求）
 * 表示层|控制层
 */

@RestController
public class PublisherController {

    //将ESService注入进来
    @Autowired
    ESService esService;

    @Autowired
    ClickHouseService clickHouseService;

    //访问路径：http://publisher:8070/realtime-total?date=2019-02-01
    /*
    //返回格式
    [
        {
            "id":"dau",
            "name":"新增日活",
            "value":1200
        },
    {"id":"new_mid","name":"新增设备","value":233}
    ]
    */
    @RequestMapping("/realtime-total")
    public Object realtimeTotal(@RequestParam("date") String dt){
        List<Map<String,Object>> dauList = new ArrayList<Map<String,Object>>();

        //定义一个map集合，用于封装新增日活数据
        Map<String,Object> dauMap = new HashMap<String,Object>();
        dauMap.put("id","dau");
        dauMap.put("name","新增日活");
        //获取指定日期的日活数
        Long dauTotal = esService.getDauTotal(dt);
        dauMap.put("value",dauTotal);
        dauList.add(dauMap);

        //定义一个map集合，用于封装新增设备数据
        Map<String,Object> midMap = new HashMap<String,Object>();
        midMap.put("id","mid");
        midMap.put("name","新增设备");
        midMap.put("value",66);
        dauList.add(midMap);

        //定义一个map集合，用于封装当日新增交易额
        Map<String,Object> amMap = new HashMap<String,Object>();
        amMap.put("id","order_amount");
        amMap.put("name","新增交易额");
        amMap.put("value",clickHouseService.getOrderAmountTotal(dt));
        dauList.add(amMap);

        return dauList;
    }

    /*//发布日活分时值接口
    数据格式：
        {
            "yesterday":{"11":383,"12":123,"17":88,"19":200 },
            "today":{"12":38,"13":1233,"17":123,"19":688 }
        }
    访问路径：http://publisher:8070/realtime-hour?id=dau&date=2019-02-01
    */
    @RequestMapping("/realtime-hour")
    public Object realtimeHour(@RequestParam("id")String id,
                               @RequestParam("date") String dt){

        if("dau".equals(id)){
            Map<String,Map<String,Long>> hourMap = new HashMap<>();

            //获取今天日活分时值
            Map<String, Long> tdMap = esService.getDauHour(dt);
            hourMap.put("today",tdMap);

            //获取昨天日期字符串
            String yd = getYd(dt);
            //获取昨天的日活分时值
            Map<String, Long> ydMap = esService.getDauHour(yd);
            hourMap.put("yesterday",ydMap);
            return hourMap;
        }else if("order_amount".equals(id)){
            Map<String,Map<String, BigDecimal>> orderHourMap = new HashMap<>();

            //获取今天日活分时值
            Map<String, BigDecimal> tdMap = clickHouseService.getOrderAmountHourMap(dt);
            orderHourMap.put("today",tdMap);
            String yd = getYd(dt);

            Map<String, BigDecimal> ydMap = clickHouseService.getOrderAmountHourMap(yd);
            orderHourMap.put("yesterday",ydMap);
            return orderHourMap;
        }else{
            return null;
        }
    }

    private  String getYd(String td){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String yd = null;
        try {
            Date tdDate = dateFormat.parse(td);
            Date ydDate = DateUtils.addDays(tdDate, -1);
            yd = dateFormat.format(ydDate);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("日期格式转变失败");
        }
        return yd;
    }

}
