package cn.zjiali.robot.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * 消息工具类
 *
 * @author zJiaLi
 * @since 2021-04-04 20:24
 */
public class MessageUtil {

    private MessageUtil() {
    }

    /**
     * 替换消息内容
     *
     * @param message 发送消息内容
     * @param o       提花实例对象
     * @return
     */
    public static String replaceMessage(String message, Object o) {
        if (ObjectUtil.isNullOrEmpty(message) || ObjectUtil.isNullOrEmpty(o)) {
            return null;
        }
        Class<?> aClass = o.getClass();
        Field[] declaredFields = aClass.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            String fieldName = declaredField.getName();
            if ("serialVersionUID".equals(fieldName) || !message.contains(fieldName)) {
                continue;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("get").append(fieldName.substring(0, 1).toUpperCase()).append(fieldName.substring(1));
            try {
                Method method = aClass.getDeclaredMethod(sb.toString());
                String invokeValue = String.valueOf(method.invoke(o));
                fieldName = "{" + fieldName + "}";
                if (message.contains(fieldName)) {
                    message = message.replace(fieldName, invokeValue);
                }
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return message;
    }

    public static String replaceMessage(String message, Map<String, String> fillMap) {
        if (ObjectUtil.isNullOrEmpty(message) || ObjectUtil.isNullOrEmpty(fillMap)) {
            return null;
        }
        for (Map.Entry<String, String> entry : fillMap.entrySet()) {
            String fieldName = "{" + entry.getKey() + "}";
            if (message.contains(fieldName)) {
                message = message.replace(fieldName, entry.getValue());
            }
        }
        return message;
    }
}