package influxdb;

import org.influxdb.BatchOptions;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Point;
import org.influxdb.dto.Query;

import java.util.concurrent.TimeUnit;

/*

 */
public class InfluxdbQuery {
    public static void main(String[] args) {
        InfluxDB influxDB = InfluxDBFactory.connect("http://192.168.1.245:8086", "administrator", "abc@123@!@#");
        String dbName = "mytestdb";
        influxDB.query(new Query("CREATE DATABASE " + dbName));
        influxDB.setDatabase(dbName);
        String rpName = "CustomRetentionPolicy";
        influxDB.query(new Query("CREATE RETENTION POLICY " + rpName + " ON " + dbName + " DURATION 30h REPLICATION 2 SHARD DURATION 30m DEFAULT"));
        influxDB.setRetentionPolicy(rpName);

        influxDB.enableBatch(BatchOptions.DEFAULTS);

        influxDB.write(Point.measurement("cpu")
                .time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
                .addField("idle", 90L)
                .addField("user", 9L)
                .addField("system", 1L)
                .build());

        influxDB.write(Point.measurement("disk")
                .time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
                .addField("used", 80L)
                .addField("free", 1L)
                .build());

        Query query = new Query("SELECT * FROM cpu", dbName);
        System.out.println(influxDB.query(query));

//        influxDB.query(new Query("DROP RETENTION POLICY " + rpName + " ON " + dbName));
//        influxDB.query(new Query("DROP DATABASE " + dbName));
        influxDB.close();
    }
}

