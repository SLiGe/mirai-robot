package cn.zjiali.robot.factory;

import cn.zjiali.robot.handler.Handler;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zJiaLi
 * @since 2020-10-30 09:24
 */
public class HandlerFactory {

    private static final Map<String, Handler> handlerMap = new HashMap<>();

    public static void put(String pluginName, String handler) {
        try {
            Class<?> handlerClass = Class.forName(handler);
            Handler instance = (Handler) handlerClass.getConstructor().newInstance();
            handlerMap.put(pluginName, instance);
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public static Handler get(String pluginName) {
        return handlerMap.get(pluginName);
    }
}
