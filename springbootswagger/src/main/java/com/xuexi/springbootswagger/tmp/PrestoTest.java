package com.xuexi.springbootswagger.tmp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class PrestoTest {
    public static void main(String[] args) throws Exception {
        long startTime = System.currentTimeMillis();

        Class.forName("com.facebook.presto.jdbc.PrestoDriver");
        Connection connection = DriverManager.getConnection("jdbc:presto://192.168.10.10:8085/hive/test","hive",null);  ;
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("select count(*) from clph where data_time between '2020-06-11 09:00:00' and '2020-06-11 10:00:00' ");
        while (rs.next()) {
            System.out.print(rs.getString(1) + "^");
//            System.out.print(rs.getString(2) + "^");
//            System.out.print(rs.getString(3) + "^");
//            System.out.print(rs.getString(4) + "^");
//            System.out.println(rs.getString(5));
        }
        rs.close();
        connection.close();


        long endTime = System.currentTimeMillis();
        System.out.println("程序用时：" + (endTime-startTime) + "ms");
    }
}
