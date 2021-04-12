package cn.zjiali.robot.factory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 业务类工厂
 *
 * @author zJiaLi
 * @since 2021-04-07 21:19
 */
public class ServiceFactory {

    private static final Map<String, Object> SERVICE_MAP = new ConcurrentHashMap<>();

    public static synchronized void put(String name, Object o) {
        SERVICE_MAP.put(name, o);
    }

    public static synchronized Object get(String serviceName) {
        return SERVICE_MAP.get(serviceName);
    }

}
