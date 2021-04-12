package cn.zjiali.robot.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 应用注解
 *
 * @author zJiaLi
 * @since 2021-04-07 21:32
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Application {

    /**
     * 应用名
     */
    String name() default "Mirai机器人";

    /**
     * 扫描包路径
     */
    String[] basePackages() default "";
}
