package cn.zjiali.robot.factory;


import cn.hutool.core.util.StrUtil;
import cn.zjiali.robot.annotation.Autowired;
import cn.zjiali.robot.model.bean.BeanDefinition;
import cn.zjiali.robot.util.ClassUtil;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
        Optional<Object> beanByInterface = beanMap.values().stream().filter(bean -> ((BeanDefinition) bean).getInterfaces().contains(requireType)).findFirst();
        if (beanByInterface.isPresent()) {
            return (T) ((BeanDefinition) beanByInterface.get()).getInstance();
        }
        return beanDefinition == null ? null : (T) beanDefinition.getInstance();
    }

    @Override
    public <T> void putBean(String beanName, T bean) {
        BeanDefinition beanDefinition = new BeanDefinition();
        beanDefinition.setBeanAlias(beanName);
        Class<?> beanClass = bean.getClass();
        List<Class<?>> beanInterfaces = ClassUtil.getAllInterfaces(beanClass);
        beanDefinition.setInterfaces(beanInterfaces);
        beanDefinition.setTypeClass(beanClass);
        beanDefinition.setInstance(bean);
        beanDefinition.setBeanName(beanClass.getSimpleName());
        beanMap.put(beanPrefix() + beanName, beanDefinition);
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> getBeanList(Class<T> requireType) {
        return (List<T>) beanMap.values().stream().filter(bean -> {
            List<Class<?>> beanInterfaces = ((BeanDefinition) bean).getInterfaces();
            return beanInterfaces.contains(requireType);
        }).map(bean -> ((BeanDefinition) bean).getInstance()).collect(Collectors.toList());
    }

    @Override
    public String beanPrefix() {
        return "";
    }

    public void fillBeanDefinitionFields() {
        beanMap.values().forEach(
                bean -> {
                    try {
                        fillBeanFields(((BeanDefinition) bean).getTypeClass(), ((BeanDefinition) bean).getInstance());
                    } catch (IllegalAccessException | ClassNotFoundException | InstantiationException e) {
                        e.printStackTrace();
                    }
                }
        );
    }

    private void fillBeanFields(Class<?> handlerClass, Object instance) throws IllegalAccessException, ClassNotFoundException, InstantiationException {
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
            Object bean = getBean(beanName, Class.forName(declaredField.getType().getName()));
            if (bean == null) {
                bean = initialBean(beanName, declaredField.getType());
                fillBeanFields(getBeanDefinition(beanName).getTypeClass(), bean);
            }
            declaredField.setAccessible(true);
            declaredField.set(instance, bean);
        }
    }


    public Object initialBean(String beanName, Class<?> clazz) throws InstantiationException, IllegalAccessException {
        Object instance = clazz.newInstance();
        beanName = StrUtil.isBlank(beanName) ? clazz.getSimpleName() : beanName;
        putBean(beanName, instance);
        return getBeanDefinition(beanName).getInstance();
    }


    public BeanDefinition getBeanDefinition(String beanName) {
        return (BeanDefinition) beanMap.get(beanName);
    }


    public static BeanFactory getInstance() {
        return beanFactory;
    }
}
