package cn.zjiali.robot.factory;

import cn.zjiali.robot.annotation.Autowired;
import cn.zjiali.robot.handler.Handler;

import java.lang.reflect.Field;
import java.util.List;

/**
 * @author zJiaLi
 * @since 2021-05-28 14:14
 */
public abstract class AbstractBeanFactory implements BeanFactory {

    @Override
    public <T> T getBean(String beanName, Class<T> requireType) {
        return DefaultBeanFactory.getInstance().getBean(beanName, requireType);
    }

    @Override
    public <T> void putBean(String beanName, T bean) {
        DefaultBeanFactory.getInstance().putBean(beanName, bean);
    }

    @Override
    public <T> List<T> getBeanList(Class<T> requireType) {
        return DefaultBeanFactory.getInstance().getBeanList(requireType);
    }

    public Object initialBean(Class<?> clazz) throws InstantiationException, IllegalAccessException {
        Object instance = clazz.newInstance();

        return instance;
    }

    public Object initialBean(String clazzName) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        return initialBean(Class.forName(clazzName));
    }

    private void fillHandlerFields(Class<?> handlerClass, Handler instance) throws IllegalAccessException, ClassNotFoundException, InstantiationException {
        Field[] declaredFields = handlerClass.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            Autowired autowired = declaredField.getAnnotation(Autowired.class);
            if (autowired == null) continue;
            String beanName = "";
            if ("".equals(autowired.value())) {
                beanName = declaredField.getType().getName();
            } else {
                beanName = autowired.value();
            }
            Object bean = getBean(beanName, Object.class);
            if (bean == null) {
                initialBean(beanName);
            }
            declaredField.setAccessible(true);
            declaredField.set(instance, bean);
        }
    }
}
