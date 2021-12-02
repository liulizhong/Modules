package mysql.druid工具类;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import org.apache.commons.dbutils.QueryRunner;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

/*

 */
public class DBUtils {
    static DataSource ds;

    static {
        Properties pro = new Properties();
        try {
            pro.load(new FileInputStream(new File("src\\main\\java\\mysql\\druid工具类\\druid.properties")));
            ds = DruidDataSourceFactory.createDataSource(pro);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static QueryRunner qr = new QueryRunner(ds);
}
