package cn.zjiali.robot.factory;

import cn.zjiali.robot.annotation.Autowired;
import cn.zjiali.robot.handler.Handler;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

/**
 * @author zJiaLi
 * @since 2020-10-30 09:24
 */
public class HandlerFactory extends AbstractBeanFactory {

    private static final HandlerFactory handlerFactory = new HandlerFactory();

    public static HandlerFactory getInstance() {
        return handlerFactory;
    }

    public Handler put(String pluginName, String handler) {
        try {
            Class<?> handlerClass = Class.forName(handler);
            Handler instance = (Handler) handlerClass.getConstructor().newInstance();
          //  fillHandlerFields(handlerClass, instance);
            putBean(pluginName, instance);
            return instance;
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Handler get(String pluginName) {
        return getBean(pluginName, Handler.class);
    }

    private void fillHandlerFields(Class<?> handlerClass, Handler instance) throws IllegalAccessException {
        Field[] declaredFields = handlerClass.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            Autowired autowired = declaredField.getAnnotation(Autowired.class);
            if (autowired == null) continue;
            String beanName = "";
            if ("".equals(autowired.value())) {
                beanName = declaredField.getType().getSimpleName();
            } else {
                beanName = autowired.value();
            }
            Object bean = getBean(beanName, Object.class);
            if (bean == null) throw new RuntimeException("Bean Not Found! name: " + beanName);
            declaredField.setAccessible(true);
            declaredField.set(instance, bean);
        }
    }


    @Override
    public String beanPrefix() {
        return "Handler-";
    }

}
