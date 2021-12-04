package hbaseanalyze.convertor;

import hbaseanalyze.kv.BaseDimension;
import hbaseanalyze.kv.ContactDimension;
import hbaseanalyze.kv.DateDimension;
import utils.JDBCUtil;
import utils.LRUCache;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DimensionConvertor {

    //初始化LRUCache
    private LRUCache lruCache = new LRUCache(10000);

    public int getID(BaseDimension baseDimension) throws SQLException {

        //获取缓存的key
        String lruCacheKey = getLruCacheKey(baseDimension);

        //1.查询缓存中是否有数据
        if (lruCache.containsKey(lruCacheKey)) {
            return lruCache.get(lruCacheKey);
        }

        //2.查询数据
        //3.如果没查询到数据，则插入
        //4.再次查询
        int id = execSql(baseDimension);

        if (id == 0) {
            throw new RuntimeException("未找到指定维度！！！");
        }

        //5.将查询结果放入缓存
        lruCache.put(lruCacheKey, id);

        return id;
    }

    //MySQL表交互，查询数据ID
    private int execSql(BaseDimension baseDimension) throws SQLException {

        int id = 0;

        //获取mysql连接
        Connection connection = JDBCUtil.getInstance();

        //获取SQL语句
        String[] sqls = getSqls(baseDimension);

        //第一次查询
        PreparedStatement preparedStatement = connection.prepareStatement(sqls[0]);

        //给预编译SQL赋值
        setArguments(preparedStatement, baseDimension);

        //执行查询，获取结果
        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            id = resultSet.getInt(1);
        } else {

            //执行插入数据
            preparedStatement = connection.prepareStatement(sqls[1]);
            setArguments(preparedStatement, baseDimension);
            preparedStatement.executeUpdate();

            //第二次查询数据
            preparedStatement = connection.prepareStatement(sqls[0]);
            setArguments(preparedStatement, baseDimension);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                id = resultSet.getInt(1);
            }
        }

        //资源关闭
        JDBCUtil.close(resultSet, preparedStatement, null);

        return id;
    }

    //给预编译SQL赋值
    private void setArguments(PreparedStatement preparedStatement, BaseDimension baseDimension) throws SQLException {

        //联系人维度
        if (baseDimension instanceof ContactDimension) {
            ContactDimension contactDimension = (ContactDimension) baseDimension;

            preparedStatement.setString(1, contactDimension.getPhoneNum());
            preparedStatement.setString(2, contactDimension.getName());
        } else {
            //时间维度
            DateDimension dateDimension = (DateDimension) baseDimension;

            preparedStatement.setInt(1, Integer.parseInt(dateDimension.getYear()));
            preparedStatement.setInt(2, Integer.parseInt(dateDimension.getMonth()));
            preparedStatement.setInt(3, Integer.parseInt(dateDimension.getDay()));
        }
    }

    //根据维度信息获取SQL语句（两条：查询，插入）
    private String[] getSqls(BaseDimension baseDimension) {

        String[] sqls = new String[2];

        //联系人维度
        if (baseDimension instanceof ContactDimension) {
            sqls[0] = "SELECT `id` FROM `tb_contacts` WHERE `telephone`=? AND `name`=?";
            sqls[1] = "INSERT INTO `tb_contacts` VALUES(null,?,?)";
        } else {
            sqls[0] = "SELECT `id` FROM `tb_dimension_date` WHERE `year`=? AND `month`=? AND `day`=?";
            sqls[1] = "INSERT INTO `tb_dimension_date` VALUES(null,?,?,?)";
        }

        return sqls;
    }

    private String getLruCacheKey(BaseDimension baseDimension) {

        if (baseDimension instanceof ContactDimension) {
            //如果是联系人维度
            ContactDimension contactDimension = (ContactDimension) baseDimension;
            return contactDimension.getPhoneNum();
        }

        //如果是时间维度
        DateDimension dateDimension = (DateDimension) baseDimension;
        return dateDimension.getYear() + "_" + dateDimension.getMonth() + "_" + dateDimension.getDay();
    }
}
