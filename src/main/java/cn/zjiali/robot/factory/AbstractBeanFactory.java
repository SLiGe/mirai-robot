package cn.zjiali.robot.factory;

/**
 * @author zJiaLi
 * @since 2021-05-28 14:14
 */
public abstract class AbstractBeanFactory implements BeanFactory {

    @Override
    public <T> T getBean(String beanName, Class<T> requireType) {
        return DefaultBeanFactory.getInstance().getBean( beanName, requireType);
    }

    @Override
    public <T> void putBean(String beanName, T bean) {
        DefaultBeanFactory.getInstance().putBean( beanName, bean);
    }


}
