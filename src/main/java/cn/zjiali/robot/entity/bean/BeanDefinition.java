package cn.zjiali.robot.entity.bean;

import lombok.Data;

import java.util.Arrays;
import java.util.List;

/**
 * @author zJiaLi
 * @since 2021-07-22 10:43
 */
@Data
public class BeanDefinition {

    /**
     * bean名称, --> simpleName
     */
    private String beanName;

    /**
     * bean别名 --> name='?'
     */
    private String beanAlias;

    /**
     * bean所继承接口
     */
    private Class<?>[] interfaces;

    /**
     * bean类型
     */
    private Class<?> typeClass;

    /**
     * 实例
     */
    private Object instance;

    public List<?> getBeanInterfaces() {
        return Arrays.asList(getInterfaces());
    }

}
