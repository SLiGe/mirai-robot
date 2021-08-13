package cn.zjiali.robot.manager;

import cn.zjiali.robot.factory.MessageFactory;
import cn.zjiali.robot.util.MessageUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zJiaLi
 * @since 2021-08-13 11:02
 */
public class DefaultMessageManager implements MessageManager {

    private final Map<String, Method> doMethodMap = new HashMap<>();
    private final MessageFactory messageFactory = new MessageFactory();

    {
        Class<MessageFactory> messageFactoryClass = MessageFactory.class;
        Method[] declaredMethods = messageFactoryClass.getDeclaredMethods();
        for (Method declaredMethod : declaredMethods) {
            doMethodMap.put(declaredMethod.getName(), declaredMethod);
        }
    }

    @Override
    public String produce(String doName, Object... params) {
        Method method = doMethodMap.get(doName);
        try {
            Object message = method.invoke(messageFactory, params);
            return preProduce() + message + afterProduce();
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }


}
