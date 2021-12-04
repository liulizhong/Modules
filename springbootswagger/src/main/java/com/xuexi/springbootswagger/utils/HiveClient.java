package com.xuexi.springbootswagger.utils;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * @author lizhong.liu
 * @version TODO
 * @class hive客户端的获取静态方法
 * @CalssName HiveJDBCClient
 * @create 2020-07-28 10:39
 * @Des TODO
 */
public class HiveClient {
    /**
     * 获取HiveJDBC的连接客户端
     *
     * @return Connection
     * @throws Exception
     */
    public static Connection getHiveJDBCClient() throws Exception {
        Class.forName("org.apache.hive.jdbc.HiveDriver");
        Connection hiveJDBCConnection = DriverManager.getConnection("jdbc:hive2://192.168.1.241:10000/tashanopc", "hive", null);
        if (hiveJDBCConnection == null) {
            throw new Exception("连接失败");
        } else {
            return hiveJDBCConnection;
        }
    }

    /**
     * 获取 hivePresto的链接客户端--未安装
     *
     * @return Connection
     * @throws Exception
     */
    public static Connection getHivePrestoClient() throws Exception {
        Class.forName("com.facebook.presto.jdbc.PrestoDriver");
        Connection hivePrestoConnection = DriverManager.getConnection("jdbc:presto://192.168.10.10:8085/hive/test", "hive", null);
        if (hivePrestoConnection == null) {
            throw new Exception("连接失败");
        } else {
            return hivePrestoConnection;
        }
    }
}
