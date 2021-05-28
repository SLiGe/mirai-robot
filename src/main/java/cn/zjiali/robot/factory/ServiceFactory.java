package cn.zjiali.robot.factory;

/**
 * 业务类工厂
 *
 * @author zJiaLi
 * @since 2021-04-07 21:19
 */
public class ServiceFactory extends AbstractBeanFactory {

    private final static ServiceFactory serviceFactory = new ServiceFactory();

    public static ServiceFactory getInstance() {
        return serviceFactory;
    }

    public void put(String name, Object o) {
        putBean(name, o);
    }

    public <T> T get(String serviceName, Class<T> requireType) {
        return getBean(serviceName, requireType);
    }

    @Override
    public String beanPrefix() {
        return "Service-";
    }
}
