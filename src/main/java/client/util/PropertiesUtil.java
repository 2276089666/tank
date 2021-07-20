package client.util;

import java.io.IOException;
import java.util.Properties;

/**
 * @Author ws
 * @Date 2021/7/16 8:33
 */
public class PropertiesUtil {


    private static Properties properties;

    static {
        properties = new Properties();
        try {
            properties.load(PropertiesUtil.class.getClassLoader().getResourceAsStream("config.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String get(String key) {
        return (String) properties.get(key);
    }
}
