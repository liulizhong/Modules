package com.xuexi.springbootswagger.tmp;

import org.apache.hadoop.hive.conf.HiveConf;
import org.apache.hadoop.hive.metastore.IMetaStoreClient;
import org.apache.hadoop.hive.metastore.RetryingMetaStoreClient;
import org.apache.hadoop.hive.metastore.api.Database;
import org.apache.hadoop.hive.metastore.api.FieldSchema;
import org.apache.hadoop.hive.metastore.api.MetaException;
import org.apache.thrift.TException;
import org.slf4j.Logger;
import java.util.List;


/**
 * @author lizhong.liu
 * @version TODO
 * @class ??
 * @CalssName HiveClient
 * @create 2020-07-27 17:03
 * @Des TODO
 */

public class HiveSideXmlClient {
    protected final Logger logger = org.slf4j.LoggerFactory.getLogger(this.getClass());
    IMetaStoreClient client;

    // 初始化hiveClient
    public HiveSideXmlClient() {
        try {
            HiveConf hiveConf = new HiveConf();
            hiveConf.addResource("hive-site.xml");
            client = RetryingMetaStoreClient.getProxy(hiveConf);
        } catch (MetaException ex) {
            logger.error(ex.getMessage());
        }
    }

    // 获取所有数据库名
    public List<String> getAllDatabases() {
        List<String> databases = null;
        try {
            databases = client.getAllDatabases();
        } catch (TException ex) {
            logger.error(ex.getMessage());
        }
        return databases;
    }

    // 获取指定数据库
    public Database getDatabase(String db) {
        Database database = null;
        try {
            database = client.getDatabase(db);
        } catch (TException ex) {
            logger.error(ex.getMessage());
        }
        return database;
    }

    // 获取指定数据指定表
    public List<FieldSchema> getSchema(String db, String table) {
        List<FieldSchema> schema = null;
        try {
            schema = client.getSchema(db, table);
        } catch (TException ex) {
            logger.error(ex.getMessage());
        }
        return schema;
    }

    // 获取指定数据库下所有表
    public List<String> getAllTables(String db) {
        List<String> tables = null;
        try {
            tables = client.getAllTables(db);
        } catch (TException ex) {
            logger.error(ex.getMessage());
        }
        return tables;
    }

    // 获取指定数据库下指定表路径
    public String getLocation(String db, String table) {
        String location = null;
        try {
            location = client.getTable(db, table).getSd().getLocation();
        } catch (TException ex) {
            logger.error(ex.getMessage());
        }
        return location;
    }
}
