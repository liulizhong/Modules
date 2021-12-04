import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lizhong.liu
 * @version TODO
 * @class ??
 * @CalssName LoggerController
 * @create 2020-11-12 15:56
 * @Des TODO
 */

// @Controller     // 标识为controller组件，交给Sprint容器管理，并接收处理请求  如果返回String，会当作网页进行跳转
@RestController    // @RestController = @Controller + @ResponseBody  会将返回结果转换为json进行响应
@Slf4j             // 引入
public class LoggerController {

    //注入KafkaTemplate
    @Autowired
    KafkaTemplate kafkaTemplate;

    //表示接收http://localhost:8080/applog,交给当前方法进行处理
    @RequestMapping("/applog")
    public String applog(@RequestBody String applog) {
        //System.out.println(log);
        //将采集到的数据进行落盘操作
        log.info(applog);
        //将采集到的数据进行分流（事件|启动），发送到Kafka对应的主题去
        JSONObject jsonObject = JSON.parseObject(applog);
        if (jsonObject.getJSONObject("start") != null) {
            //启动日志
            kafkaTemplate.send("gmall_start_bak", applog);
        } else {
            //事件日志
            kafkaTemplate.send("gmall_event_bak", applog);
        }
        return "success";
    }
}
