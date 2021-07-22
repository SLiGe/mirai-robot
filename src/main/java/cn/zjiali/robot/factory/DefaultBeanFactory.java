package cn.zjiali.robot.factory;


import cn.zjiali.robot.entity.bean.BeanDefinition;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

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
        BeanDefinition beanDefinition = (BeanDefinition) beanMap.get(beanPrefix() + beanName);
        return (T) beanDefinition.getInstance();
    }

    @Override
    public <T> void putBean(String beanName, T bean) {
        BeanDefinition beanDefinition = new BeanDefinition();
        beanDefinition.setBeanAlias(beanName);
        Class<?> beanClass = bean.getClass();
        beanDefinition.setInterfaces(beanClass.getInterfaces());
        beanDefinition.setTypeClass(beanClass);
        beanDefinition.setInstance(bean);
        beanDefinition.setBeanName(beanClass.getSimpleName());
        beanMap.put(beanPrefix() + beanName, beanDefinition);
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> getBeanList(Class<T> requireType) {
        return (List<T>) beanMap.values().stream().filter(bean -> {
            List<?> beanInterfaces = ((BeanDefinition) bean).getBeanInterfaces();
            return beanInterfaces.contains(requireType);
        }).map(bean -> ((BeanDefinition) bean).getInstance()).collect(Collectors.toList());
    }

    @Override
    public String beanPrefix() {
        return "";
    }

    public static BeanFactory getInstance() {
        return beanFactory;
    }
}
