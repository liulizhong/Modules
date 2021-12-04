package com.xuexi.springbootswagger.tmp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class HiveTest {
    private static String driverName = "org.apache.hive.jdbc.HiveDriver";

    public static void main(String[] args) throws Exception {
        long startTime = System.currentTimeMillis();

        Class.forName(driverName);
        Connection con = DriverManager.getConnection("jdbc:hive2://192.168.10.11:10000/test", "hive", null);
        if(con==null)
            System.out.println("连接失败");
        else {
            Statement stmt = con.createStatement();
            String sql = "SELECT count(*) FROM clph";
            System.out.println("Running: " + sql);
            ResultSet rs = stmt.executeQuery("select count(*) from clph where data_time between '2020-06-11 09:00:00' and '2020-06-11 10:00:00' ");
            while (rs.next()) {
                System.out.print(rs.getString(1) + "^");
                System.out.print(rs.getString(2) + "^");
                System.out.print(rs.getString(3) + "^");
                System.out.print(rs.getString(4) + "^");
                System.out.println(rs.getString(5));
            }
            rs.close();
            con.close();

            long endTime = System.currentTimeMillis();
            System.out.println("程序用时：" + (endTime-startTime) + "ms");
        }
    }
}
