package cn.zjiali.robot.annotation;

import java.lang.annotation.*;

/**
 * @author zJiaLi
 * @since 2021-07-02 14:52
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Component {
    String name() default "";
}
