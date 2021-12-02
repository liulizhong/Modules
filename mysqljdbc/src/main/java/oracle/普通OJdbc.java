package oracle;

import oracle.jdbc.driver.OracleDriver;
import org.junit.jupiter.api.Test;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/*
 * @class 不用连接池，直接用jdbc连接oracle数据库
 */
public class 普通OJdbc {
    static Connection connect = null;
    static Statement statement = null;
    static ResultSet resultSet = null;
    static String name = "";
    static Map<String, String> map = new HashMap<String, String>();

    /**
     * 建立连接
     */
    static {
        try {
            Properties pro = new Properties();
            pro.put("user", "mkgk");
            pro.put("password", "mkgk");
            Driver driver = new OracleDriver();
            DriverManager.deregisterDriver(driver);
            connect = driver.connect("jdbc:oracle:thin:@10.238.255.250:1521:MKDB3D", pro);
            statement = connect.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //产量平衡
    public static Map<String, String> clph() throws Exception {
        //执行sql语句
        resultSet = statement.executeQuery("select *  from TB_EMP_ATTENDTIMERULE");

        //处理结果集
        while (resultSet.next()) {
            name = resultSet.getString("ATTRIBUTENAME");
            map.put(name, "clph");
            //System.out.println("点表名称："+name);  //打印输出结果集
        }
        close();
        return map;
    }

    /**
     * 释放资源
     *
     * @throws Exception
     */
    public static void close() throws Exception {
        if (resultSet != null) resultSet.close();
        if (statement != null) statement.close();
        if (connect != null) connect.close();
    }

    @Test
    public void test() throws Exception {
        Map<String, String> clph = clph();
        System.out.println(clph);
    }
}
