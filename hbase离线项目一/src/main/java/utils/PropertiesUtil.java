package utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author lizhong.liu
 * @version TODO
 * @class ??
 * @CalssName PropertiesUtil
 * @create 2020-08-10 17:44
 * @Des TODO
 */
public class PropertiesUtil {
    public static Properties properties = null;

    static {
        try {
            // 加载配置属性从，初始化properties
            InputStream inputStream = ClassLoader.getSystemResourceAsStream("kafka.properties");
            // 通过类加载器读取配置文件，创建输入流
            properties = new Properties();
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }
}
