package com.xuexi.springbootswagger.tmp;

import lombok.val;
import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.hive.HiveContext;


/**
 * @author lizhong.liu
 * @version TODO
 * @class 获取spark session链接客户端
 * @CalssName UtilsSparkSC
 * @create 2020-07-27 14:21
 * @Des TODO
 */

public class UtilsSparkSC {
    public static SparkSession getNewSparkSession() {
//        SparkConf sparkConf = new SparkConf().setAppName("hive-spark-n");
//        SparkContext sparkContext = new SparkContext(sparkConf);
//        HiveContext hiveContext = new HiveContext(sparkContext);
//        val tableName="hive_test";

        SparkSession sparkSession = SparkSession.builder().master("local").appName("Spark SQL basic Hive example")
                .config("spark.sql.warehouse.dir", "hdfs://192.168.1.241:8020/apps/hive/warehouse")
                .enableHiveSupport()
                .getOrCreate();
        return sparkSession;
    }
}
