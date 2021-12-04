package utils;

import java.sql.*;

public class JDBCUtil {

    private static final String DRIVER_CLASS = "com.mysql.jdbc.Driver";
    private static final String URL = "jdbc:mysql://hadoop102:3306/ct?useUnicode=true&characterEncoding=UTF-8";
    private static final String USER_NAME = "root";
    private static final String PASSWD = "000000";

    private JDBCUtil() {
    }

    //定义连接
    public static Connection connection = null;

    private static Connection getConn() {

        Connection connection = null;

        try {
            //加载驱动
            Class.forName(DRIVER_CLASS);

            //获取连接
            connection = DriverManager.getConnection(URL, USER_NAME, PASSWD);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return connection;
    }

    //关闭资源
    public static void close(ResultSet resultSet, PreparedStatement preparedStatement, Connection connection) {

        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if (preparedStatement != null) {
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    //单例获取连接
    public static Connection getInstance() {

        //第一次判断连接是否创建
        if (connection == null) {
            //创建连接
            synchronized (JDBCUtil.class) {

                //第二次判断连接是否创建
                if (connection == null) {
                    connection = getConn();
                }
            }
        }

        return connection;
    }
}