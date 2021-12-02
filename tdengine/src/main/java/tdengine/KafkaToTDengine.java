package tdengine;

import com.taosdata.jdbc.TSDBDriver;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Properties;

/*

 */
public class KafkaToTDengine {
    private static Connection conn;
    private static Statement stmt;
    private static Properties properties = null;
    private static Properties proOPC = new Properties();
    private static KafkaConsumer<String, String> kafkaConsumer = null;

    static {
        // 1、kafka配置
        properties = new Properties();
        properties.put("bootstrap.servers", "10.238.255.151:9092,10.238.255.152:9092,10.238.255.153:9092");
        properties.put("group.id", "Kafka151to241TDengine");
        properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        kafkaConsumer = new KafkaConsumer<String, String>(properties);

        try {
            // 2、创建TDengine链接并初始化，库:tashanopc。表：每个点表名。
            Class.forName("com.taosdata.jdbc.TSDBDriver");
            // URL的固定格式：jdbc:TSDB://{host_ip}:{port}/[database_name]?[user={user}|&password={password}|&charset={charset}|&cfgdir={config_dir}|&locale={locale}|&timezone={timezone}]
            String jdbcUrl = "jdbc:TAOS://192.168.1.241:6030/tashanopc?user=root&password=taosdata"; //{} 中的内容必须，[] 中为可选。固定格式：默认端口6030，默认用户名和密码。
            Properties connProps = new Properties();
            connProps.setProperty(TSDBDriver.PROPERTY_KEY_USER, "root");  //默认用户名。
            connProps.setProperty(TSDBDriver.PROPERTY_KEY_PASSWORD, "taosdata");  //默认密码。
            connProps.setProperty(TSDBDriver.PROPERTY_KEY_CONFIG_DIR, "/etc/taos"); //客户端配置文件目录路径，Linux OS 上默认值 /etc/taos ，Windows OS 上默认值 C:/TDengine/cfg。
            connProps.setProperty(TSDBDriver.PROPERTY_KEY_CHARSET, "UTF-8");         //客户端使用的字符集，默认值为系统字符集。
            connProps.setProperty(TSDBDriver.PROPERTY_KEY_LOCALE, "en_US.UTF-8");    //客户端语言环境，默认值系统当前 locale。
            connProps.setProperty(TSDBDriver.PROPERTY_KEY_TIME_ZONE, "UTC-8");       //客户端使用的时区，默认值为系统当前时区。
            conn = DriverManager.getConnection(jdbcUrl, connProps);
            stmt = conn.createStatement();
            stmt.executeUpdate("use tashanopc");

            // 3、初始化读取opc.properties配置文件
            InputStreamReader reader = new InputStreamReader(
                    new FileInputStream("src\\main\\java\\alltool\\tdengine\\opc.properties"),
                    "Gbk");
            proOPC.load(reader);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        SumConsumer();
    }

    /**
     * 全部数据
     */
    public static void SumConsumer() throws Exception {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //订阅opc点表Kafka-opc主题
        kafkaConsumer.subscribe(Arrays.asList("TDengine"));
        while (true) {
            ConsumerRecords<String, String> records = kafkaConsumer.poll(100);
            for (ConsumerRecord<String, String> record : records) {
                String[] split_record = record.value().split(",");
//                String opcname = split_record[1];
//                String status = split_record[3];
//                String equipmenttime = split_record[0];
//                String opcvalue = split_record[2];
                String opcProperties = proOPC.getProperty(split_record[1]);
                String[] proSplit = opcProperties.split("---");
                // create table superopc
                // (ts TIMESTAMP,opcvalue binary(20),equipmenttime binary(20),status binary(20))
                // TAGS(systemname binary(20),categoryname binary(20),equipmentname binary(20),opctype binary(20),description binary(20),isvalid binary(20),remarks binary(20));
                // opcname=systemname---categoryname---equipmentname---opctype---description---isvalid---remarks---opcunit
                // HJJC01FT01=安全监测系统---地面环境监测---虎龙沟风井---double---虎龙沟风井温度---TRUE---℃---empty
                System.out.println(proSplit.length);
                System.out.println(split_record.length);
                System.out.println(proSplit[0] + ", " + proSplit[1] + ", " + proSplit[2] + ", " + proSplit[3] + ", " + proSplit[4] + ", " + proSplit[5] + ", " + proSplit[6]);
                String sql = "insert into " + split_record[1] + " using superopc tags ("
                        + proSplit[0] + ", " + proSplit[1] + ", " + proSplit[2] + ", " + proSplit[3] + ", " + proSplit[4] + ", " + proSplit[5] + ", " + proSplit[6] +
                        ") values (" + split_record[4] + ", " + split_record[2] + "," + split_record[0] + "," + split_record[3] + ")";
                System.out.println("sql:" + sql);
            }
        }
    }
}