package cn.zjiali.robot.factory;

/**
 * @author zJiaLi
 * @since 2021-05-28 11:42
 */
public interface BeanFactory {

    <T> T getBean(String beanName, Class<T> requireType);

    <T> void putBean(String beanName, T bean);

    String beanPrefix();
}
