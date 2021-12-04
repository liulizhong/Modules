package com.xuexi.springbootswagger.tmp;

import com.xuexi.springbootswagger.bean.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.spark.api.java.function.ForeachFunction;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * @author lizhong.liu
 * @version TODO
 * @class ??
 * @CalssName UserController
 * @create 2020-07-24 17:49
 * @Des TODO
 */

@Api(tags = "spark-自动化数据管理")  //接口集的标签题
@RestController
public class OpcAlldetailController {
    @ApiOperation(value = "查询操作", notes = "用户查询操作")
    @GetMapping(value = "/select_opcnametest")
    public String select_opcname(@ApiParam(value = "系统名", required = false, defaultValue = "defaultValue") @RequestParam String systemname,
                                       @ApiParam(value = "分类名", required = false, defaultValue = "defaultValue") @RequestParam String categoryname,
                                       @ApiParam(value = "设备名", required = false, defaultValue = "defaultValue") @RequestParam String equipmentname) {
        SparkSession sparkSession = UtilsSparkSC.getNewSparkSession();

        String sql = "select opcname from tashanopc.dwd_rhopcsys_tashan_tb_opc_alldetail where 1 = 1";
        if (!"defaultValue".equals(systemname)) {
            sql += " and systemname = \"" + systemname + "\"";
        }
        if (!"defaultValue".equals(categoryname)) {
            sql += " and categoryname = \"" + categoryname + "\"";
        }
        if (!"defaultValue".equals(equipmentname)) {
            sql += " and equipmentname = \"" + equipmentname + "\"";
        }
        Dataset<Row> dataset = sparkSession.sql(sql);
        StringBuilder stringBuilder = new StringBuilder("");
        dataset.foreach((str) -> {
            stringBuilder.append(str);
        });
        return stringBuilder.toString();
    }

    @ApiOperation(value = "新增操作", notes = "用户新增操作")
    @GetMapping(value = "/add")
    public User add(User user) {
        return user;
    }

}
