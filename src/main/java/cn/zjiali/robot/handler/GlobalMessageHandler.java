package cn.zjiali.robot.handler;

import cn.hutool.core.collection.CollectionUtil;
import cn.zjiali.robot.factory.ServiceFactory;
import cn.zjiali.robot.main.interceptor.HandlerInterceptor;
import cn.zjiali.robot.model.message.OutMessage;
import net.mamoe.mirai.event.events.AbstractMessageEvent;
import net.mamoe.mirai.event.events.FriendMessageEvent;
import net.mamoe.mirai.event.events.GroupMessageEvent;

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
     * @param abstractMessageEvent 消息事件
     * @return boolean
     */
   default boolean doPreHandle(AbstractMessageEvent abstractMessageEvent){
       boolean preHandle = false;
       List<HandlerInterceptor> handlerInterceptors = ServiceFactory.getInstance().getBeanList(HandlerInterceptor.class);
       if (CollectionUtil.isNotEmpty(handlerInterceptors)) {
           for (HandlerInterceptor handlerInterceptor : handlerInterceptors) {
               try {
                   preHandle = handlerInterceptor.preHandle(abstractMessageEvent);
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
    void handleGroupMessage(GroupMessageEvent event);

    /**
     * 处理好友消息
     *
     * @param event 好友消息事件
     */
    void handleFriendMessage(FriendMessageEvent event);

    /**
     * 消息事件后置处理
     *
     * @param abstractMessageEvent 消息事件
     * @param outMessageList       输出消息列表
     */
    default void triggerAfterCompletion(AbstractMessageEvent abstractMessageEvent, List<OutMessage> outMessageList){
        List<HandlerInterceptor> handlerInterceptors = ServiceFactory.getInstance().getBeanList(HandlerInterceptor.class);
        if (CollectionUtil.isNotEmpty(handlerInterceptors)) {
            for (HandlerInterceptor handlerInterceptor : handlerInterceptors) {
                try {
                    handlerInterceptor.afterCompletion(abstractMessageEvent, outMessageList);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
