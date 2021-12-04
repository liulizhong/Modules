package com.xuexi.springbootswagger.controller;

import com.xuexi.springbootswagger.bean.DataOPCInfo;
import com.xuexi.springbootswagger.bean.OPCNameInfo;
import com.xuexi.springbootswagger.utils.HiveClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lizhong.liu
 * @version TODO
 * @class 通过hive客户端提供的数仓对外API接口-自动化数据部分
 * @CalssName UserController
 * @create 2020-07-24 17:49
 * @Des TODO
 */
@Api(tags = "自动化数据管理")  //接口集的标签题
@RestController
public class OpcDataController {
    @ApiOperation(value = "获取点表名", notes = "可输入系统名、分类名、设备名，来获取该类下所有点表名")
    @GetMapping(value = "/select_opcname")
    public List<String> select_opcname(@ApiParam(value = "系统名", required = false, defaultValue = "defaultValue") @RequestParam String systemname,
                                       @ApiParam(value = "分类名", required = false, defaultValue = "defaultValue") @RequestParam String categoryname,
                                       @ApiParam(value = "设备名", required = false, defaultValue = "defaultValue") @RequestParam String equipmentname) throws Exception {
        Connection hiveJDBCClient = HiveClient.getHiveJDBCClient();
        Statement stmt = hiveJDBCClient.createStatement();
        String selectSql = "select opcname from tashanopc.dwd_rhopcsys_tashan_tb_opc_alldetail where 1 = 1";
        if (!"defaultValue".equals(systemname)) {
            selectSql += " and systemname = \"" + systemname + "\"";
        }
        if (!"defaultValue".equals(categoryname)) {
            selectSql += " and categoryname = \"" + categoryname + "\"";
        }
        if (!"defaultValue".equals(equipmentname)) {
            selectSql += " and equipmentname = \"" + equipmentname + "\"";
        }
//        System.out.println("Running: " + sql);
        ResultSet rs = stmt.executeQuery(selectSql);
        List<String> listOPCName = new ArrayList<String>();
        while (rs.next()) {
//            System.out.print(rs.getString(1) + "^");
            listOPCName.add(rs.getString(1));
//                System.out.print(rs.getString(2) + "^");
//                System.out.print(rs.getString(3) + "^");
//                System.out.print(rs.getString(4) + "^");
//                System.out.println(rs.getString(5));
        }
        rs.close();
        hiveJDBCClient.close();
        return listOPCName;
    }


    @ApiOperation(value = "新增操作", notes = "新增点表，维度信息均为必填项。添加后不可更改！！！")
    @GetMapping(value = "/add_opcname")
    public OPCNameInfo add(OPCNameInfo opcInfo) throws Exception {
        Connection hiveJDBCClient = HiveClient.getHiveJDBCClient();
        Statement stmt = hiveJDBCClient.createStatement();
        String values = " values(\"" + opcInfo.getOpcname() + "\", \"" + opcInfo.getSystemname() + "\", \"" + opcInfo.getCategoryname() + "\", \"" + opcInfo.getEquipmentname()
                + "\", \"" + opcInfo.getOpctype() + "\", \"" + opcInfo.getDescription() + "\", " + opcInfo.getIsvalid() + ", \"" + opcInfo.getRemarks()
                + "\", \"" + opcInfo.getOpcunit() + "\", \"" + opcInfo.getCreatetime() + "\", \"" + opcInfo.getDeletetime() + "\")";
        String insertSql = "insert into table tashanopc.dwd_rhopcsys_tashan_tb_opc_alldetail" + values;
        System.out.println("insertSql: " + insertSql);
        int result = stmt.executeUpdate(insertSql);
        hiveJDBCClient.close();
        if (result != 1) {
            throw new Exception("数据插入错误！！！");
        } else {
            return opcInfo;
        }
    }



    @ApiOperation(value = "获取点表历史数据", notes = "必须输入点表名、起始时间、截止时间！！！！！！")
    @GetMapping(value = "/select_opcdata")
    public List<DataOPCInfo> select_opcdata(@ApiParam(value = "点表名", required = true) @RequestParam String opcname,
                                            @ApiParam(value = "起始时间", required = true) @RequestParam String startTime,
                                            @ApiParam(value = "截止时间", required = true) @RequestParam String endTime) throws Exception {
        Connection hiveJDBCClient = HiveClient.getHiveJDBCClient();
        Statement stmt = hiveJDBCClient.createStatement();
        if (opcname == null || opcname == "" || startTime == null || startTime == "" || endTime == null || endTime == "") {
            throw new Exception("参数有问题！！！");
        }
        String startDayPartition = startTime.substring(0,10);
        String endDayPartition = endTime.substring(0,10);
        String selectSql = "select\n" +
                "t2.opcname,\n" +
                "t1.writetime,\n" +
                "t1.opcvalue,\n" +
                "t1.status,\n" +
                "t2.systemname,\n" +
                "t2.categoryname,\n" +
                "t2.equipmentname,\n" +
                "t2.opctype,\n" +
                "t2.description,\n" +
                "t2.isvalid,\n" +
                "t2.remarks,\n" +
                "t2.opcunit,\n" +
                "t2.createtime,\n" +
                "t2.deletetime,\n" +
                "t1.equipmenttime\n" +
                "from \n" +
                "(select * from tashanopc.ods_rhopcsys_tashan_tb_opc_allhistory_d " +
                "where day >= '" + startDayPartition + "' and day <= '" + endDayPartition + "' and opcname = '" + opcname + "' and writetime >= '" + startTime + "' and writetime <= '" + endTime + "') t1\n" +
                "left join\n" +
                "(select * from tashanopc.dwd_rhopcsys_tashan_tb_opc_alldetail where opcname = '" + opcname + "') t2\n" +
                "on t1.opcname = t2.opcname";
        ResultSet resultSet = stmt.executeQuery(selectSql);
        List<DataOPCInfo> listOPCDatas = new ArrayList<DataOPCInfo>();
        while (resultSet.next()) {
            DataOPCInfo dataOPCInfo = new DataOPCInfo(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getString(5),
                    resultSet.getString(6),
                    resultSet.getString(7),
                    resultSet.getString(8),
                    resultSet.getString(9),
                    resultSet.getBoolean(10),
                    resultSet.getString(11),
                    resultSet.getString(12),
                    resultSet.getString(13),
                    resultSet.getString(14),
                    resultSet.getString(15)
            );
            listOPCDatas.add(dataOPCInfo);
        }
        resultSet.close();
        hiveJDBCClient.close();
        return listOPCDatas;
    }
}
