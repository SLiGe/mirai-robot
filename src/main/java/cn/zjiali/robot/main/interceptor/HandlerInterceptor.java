package cn.zjiali.robot.main.interceptor;

import net.mamoe.mirai.event.events.MessageEvent;

/**
 * @author zJiaLi
 * @since 2021-06-14 12:25
 */
public interface HandlerInterceptor {

    default boolean preHandle(MessageEvent messageEvent)
            throws Exception {

        return true;
    }

    default void postHandle(MessageEvent messageEvent) throws Exception {
    }

    default void afterCompletion(MessageEvent messageEvent) throws Exception {
    }
}
