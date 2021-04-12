package cn.zjiali.robot.factory;

import cn.zjiali.robot.handler.Handler;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zJiaLi
 * @since 2020-10-30 09:24
 */
public class HandlerFactory {

    private static final Map<String, Handler> handlerMap = new ConcurrentHashMap<>();

    public static Handler put(String pluginName, String handler) {
        try {
            Class<?> handlerClass = Class.forName(handler);
            Handler instance = (Handler) handlerClass.getConstructor().newInstance();
            handlerMap.put(pluginName, instance);
            return instance;
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Handler get(String pluginName) {
        return handlerMap.get(pluginName);
    }
}
