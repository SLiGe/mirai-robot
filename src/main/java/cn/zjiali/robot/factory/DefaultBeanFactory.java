package cn.zjiali.robot.factory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zJiaLi
 * @since 2021-05-28 11:00
 */
public class DefaultBeanFactory implements BeanFactory {

    private final Map<String, Object> beanMap = new ConcurrentHashMap<>();

    private static final BeanFactory beanFactory = new DefaultBeanFactory();

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getBean(String beanName, Class<T> requireType) {
        return (T) beanMap.get(beanPrefix() + beanName);
    }

    @Override
    public <T> void putBean(String beanName, T bean) {
        beanMap.put(beanPrefix() + beanName, bean);
    }

    @Override
    public String beanPrefix() {
        return "";
    }

    public static BeanFactory getInstance() {
        return beanFactory;
    }
}
