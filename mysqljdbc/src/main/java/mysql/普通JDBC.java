package mysql;

import java.sql.*;

public class 普通JDBC {
    private static String mysql_driverName = "com.mysql.jdbc.Driver";
    private static String mysql_dbURL = "jdbc:mysql://10.238.251.4:3306/tshz?rewriteBatchedStatements=true&characterEncoding=UTF-8"; // 指定编码集utf8
    //String sqlserver_driverName = "com.microsoft.sqlserver.jdbc.SQLServerDriver";         //SQL server的连接，除了这两行其他都一样
    //String sqlserver_dbURL = "jdbc:sqlserver://10.238.255.225:1433;DatabaseName=tshz";
    private static String mysql_userName = "root";
    private static String mysql_userPwd = "pwd@123";
    private static Connection mysql_dbConn;
    private static Statement mysql_statement;

    static {    // 初始化MySQL的所有连接配置
        try {
            Class.forName(mysql_driverName);
            mysql_dbConn = DriverManager.getConnection(mysql_dbURL, mysql_userName, mysql_userPwd);
            System.out.println("MySQL连接数据库成功");
            mysql_statement = mysql_dbConn.createStatement();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 测试使用
    public static void main(String[] args) throws Exception {
        // 3、执行 sql 语句，获取前天的数据并插入mysql数据库
        ResultSet resultSet = mysql_statement.executeQuery("select * from kaoqin");
        while (resultSet.next()) {
            String insertSql = "INSERT INTO kaoqin_back VALUES(" + resultSet.getInt(1) + "," + "\"" + resultSet.getString(2) + "\")";
            boolean execute = mysql_statement.execute(insertSql);
            System.out.println("插入：" + execute);
        }
        resultSet.close();
        mysql_statement.close();
        mysql_dbConn.close();
    }
}

