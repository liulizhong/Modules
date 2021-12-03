package filetool;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

/*
    读取配置文件工具类
 */
public class MyPropertiesUtil {
    public static String load(String propertiesName, String key) throws IOException {
        Properties properties = new Properties();
        properties.load(new FileReader(propertiesName));
        String strValue = properties.getProperty(key);
        return strValue;
    }

    public static void main(String[] args) throws IOException {
        String str = MyPropertiesUtil.load("D:\\workhouse\\Modules\\javabase\\src\\main\\resources\\condition.properties", "condition.params.json");
        System.out.println(str);
    }
}
