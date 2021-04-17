package cn.zjiali.robot.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 配置项工具类
 *
 * @author zJiaLi
 * @since 2021-04-17 10:18
 */
public class PropertiesUtil {
    private PropertiesUtil() {
    }

    private static final Map<String, Properties> propertiesMap = new ConcurrentHashMap<>();

    /**
     * 获取配置内容
     *
     * @param propertiesFileName 配置文件名称
     * @param propertyName       配置项名称
     * @return 配置项值
     * @throws IOException IO
     */
    public static String getProperty(String propertiesFileName, String propertyName) throws IOException {
        if (propertiesMap.containsKey(propertiesFileName)) {
            return propertiesMap.get(propertiesFileName).getProperty(propertyName);
        }
        InputStream propertiesFileStream = PropertiesUtil.class.getResourceAsStream("/" + propertiesFileName);
        Properties properties = new Properties();
        properties.load(propertiesFileStream);
        propertiesMap.put(propertiesFileName, properties);
        return properties.getProperty(propertyName);
    }
}
