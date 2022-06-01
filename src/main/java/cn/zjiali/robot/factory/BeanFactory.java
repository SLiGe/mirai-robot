package cn.zjiali.robot.factory;

import java.util.List;

/**
 * @author zJiaLi
 * @since 2021-05-28 11:42
 */
@Deprecated
public interface BeanFactory {

    <T> T getBean(String beanName, Class<T> requireType);

    <T> List<T> getBeanList(Class<T> requireType);

    <T> void putBean(String beanName, T bean);

    String beanPrefix();

    default void fillBeanDefinitionFields() {
    }
}
