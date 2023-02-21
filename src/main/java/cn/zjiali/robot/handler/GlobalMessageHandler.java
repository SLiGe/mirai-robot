package cn.zjiali.robot.handler;

import cn.hutool.core.collection.CollectionUtil;
import cn.zjiali.robot.main.interceptor.HandlerInterceptor;
import cn.zjiali.robot.model.message.OutMessage;
import cn.zjiali.robot.util.GuiceUtil;
import net.mamoe.mirai.event.events.FriendMessageEvent;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.event.events.MessageEvent;

import java.util.List;

/**
 * 消息事件总处理器
 * * <p>
 * * default see {@link DefaultGlobalMessageHandler}
 * * </p>
 *
 * @author zJiaLi
 * @since 2021-09-04 15:15
 */
public interface GlobalMessageHandler {

    /**
     * 消息事件前置处理
     *
     * @param messageEvent 消息事件
     * @return boolean
     */
    default boolean doPreHandle(MessageEvent messageEvent) {
        boolean preHandle = false;
        List<HandlerInterceptor> handlerInterceptors = GuiceUtil.getMultiBean(HandlerInterceptor.class);
        if (CollectionUtil.isNotEmpty(handlerInterceptors)) {
            for (HandlerInterceptor handlerInterceptor : handlerInterceptors) {
                try {
                    preHandle = handlerInterceptor.preHandle(messageEvent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (!preHandle) break;
            }
        }
        return preHandle;
    }

    /**
     * 处理群组消息
     *
     * @param event 群组消息事件
     */
    void handleGroupMessageEvent(GroupMessageEvent event);

    /**
     * 处理好友消息
     *
     * @param event 好友消息事件
     */
    void handleFriendMessageEvent(FriendMessageEvent event);

    /**
     * 处理其他消息事件 如果陌生人消息,临时会话...
     *
     * @param event 消息事件
     */
    void handleOtherMessageEvent(MessageEvent event);

    /**
     * 消息事件后置处理
     *
     * @param messageEvent   消息事件
     * @param outMessageList 输出消息列表
     */
    default void triggerAfterCompletion(MessageEvent messageEvent, List<OutMessage> outMessageList) {
        List<HandlerInterceptor> handlerInterceptors = GuiceUtil.getMultiBean(HandlerInterceptor.class);
        if (CollectionUtil.isNotEmpty(handlerInterceptors)) {
            for (HandlerInterceptor handlerInterceptor : handlerInterceptors) {
                try {
                    handlerInterceptor.afterCompletion(messageEvent, outMessageList);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
